package com.example.namma_mela.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.namma_mela.R
import com.example.namma_mela.databinding.FragmentManagerBinding

class ManagerFragment : Fragment(R.layout.fragment_manager) {
    private val viewModel: MelaViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentManagerBinding.bind(view)

        binding.btnUpdatePlay.setOnClickListener {
            val title = binding.etPlayTitle.text.toString()
            val duration = binding.etDuration.text.toString()
            val url = binding.etPosterUrl.text.toString()

            if (title.isNotEmpty()) {
                viewModel.updatePlay(title, duration, url)
                Toast.makeText(context, "Play Updated Successfully!", Toast.LENGTH_SHORT).show()
                binding.etPlayTitle.text?.clear()
                binding.etDuration.text?.clear()
                binding.etPosterUrl.text?.clear()
            }
        }
    }
}