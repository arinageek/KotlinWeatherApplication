package com.example.kotlinweatherapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.kotlinweatherapplication.R
import com.example.kotlinweatherapplication.Utils.Formatting
import com.example.kotlinweatherapplication.Utils.Formatting.getDate
import com.example.kotlinweatherapplication.Utils.Formatting.getTemp
import com.example.kotlinweatherapplication.databinding.FragmentHomeBinding
import com.example.kotlinweatherapplication.networking.openweathermap.forecast_models.Hourly
import com.example.kotlinweatherapplication.networking.vk.cities_models.Item
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.cities_autocomplete.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), CitiesOnItemClickListener {

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

        val forecastAdapter = ForecastAdapter()
        binding.recyclerView.adapter = forecastAdapter

        val citiesAdapter = CitiesAdapter(this)
        binding.toolbar.citiesRecyclerView.adapter = citiesAdapter

        var job: Job? = null
        binding.toolbar.tvEnterCity.addTextChangedListener { editable ->
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
        binding.toolbar.buttonClose.setOnClickListener {
            hideAutocomplete()
            binding.toolbar.apply {
                tvEnterCity.setText("")
                tvEnterCity.clearFocus()
            }
        }

        binding.btnSaveCity.setOnClickListener {
            viewModel.insertCity()
        }

        viewModel.isAlreadySaved.observe(viewLifecycleOwner) {
            if (it) binding.btnSaveCity.setImageResource(R.drawable.ic_filled_star)
            else binding.btnSaveCity.setImageResource(R.drawable.ic_baseline_star_border_24)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is HomeViewModel.Event.showNoInternetConnectionMessage -> {
                        binding.tvNoConnection.visibility = View.VISIBLE
                        binding.cardView.visibility = View.GONE
                    }
                    is HomeViewModel.Event.removeNoInternetConnectionMessage -> {
                        binding.tvNoConnection.visibility = View.GONE
                        binding.cardView.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.citiesResponse.observe(viewLifecycleOwner) { response ->
            citiesAdapter.submitList(response.response.items)
            showAutocomplete()
        }

        viewModel.weatherResponse.observe(viewLifecycleOwner) { response ->
            binding.apply {
                textCityName.text = viewModel.currentCity
                textDateDisplay.text = getDate(response.current.dt)
                textDegrees.text = getTemp(response.current.temp)
                textWeatherDescription.text = response.current.weather[0].description
                Glide.with(this@HomeFragment)
                    .load("https://openweathermap.org/img/wn/" + response.current.weather[0].icon + "@2x.png")
                    .into(weatherIcon)
                forecastAdapter.submitList(response.daily)
                if (!response.hourly.isNullOrEmpty()) {
                    val chartModel: AAChartModel = createChart(response.hourly)
                    aaChartView.aa_drawChartWithChartModel(chartModel)
                }
            }
        }

        return view
    }

    private fun showAutocomplete() {
        binding.toolbar.citiesRecyclerView.visibility = View.VISIBLE
    }

    private fun hideAutocomplete() {
        binding.toolbar.citiesRecyclerView.visibility = View.GONE
    }

    private fun createChart(hourly: List<Hourly>): AAChartModel {
        val list: ArrayList<Int> = ArrayList()
        for (item in hourly) {
            list.add(item.temp.toInt())
        }
        Log.d("HomeViewModel", list.toString())
        return AAChartModel()
            .chartType(AAChartType.Spline)
            .title("Weather by hours")
            .backgroundColor("#9a7ffa")
            .dataLabelsEnabled(false)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Temperature")
                        .data(list.toTypedArray())
                )
            )
            .categories(hourly.map { h ->
                Formatting.getFullDate(h.dt)
            }.toTypedArray())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(city: Item) {
        hideAutocomplete()
        binding.toolbar.apply {
            tvEnterCity.setText("")
            tvEnterCity.clearFocus()
        }
        viewModel.getCityCoordinates(city.title)
    }

}