package com.example.kotlinweatherapplication.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kotlinweatherapplication.R
import com.example.kotlinweatherapplication.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val viewModel by viewModels<SettingsViewModel>()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        var job: Job? = null
        binding.etCities.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(1500)
                editable?.let {
                    if (editable.toString().trim().isNotEmpty()) viewModel.getCities(editable.toString())
                }
            }
        }

        viewModel.citiesResponse.observe(viewLifecycleOwner){ list ->
            val cityAutoCompleteAdapter = ArrayAdapter<String>(inflater.context, android.R.layout.simple_dropdown_item_1line)
            cityAutoCompleteAdapter.addAll(list)
            binding.etCities.setAdapter(cityAutoCompleteAdapter)
        }

        viewModel.countriesResponse.observe(viewLifecycleOwner){ list ->
            val countrySpinnerAdapter = ArrayAdapter<String>(inflater.context, R.layout.support_simple_spinner_dropdown_item)
            countrySpinnerAdapter.addAll(list)
            binding.spCountries.adapter = countrySpinnerAdapter
        }

        binding.spCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.saveCountry(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        viewModel.countrySelected.observe(viewLifecycleOwner){
            if(it) {
                binding.etCities.isEnabled = true
                binding.etCities.background = resources.getDrawable(R.color.light_purple)
            }else{
                binding.etCities.isEnabled = false
                binding.etCities.background = resources.getDrawable(R.color.very_light_blue)
            }
        }

        return view
    }
}