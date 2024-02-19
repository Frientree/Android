package com.d101.presentation.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.d101.presentation.databinding.FragmentTutorialTreeMessageBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TutorialTreeMessageFragment : Fragment() {
    private var _binding: FragmentTutorialTreeMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTutorialTreeMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeat(Int.MAX_VALUE) {
                delay(300)
                binding.tapLottieAnimationView.playAnimation()
                delay(binding.tapLottieAnimationView.duration)
                binding.chatLottieAnimationView.playAnimation()
                delay(binding.chatLottieAnimationView.duration)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
