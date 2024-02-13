package com.d101.presentation.main.fragments.dialogs

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentEmotionDumpingBinding
import com.d101.presentation.main.MainActivity
import utils.repeatOnStarted
import java.util.Timer
import kotlin.concurrent.timer

class EmotionDumpingFragment : DialogFragment() {

    private var _binding: FragmentEmotionDumpingBinding? = null
    private val binding get() = _binding!!

    private val animatorBigger = ValueAnimator.ofFloat(1f, 1.5f)
    private val animatorSmaller = ValueAnimator.ofFloat(1.5f, 0f)

    private val animatorSet = AnimatorSet()

    private lateinit var activity: MainActivity

    private val soundPoolBuilder = SoundPool.Builder()
    private val soundPool = soundPoolBuilder.build()

    private var timerTask = Timer()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

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

        binding.lifecycleOwner = viewLifecycleOwner

        val soundId = soundPool.load(activity, R.raw.sound_explosion, 0)

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
                startTimer()
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 2f)
            }
        })
    }

    private fun startTimer() {
        viewLifecycleOwner.repeatOnStarted {
            var time = 0
            timerTask = timer(period = 10) {
                time++
                val sec = time / 100
                if (sec >= 3) {
                    dialog?.dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerTask.cancel()
        soundPool.release()
        binding.boomLottieView.removeAllAnimatorListeners()
        animatorSet.removeAllListeners()
        animatorBigger.removeAllUpdateListeners()
        animatorSmaller.removeAllUpdateListeners()
        _binding = null
    }
}
