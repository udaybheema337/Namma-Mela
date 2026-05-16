package com.example.namma_mela.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.namma_mela.data.MelaRepository
import com.example.namma_mela.data.PlayDetails
import com.example.namma_mela.data.Seat
import kotlinx.coroutines.launch

class MelaViewModel(private val repository: MelaRepository) : ViewModel() {

    init {
        // Initialize 40 seats for the rural theater if they don't exist
        repository.initializeSeats(40)
    }

    val seats = repository.allSeats.asLiveData()
    val playDetails = repository.playDetails.asLiveData()
    val comments = repository.allComments.asLiveData()

    fun onSeatClicked(seat: Seat, userName: String = "") {
        viewModelScope.launch {
            repository.toggleSeat(seat, userName)
        }
    }

    fun postComment(author: String, text: String) {
        repository.addComment(author, text)
    }

    fun updatePlay(details: PlayDetails) {
        repository.updatePlayDetails(details)
    }

    class Factory(private val repository: MelaRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MelaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MelaViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
