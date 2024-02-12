package com.d101.presentation.main.fragments

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentMainBinding
import com.d101.presentation.main.event.TreeFragmentEvent
import com.d101.presentation.main.fragments.dialogs.BeforeFruitCreateBaseFragment
import com.d101.presentation.main.fragments.dialogs.FruitDialogInterface
import com.d101.presentation.main.fragments.dialogs.TodayFruitFragment
import com.d101.presentation.main.state.TreeFragmentViewState
import com.d101.presentation.main.state.TreeMessageState
import com.d101.presentation.main.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.CustomToast
import utils.repeatOnStarted

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainFragmentViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: DialogFragment

    private var messageTypingJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = viewModel

        binding.createFruitButton.setOnClickListener {
            viewModel.onButtonClick()
        }

        binding.mainTreeImagebutton.setOnClickListener {
            viewModel.onGetTreeMessage()
        }

        binding.mainTreeImagebutton.setOnLongClickListener {
            viewModel.onLongClickEmotionTrashMode()
            true
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.messageState.collect {
                when (it) {
                    is TreeMessageState.EnableMessage -> {
                        binding.mainTreeImagebutton.isEnabled = true
                    }

                    is TreeMessageState.NoAccessMessage -> {
                        binding.mainTreeImagebutton.isEnabled = false
                    }
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collect {
                when (it) {
                    is TreeFragmentViewState.EmotionTrashMode -> {
                        val fadeIn = ObjectAnimator.ofFloat(
                            binding.nightLottieView,
                            "alpha",
                            0f,
                            1f,
                        )
                        fadeIn.duration = 500
                        fadeIn.start()
                        binding.nightLottieView.visibility = View.VISIBLE
                        binding.nightLottieView.playAnimation()
                    }

                    else -> {
                        val fadeOut = ObjectAnimator.ofFloat(
                            binding.nightLottieView,
                            "alpha",
                            1f,
                            0f,
                        )
                        fadeOut.duration = 500
                        fadeOut.start()
                        binding.nightLottieView.pauseAnimation()
                    }
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect {
                when (it) {
                    is TreeFragmentEvent.MakeFruitEvent -> {
                        dialog = BeforeFruitCreateBaseFragment()
                        FruitDialogInterface.dialog = dialog
                        dialog.show(childFragmentManager, "")
                    }

                    is TreeFragmentEvent.CheckTodayFruitEvent -> {
                        dialog = TodayFruitFragment()
                        dialog.dialog?.window?.setBackgroundDrawable(
                            ColorDrawable(Color.TRANSPARENT),
                        )
                        dialog.show(childFragmentManager, "")
                    }

                    is TreeFragmentEvent.ShowErrorEvent -> {
                        showToast(it.message)
                    }

                    is TreeFragmentEvent.ChangeTreeMessage -> {
                        typingAnimation(it.message)
                    }

                    is TreeFragmentEvent.EmotionTrashEvent -> {
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        CustomToast.createAndShow(requireContext(), message)
    }

    private fun typingAnimation(message: String) {
        messageTypingJob?.cancel()
        val sb = StringBuilder()

        messageTypingJob = lifecycleScope.launch(Dispatchers.Default) {
            message.forEach {
                delay(50L)
                sb.append(it)
                withContext(Dispatchers.Main) {
                    binding.treeSpeechTextview.text = sb
                }
            }
            viewModel.enableMessage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        messageTypingJob?.cancel()
        messageTypingJob = null
        _binding = null
    }
}
