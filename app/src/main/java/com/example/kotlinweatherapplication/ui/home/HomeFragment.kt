package com.example.kotlinweatherapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.kotlinweatherapplication.R
import com.example.kotlinweatherapplication.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.weatherResponse.observe(viewLifecycleOwner){ response ->
            binding.textCityName.text = "Moscow"
            val sd = SimpleDateFormat("dd-MMM")
            val date: String = sd.format(response.current.dt * 1000)
            binding.textDateDisplay.text = date
            binding.textDegrees.text = response.current.temp.toInt().toString()+"°C"
            binding.textWeatherDescription.text = response.current.weather[0].description
            Glide.with(this@HomeFragment)
                .load("https://openweathermap.org/img/wn/" + response.current.weather[0].icon + "@2x.png")
                .into(binding.weatherIcon)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}