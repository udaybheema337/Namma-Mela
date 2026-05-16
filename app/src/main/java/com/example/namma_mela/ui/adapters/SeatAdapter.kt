package com.example.namma_mela.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.namma_mela.data.Seat
import com.example.namma_mela.databinding.ItemSeatBinding

class SeatAdapter(private val onSeatClick: (Seat) -> Unit) :
    ListAdapter<Seat, SeatAdapter.SeatViewHolder>(SeatDiffCallback()) {

    inner class SeatViewHolder(private val binding: ItemSeatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seat: Seat) {
            // Show row label (e.g., A1) or seat number if label is missing
            binding.tvSeatNumber.text = if (seat.rowLabel.isNotEmpty()) seat.rowLabel else seat.seatNumber.toString()
            
            if (seat.isReserved) {
                // Reserved seat color (Red)
                binding.cardSeat.setCardBackgroundColor(Color.parseColor("#B00020"))
                binding.tvSeatNumber.setTextColor(Color.WHITE)
            } else {
                // Available seat color (Yellow/Gold)
                binding.cardSeat.setCardBackgroundColor(Color.parseColor("#FFD54F"))
                binding.tvSeatNumber.setTextColor(Color.BLACK)
            }
            binding.root.setOnClickListener { onSeatClick(seat) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val binding = ItemSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) = holder.bind(getItem(position))
}

class SeatDiffCallback : DiffUtil.ItemCallback<Seat>() {
    override fun areItemsTheSame(oldItem: Seat, newItem: Seat) = oldItem.seatNumber == newItem.seatNumber
    override fun areContentsTheSame(oldItem: Seat, newItem: Seat) = oldItem == newItem
}
