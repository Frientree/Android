package com.d101.presentation.main.fragments.dialogs

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentEmotionDumpingBinding

class EmotionDumpingFragment : DialogFragment() {

    private var _binding: FragmentEmotionDumpingBinding? = null
    private val binding get() = _binding!!

    private val animatorBigger = ValueAnimator.ofFloat(1f, 1.5f)
    private val animatorSmaller = ValueAnimator.ofFloat(1.5f, 0f)

    val animatorSet = AnimatorSet()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_emotion_dumping,
            container,
            false,
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        animatorBigger.duration = 300
        animatorSmaller.duration = 700
        animatorBigger.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            binding.blackHoleLottieView.scaleX = value
            binding.blackHoleLottieView.scaleY = value
        }
        animatorSmaller.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            binding.blackHoleLottieView.scaleX = value
            binding.blackHoleLottieView.scaleY = value
        }

        animatorSet.play(animatorSmaller).after(animatorBigger)

        val fadeOutTextView = ObjectAnimator.ofFloat(
            binding.blackHoleTextView,
            "alpha",
            1f,
            0f,
        )
        val fadeOutButton = ObjectAnimator.ofFloat(
            binding.blackHoleButton,
            "alpha",
            1f,
            0f,
        )
        fadeOutButton.duration = 500
        fadeOutTextView.duration = 500

        binding.blackHoleButton.setOnClickListener {
            animatorSet.start()
            fadeOutButton.start()
            fadeOutTextView.start()
        }
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.boomLottieView.visibility = View.VISIBLE
                binding.boomLottieView.playAnimation()
            }
        })

        binding.boomLottieView.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                dialog?.dismiss()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.boomLottieView.removeAllAnimatorListeners()
        animatorSet.removeAllListeners()
        animatorBigger.removeAllUpdateListeners()
        animatorSmaller.removeAllUpdateListeners()
        _binding = null
    }
}
