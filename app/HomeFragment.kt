package com.example.namma_mela.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.namma_mela.R
import com.example.namma_mela.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: MelaViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)

        viewModel.playDetails.observe(viewLifecycleOwner) { play ->
            binding.tvPlayTitle.text = play.title
            binding.tvDuration.text = "Duration: ${play.duration}"

            if (play.posterUrl.isNotEmpty()) {
                Glide.with(this).load(play.posterUrl).into(binding.ivPoster)
            }
        }
    }
}