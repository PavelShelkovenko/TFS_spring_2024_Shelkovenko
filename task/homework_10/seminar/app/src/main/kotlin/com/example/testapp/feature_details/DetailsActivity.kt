package com.example.testapp.feature_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.testapp.R
import com.example.testapp.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity(R.layout.activity_details) {

    private val binding: ActivityDetailsBinding by viewBinding(ActivityDetailsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.details.text = intent.getStringExtra(EXTRA_DETAILS_TITLE)
    }

    companion object {

        private const val EXTRA_DETAILS_TITLE = "extra_details_id"

        fun createIntent(context: Context, title: String): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_DETAILS_TITLE, title)
            }
        }
    }
}
