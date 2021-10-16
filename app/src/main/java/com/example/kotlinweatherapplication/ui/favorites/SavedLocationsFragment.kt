package com.example.kotlinweatherapplication.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kotlinweatherapplication.R
import com.example.kotlinweatherapplication.databinding.FragmentSavedLocationsBinding
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.weatherResponse.observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
            adapter.notifyDataSetChanged()
        }

        return view
    }
}