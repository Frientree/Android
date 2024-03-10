package com.d101.presentation.fruit

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.d101.domain.model.FruitResources
import com.d101.presentation.R
import com.d101.presentation.databinding.DialogFruitDetailBinding
import com.d101.presentation.model.FruitUiModel
import dagger.hilt.android.AndroidEntryPoint
import utils.darkenColor

@AndroidEntryPoint
class FruitDetailDialog : DialogFragment() {

    private var _binding: DialogFruitDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Base_FTR_FullScreenDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            setBackgroundDrawableResource(R.drawable.bg_white_radius_30dp)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_fruit_detail, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fruit = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(FRUIT_DATA, FruitUiModel::class.java)
        } else {
            arguments?.getParcelable(FRUIT_DATA) as? FruitUiModel
        }

        binding.lifecycleOwner = viewLifecycleOwner

        FruitResources.entries.find { it.fruitEmotion == binding.fruit?.fruitEmotion }
            ?.let { fruitResources ->
                val backgroundColor = resources.getColor(fruitResources.color, null)
                binding.fruitDescriptionCardView.setCardBackgroundColor(
                    backgroundColor,
                )

                binding.fruitDescriptionCardView.strokeColor =
                    backgroundColor.darkenColor()

                Glide.with(requireActivity())
                    .asGif()
                    .load(fruitResources.fallingImage)
                    .into(binding.fruitDetailBackgroundImageView)
            }

        FruitResources.entries.find { it.fruitEmotion == binding.fruit?.fruitEmotion }?.let {
            binding.fruitDescriptionCardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    it.color,
                ),
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FRUIT_DATA = "FRUIT_DATA"
        fun newInstance(fruit: FruitUiModel): FruitDetailDialog {
            return FruitDetailDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(FRUIT_DATA, fruit)
                }
            }
        }
    }
}
