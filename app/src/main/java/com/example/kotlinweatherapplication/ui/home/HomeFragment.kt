package com.example.kotlinweatherapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.kotlinweatherapplication.R
import com.example.kotlinweatherapplication.Utils.Formatting
import com.example.kotlinweatherapplication.Utils.Formatting.getDate
import com.example.kotlinweatherapplication.Utils.Formatting.getTemp
import com.example.kotlinweatherapplication.databinding.FragmentHomeBinding
import com.example.kotlinweatherapplication.openweathermap.forecast_models.Hourly
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


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

        val adapter = ForecastAdapter()
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when(event){
                    is HomeViewModel.WeatherEvent.showNoInternetConnectionMessage ->  {
                        binding.tvNoConnection.visibility = View.VISIBLE
                        binding.cardView.visibility = View.GONE
                    }
                    is HomeViewModel.WeatherEvent.removeNoInternetConnectionMessage -> {
                        binding.tvNoConnection.visibility = View.GONE
                        binding.cardView.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.weatherResponse.observe(viewLifecycleOwner){ response ->
            binding.apply {
                textCityName.text = "Moscow"
                textDateDisplay.text = getDate(response.current.dt)
                textDegrees.text = getTemp(response.current.temp)
                textWeatherDescription.text = response.current.weather[0].description
                Glide.with(this@HomeFragment)
                    .load("https://openweathermap.org/img/wn/" + response.current.weather[0].icon + "@2x.png")
                    .into(weatherIcon)
                adapter.submitList(response.daily)
                if(!response.hourly.isNullOrEmpty()) {
                    val chartModel: AAChartModel  = createChart(response.hourly)
                    aaChartView.aa_drawChartWithChartModel(chartModel)
                }
            }
        }

        return view
    }

    fun createChart(hourly: List<Hourly>) : AAChartModel {
        val list: ArrayList<Int> = ArrayList()
        for(item in hourly){
            list.add(item.temp.toInt())
        }
        Log.d("HomeViewModel", list.toString())
        return AAChartModel()
            .chartType(AAChartType.Spline)
            .title("Weather by hours")
            .backgroundColor("#9a7ffa")
            .dataLabelsEnabled(false)
            .series(arrayOf(
                AASeriesElement()
                    .name("Temperature")
                    .data(list.toTypedArray()))
            )
            .categories(hourly.map { h ->
                Formatting.getFullDate(h.dt)
            }.toTypedArray())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}