package com.d101.presentation.main.fragments.dialogs

import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentFruitCreationBySpeechBinding
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FruitCreationBySpeechFragment : Fragment() {

    private var _binding: FragmentFruitCreationBySpeechBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FruitCreateViewModel by viewModels({ requireParentFragment() })
    private lateinit var audioFile: File

    private var recorder: MediaRecorder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_fruit_creation_by_speech,
            container,
            false,
        )

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startRecording()

        // 버튼을 누르거나 30초가 끝나면 그만
        binding.createFruitBySpeechButton.setOnClickListener {
            stopRecording()
            viewModel.setAudioFile(audioFile)
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.before_fragment_container_view,
                    FruitCreationLoadingFragment.newInstance(FruitCreationLoadingFragment.SPEECH),
                )
                .addToBackStack(null)
                .commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startRecording() {
        audioFile = createAudioFile()
        recorder = MediaRecorder(requireContext()).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(audioFile.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                // 에러 처리
            }
            start()
        }
    }

    private fun createAudioFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val audioFileName = "AUDIO_$timeStamp"
        val cacheDir: File = requireContext().cacheDir
        return File.createTempFile(audioFileName, ".m4a", cacheDir)
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
            // 서버로 전송.
        }
        recorder = null
    }

    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
    }
}
