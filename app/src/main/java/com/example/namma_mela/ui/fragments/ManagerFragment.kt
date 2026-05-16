package com.example.namma_mela.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.namma_mela.R
import com.example.namma_mela.data.AppDatabase
import com.example.namma_mela.data.MelaRepository
import com.example.namma_mela.data.PlayDetails
import com.example.namma_mela.databinding.FragmentManagerBinding
import com.example.namma_mela.ui.MelaViewModel

class ManagerFragment : Fragment(R.layout.fragment_manager) {
    
    private val viewModel: MelaViewModel by activityViewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = MelaRepository(database.seatDao())
        MelaViewModel.Factory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentManagerBinding.bind(view)

        showAdminLock(binding)

        // Live Booking Summary for Manager
        viewModel.seats.observe(viewLifecycleOwner) { seats ->
            val booked = seats.count { it.isReserved }
            val revenue = seats.filter { it.isReserved }.sumOf { it.price }
            // We'll add these views to the XML if they don't exist, 
            // but for now we can update the title or show a toast
            binding.btnUpdatePlay.text = "PUBLISH UPDATES (Booked: $booked | ₹$revenue)"
        }

        viewModel.playDetails.observe(viewLifecycleOwner) { play ->
            if (binding.etPlayTitle.text.isEmpty()) {
                binding.etPlayTitle.setText(play.title)
                binding.etDuration.setText(play.duration)
                binding.etPosterUrl.setText(play.posterUrl)
                binding.etLeadActor.setText(play.leadActor)
                binding.etLeadActorPhoto.setText(play.leadActorPhoto)
                binding.etComedian.setText(play.comedian)
                binding.etComedianPhoto.setText(play.comedianPhoto)
                binding.etSinger.setText(play.singer)
                binding.etSingerPhoto.setText(play.singerPhoto)
            }
        }

        binding.btnUpdatePlay.setOnClickListener {
            updatePlayDetails(binding)
        }
        
        // Add a long press to the update button to reset seats (hidden feature)
        binding.btnUpdatePlay.setOnLongClickListener {
            showResetDialog()
            true
        }
    }

    private fun updatePlayDetails(binding: FragmentManagerBinding) {
        val details = PlayDetails(
            title = binding.etPlayTitle.text.toString(),
            duration = binding.etDuration.text.toString(),
            posterUrl = binding.etPosterUrl.text.toString(),
            leadActor = binding.etLeadActor.text.toString(),
            leadActorPhoto = binding.etLeadActorPhoto.text.toString(),
            comedian = binding.etComedian.text.toString(),
            comedianPhoto = binding.etComedianPhoto.text.toString(),
            singer = binding.etSinger.text.toString(),
            singerPhoto = binding.etSingerPhoto.text.toString()
        )
        if (details.title.isNotEmpty()) {
            viewModel.updatePlay(details)
            Toast.makeText(context, "Mela Details Published!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showResetDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Reset All Bookings?")
            .setMessage("This will clear all ticket bookings for the current show. This cannot be undone.")
            .setPositiveButton("Reset Now") { _, _ ->
                // Custom function to be added to ViewModel
                Toast.makeText(context, "Feature: Use long-press on seats in Admin mode to clear.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAdminLock(binding: FragmentManagerBinding) {
        binding.root.visibility = View.GONE
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        input.hint = "Enter Admin PIN"

        AlertDialog.Builder(requireContext())
            .setTitle("Admin Access")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("Unlock") { _, _ ->
                if (input.text.toString() == "1234") binding.root.visibility = View.VISIBLE
                else parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()
            }
            .setNegativeButton("Cancel") { _, _ ->
                parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()
            }
            .show()
    }
}
