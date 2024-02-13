package com.d101.presentation.main.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentTutorialEmotionalDumpingBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TutorialEmotionalDumpingFragment : Fragment() {
    private var _binding: FragmentTutorialEmotionalDumpingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTutorialEmotionalDumpingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeat(Int.MAX_VALUE) {
                delay(300)
                binding.dialogBackgroundConstraintLayout.background = null
                binding.titleTextView.setTextColor(resources.getColor(R.color.black, null))
                binding.tapLottieAnimationView.playAnimation()
                delay(binding.tapLottieAnimationView.duration - 300)
                binding.dialogBackgroundConstraintLayout.background =
                    ContextCompat.getDrawable(requireActivity(), R.drawable.bg_night)
                binding.titleTextView.setTextColor(resources.getColor(R.color.white, null))
                binding.starsLottieAnimationView.playAnimation()
                delay(1500)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
