package com.example.kotlinweatherapplication.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kotlinweatherapplication.R
import com.example.kotlinweatherapplication.databinding.FragmentSavedLocationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SavedLocationsFragment : Fragment(R.layout.fragment_saved_locations) {

    private val viewModel by viewModels<SavedLocationsViewModel>()

    private var _binding: FragmentSavedLocationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedLocationsBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapter = SavedCitiesAdapter()
        binding.rvSavedCities.adapter = adapter

        viewModel.weatherResponse.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            adapter.notifyDataSetChanged()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is SavedLocationsViewModel.SavedLocationsEvent.showNoInternetConnectionMessage -> {
                        binding.tvNoConnection.visibility = View.VISIBLE
                        binding.rvSavedCities.visibility = View.GONE
                    }
                    is SavedLocationsViewModel.SavedLocationsEvent.removeNoInternetConnectionMessage -> {
                        binding.tvNoConnection.visibility = View.GONE
                        binding.rvSavedCities.visibility = View.VISIBLE
                    }
                }
            }
        }

        return view
    }
}