package com.d101.presentation.fruit

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.d101.domain.model.FruitResources
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentFruitDetailBinding
import com.d101.presentation.tree.TreeViewModel
import utils.darkenColor

class FruitDetailDialog : DialogFragment() {

    private val viewModel: TreeViewModel by viewModels({ requireParentFragment() })

    private var _binding: FragmentFruitDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_fruit_detail, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        Glide.with(binding.root).load(viewModel.todayFruit.imageUrl).into(binding.fruitImageView)
        FruitResources.entries.find { it.fruitEmotion == viewModel.todayFruit.fruitEmotion }
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

        FruitResources.entries.find { it.fruitEmotion == viewModel.todayFruit.fruitEmotion }?.let {
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
}
