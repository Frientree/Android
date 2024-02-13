package com.d101.presentation.fruit

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentBeforeFruitCreateBaseBinding
import com.d101.presentation.tree.TreeViewModel
import com.navercorp.nid.NaverIdLoginSDK.getApplicationContext
import dagger.hilt.android.AndroidEntryPoint
import utils.CustomToast
import utils.repeatOnStarted

@AndroidEntryPoint
class BeforeFruitCreateBaseFragment : DialogFragment() {
    private val treeViewModel: TreeViewModel by viewModels({
        requireParentFragment()
    })
    private val viewModel: FruitViewModel by viewModels()
    private var _binding: FragmentBeforeFruitCreateBaseBinding? = null
    private val binding get() = _binding!!

    private var flip = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBeforeFruitCreateBaseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewModel.onGoSelectInputTypeView()

        val scale: Float = getApplicationContext().resources.displayMetrics.density
        val distance: Float =
            binding.fruitDialogCardView.cameraDistance * (scale + (scale / 3))

        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is FruitViewEvent.SelectInputTypeViewEvent -> {
                        navigateToDestinationFragment(SelectInputTypeFragment())
                    }

                    is FruitViewEvent.FruitCreationBySpeechViewEvent -> {
                        navigateToDestinationFragment(FruitCreationBySpeechFragment())
                    }

                    is FruitViewEvent.FruitCreationByTextViewEvent -> {
                        navigateToDestinationFragment(FruitCreationByTextFragment())
                    }

                    is FruitViewEvent.FruitCreationLoadingViewEvent -> {
                        navigateToDestinationFragment(FruitCreationLoadingFragment())
                    }

                    is FruitViewEvent.AfterFruitCreationViewEvent -> {
                        navigateToDestinationFragment(AfterFruitCreateFragment())
                    }

                    is FruitViewEvent.AppleEvent -> {
                        if (event.isApple) {
                            navigateToDestinationFragment(AppleFragment())
                        } else {
                            showToast("열매가 저장되었습니다!")
                            dialog?.dismiss()
                            treeViewModel.showFruitDialog()
                        }
                    }

                    is FruitViewEvent.ShowErrorToastEvent -> {
                        showToast(event.message)
                        dialog?.dismiss()
                    }

                    is FruitViewEvent.CardFlipEvent -> {
                        flipAnimation(event.color, flip, distance)
                        flip = !flip
                    }

                    is FruitViewEvent.OnServerMaintaining -> blockApp(event.message)
                }
            }
        }
    }

    private fun blockApp(message: String) {
        showToast(message)
        ActivityCompat.finishAffinity(requireActivity())
    }
    private fun flipAnimation(fruitColorValue: Int, flip: Boolean, distance: Float) {
        binding.fruitDialogCardView.cameraDistance = distance
        binding.fruitDialogCardView.animate().withLayer()
            .rotationY(90F)
            .setDuration(150)
            .withEndAction {
                if (flip) {
                    binding.fruitDialogCardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            fruitColorValue,
                        ),
                    )
                    viewModel.setAppleViewVisibility(flip)
                } else {
                    binding.fruitDialogCardView.setCardBackgroundColor(Color.WHITE)
                    viewModel.setAppleViewVisibility(flip)
                }
                binding.fruitDialogCardView.rotationY = -90F
                binding.fruitDialogCardView.animate().withLayer()
                    .rotationY(0F)
                    .setDuration(250)
                    .start()
            }.start()
    }

    private fun showToast(message: String) =
        CustomToast.createAndShow(requireActivity(), message)

    private fun navigateToDestinationFragment(destination: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.before_fragment_container_view, destination)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
