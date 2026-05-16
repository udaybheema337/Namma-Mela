package com.example.namma_mela.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.namma_mela.data.MelaRepository
import com.example.namma_mela.data.Seat

class MelaViewModel : ViewModel() {
    private val repository = MelaRepository()

    init {
        repository.initializeDatabase(40) // Setup 40 seats if DB is empty
    }

    val seats = repository.allSeats.asLiveData()
    val playDetails = repository.playDetails.asLiveData()
    val comments = repository.allComments.asLiveData()

    fun onSeatClicked(seat: Seat) = repository.toggleSeat(seat)
    fun postComment(author: String, text: String) = repository.addComment(author, text)
    fun updatePlay(title: String, duration: String, url: String) = repository.updatePlayDetails(title, duration, url)
}
