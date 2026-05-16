package com.example.namma_mela.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.namma_mela.R
import com.example.namma_mela.data.AppDatabase
import com.example.namma_mela.data.MelaRepository
import com.example.namma_mela.databinding.ActivityMainBinding
import com.example.namma_mela.ui.fragments.BookingFragment
import com.example.namma_mela.ui.fragments.FanWallFragment
import com.example.namma_mela.ui.fragments.HomeFragment
import com.example.namma_mela.ui.fragments.ManagerFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MelaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Repository and ViewModel with Factory
        val database = AppDatabase.getDatabase(this)
        val repository = MelaRepository(database.seatDao())
        val factory = MelaViewModel.Factory(repository)
        viewModel = ViewModelProvider(this, factory)[MelaViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_booking -> loadFragment(BookingFragment())
                R.id.nav_fanwall -> loadFragment(FanWallFragment())
                R.id.nav_manager -> loadFragment(ManagerFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}