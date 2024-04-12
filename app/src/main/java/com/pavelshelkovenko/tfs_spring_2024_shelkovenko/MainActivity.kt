package com.pavelshelkovenko.tfs_spring_2024_shelkovenko

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw RuntimeException("ActivityMainBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.nav_host_fragment_container)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            when(nd.id) {
                R.id.profileFragment -> bottomNavigationView.isVisible = true
                R.id.channelFragment -> bottomNavigationView.isVisible = true
                R.id.peopleFragment -> bottomNavigationView.isVisible = true
                else -> bottomNavigationView.visibility = View.GONE
            }
        }
    }

}

