package com.example.namma_mela.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.namma_mela.R
import com.example.namma_mela.data.AppDatabase
import com.example.namma_mela.data.MelaRepository
import com.example.namma_mela.data.Seat
import com.example.namma_mela.databinding.FragmentBookingBinding
import com.example.namma_mela.ui.MelaViewModel
import com.example.namma_mela.ui.adapters.SeatAdapter

class BookingFragment : Fragment(R.layout.fragment_booking) {
    
    private val viewModel: MelaViewModel by activityViewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = MelaRepository(database.seatDao())
        MelaViewModel.Factory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookingBinding.bind(view)

        val adapter = SeatAdapter { seat -> 
            showBookingDialog(seat)
        }
        binding.recyclerViewSeats.layoutManager = GridLayoutManager(requireContext(), 5)
        binding.recyclerViewSeats.adapter = adapter

        // Sync Seats
        viewModel.seats.observe(viewLifecycleOwner) { seatList ->
            adapter.submitList(seatList)
            val bookedCount = seatList.count { it.isReserved }
            binding.btnBookInfo.text = "Booked: $bookedCount | Available: ${seatList.size - bookedCount}"
        }

        // Sync Play Info for Header
        viewModel.playDetails.observe(viewLifecycleOwner) { play ->
            binding.tvBookingPlayTitle.text = play.title
            binding.tvBookingPlayTime.text = "Duration: ${play.duration}"
        }
    }

    private fun showBookingDialog(seat: Seat) {
        if (seat.isReserved) {
            AlertDialog.Builder(requireContext())
                .setTitle("Ticket Details")
                .setMessage("Seat Number: #${seat.seatNumber}\nBooked by: ${seat.bookedBy}\nPrice: ₹${seat.price}")
                .setPositiveButton("Cancel Booking") { _, _ ->
                    viewModel.onSeatClicked(seat)
                    Toast.makeText(context, "Booking for Seat #${seat.seatNumber} cancelled", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Close", null)
                .show()
        } else {
            val input = EditText(requireContext())
            input.hint = "Enter Passenger Name"
            
            val container = LinearLayout(requireContext())
            container.orientation = LinearLayout.VERTICAL
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(60, 20, 60, 0)
            container.addView(input, params)

            AlertDialog.Builder(requireContext())
                .setTitle("Book Seat #${seat.seatNumber}")
                .setMessage("Please enter name to confirm booking.\nTicket Price: ₹${seat.price}")
                .setView(container)
                .setPositiveButton("Confirm Ticket") { _, _ ->
                    val name = input.text.toString().trim()
                    if (name.isNotEmpty()) {
                        viewModel.onSeatClicked(seat, name)
                        Toast.makeText(context, "Ticket booked successfully for $name!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
