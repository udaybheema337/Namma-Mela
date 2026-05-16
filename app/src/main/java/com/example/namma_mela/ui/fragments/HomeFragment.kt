package com.example.namma_mela.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.namma_mela.R
import com.example.namma_mela.data.AppDatabase
import com.example.namma_mela.data.MelaRepository
import com.example.namma_mela.databinding.FragmentHomeBinding
import com.example.namma_mela.ui.MelaViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    
    private val viewModel: MelaViewModel by activityViewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = MelaRepository(database.seatDao())
        MelaViewModel.Factory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)

        viewModel.playDetails.observe(viewLifecycleOwner) { play ->
            binding.tvPlayTitle.text = play.title
            binding.tvDuration.text = "Duration: ${play.duration}"
            
            // Main Poster
            if (play.posterUrl.isNotEmpty()) {
                Glide.with(this)
                    .load(play.posterUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .transition(withCrossFade())
                    .error(android.R.drawable.stat_notify_error)
                    .into(binding.ivPoster)
            }

            // Lead Actor
            binding.tvLeadActorName.text = play.leadActor
            Glide.with(this).load(play.leadActorPhoto)
                .placeholder(R.drawable.ic_launcher_background)
                .transition(withCrossFade())
                .error(R.drawable.ic_launcher_background)
                .into(binding.ivLeadActor)

            // Comedian
            binding.tvComedianName.text = play.comedian
            Glide.with(this).load(play.comedianPhoto)
                .placeholder(R.drawable.ic_launcher_background)
                .transition(withCrossFade())
                .error(R.drawable.ic_launcher_background)
                .into(binding.ivComedian)

            // Singer
            binding.tvSingerName.text = play.singer
            Glide.with(this).load(play.singerPhoto)
                .placeholder(R.drawable.ic_launcher_background)
                .transition(withCrossFade())
                .error(R.drawable.ic_launcher_background)
                .into(binding.ivSinger)
        }
    }
}
