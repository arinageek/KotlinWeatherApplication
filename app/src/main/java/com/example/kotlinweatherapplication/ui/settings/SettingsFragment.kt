package com.example.kotlinweatherapplication.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kotlinweatherapplication.R
import com.example.kotlinweatherapplication.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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

        val cityAutoCompleteAdapter =
            ArrayAdapter<String>(inflater.context, android.R.layout.simple_dropdown_item_1line)
        binding.etCities.setAdapter(cityAutoCompleteAdapter)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is SettingsViewModel.SettingsEvent.ShowIncorrectDataNotification -> {
                        Snackbar.make(view, "Incorrect location", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.saveDataToDb()
        }

        viewModel.savedCountry.observe(viewLifecycleOwner) { country ->
            val names = resources.getStringArray(R.array.countryNames)
            binding.spCountries.setSelection(names.indexOf(country))
        }
        viewModel.savedCity.observe(viewLifecycleOwner) { city ->
            binding.etCities.setText(city)
        }

        var job: Job? = null
        binding.etCities.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(1500)
                editable?.let {
                    if (editable.toString().trim()
                            .isNotEmpty()
                    ) viewModel.getCities(editable.toString())
                }
            }
        }

        viewModel.citiesResponse.observe(viewLifecycleOwner) { list ->
            cityAutoCompleteAdapter.addAll(list)
        }

        binding.spCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val ids = resources.getStringArray(R.array.countryIds)
                viewModel.saveCountry(
                    adapterView?.getItemAtPosition(position).toString(),
                    ids[position].toInt()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.etCities.setOnItemClickListener { parent, view, position, id ->
            viewModel.saveCity(parent?.getItemAtPosition(position).toString())
        }

        return view
    }
}