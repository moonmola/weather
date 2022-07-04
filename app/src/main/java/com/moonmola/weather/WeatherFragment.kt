package com.moonmola.weather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moonmola.weather.databinding.FragmentWeatherBinding
import com.moonmola.weather.di.GlideApp
import com.moonmola.weather.recycler.DayAdapter
import com.moonmola.weather.recycler.HourAdapter
import com.moonmola.weather.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherFragment : Fragment() {
    @Inject
    lateinit var hourAdapter: HourAdapter

    @Inject
    lateinit var dayAdapter: DayAdapter
    private lateinit var binding: FragmentWeatherBinding
    private val viewModel: WeatherViewModel by viewModels()

    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val PERMISSIONS_REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        with(binding.recyclerViewHour) {
            adapter = hourAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.HORIZONTAL))
        }
        with(binding.recyclerViewDay) {
            adapter = dayAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
        binding.refresh.setOnRefreshListener {
            getLocation()
            binding.refresh.isRefreshing = false
        }
        subscribeUi()
        viewModel.getLocalWeather()
        return binding.root
    }

    private fun subscribeUi() {
        viewModel.stateWeatherInfo.observe(viewLifecycleOwner) { weatherResponse ->
            if (weatherResponse != null && (System.currentTimeMillis() - weatherResponse.current.dt.toLong() * 1000) < 1000 * 60 * 60 * 24) {
                // 한번도 api 호출하지 않았거나 호출한지 24시간이 지났다면 api호출 아니라면 local데이터 가져오기
                GlideApp.with(this)
                    .load(BuildConfig.BACKGROUND_URL+weatherResponse.current.weather[0].icon+".png")
                    .into(binding.fragmentBackground)

                getAddress(weatherResponse.lat, weatherResponse.lon)
                binding.currentTemp.text = (weatherResponse.current.temp - 273.15).roundToInt().toString() + "°"
//                binding.currentTempDescription.text = weatherResponse.current.weather[0].description
                //            binding.currentTempMaxMin

                dayAdapter.min = 100
                dayAdapter.max = -100
                weatherResponse.daily.forEach {
                    if((it.temp.min -273.15).roundToInt() <dayAdapter.min) {
                        dayAdapter.min = (it.temp.min -273.15).roundToInt()
                    }
                    if((it.temp.max -273.15).roundToInt() >dayAdapter.max) {
                        dayAdapter.max = (it.temp.max -273.15).roundToInt()
                    }
                }
                dayAdapter.itemList = weatherResponse.daily
                dayAdapter.notifyDataSetChanged()

                hourAdapter.itemList = weatherResponse.hourly
                hourAdapter.notifyDataSetChanged()
            } else {
                getLocation()
            }
        }
    }

    private fun getAddress(lat: Double, lon: Double) {
        val mGeoCoder = Geocoder(requireContext().applicationContext, Locale.KOREAN)
        var mResultList: List<Address>? = null
        try {
            mResultList = mGeoCoder.getFromLocation(
                lat, lon, 1
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (mResultList != null) {
            Log.d("CheckCurrentLocation", mResultList[0].getAddressLine(0))
            if (mResultList[0].locality.isEmpty()) {
                binding.currentLocation.text = mResultList[0].adminArea
            } else {
                binding.currentLocation.text = mResultList[0].locality
            }

        }
    }

    private fun getLocation() {
        val userLocation: Location? = getLatLng()
        userLocation?.let {
            val latitude = it.latitude
            val longitude = it.longitude
            val mGeoCoder = Geocoder(requireContext().applicationContext, Locale.KOREAN)
            var mResultList: List<Address>? = null
            try {
                mResultList = mGeoCoder.getFromLocation(
                    latitude, longitude, 1
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (mResultList != null) {
                Log.d("CheckCurrentLocation", mResultList[0].getAddressLine(0))
                if (mResultList[0].locality.isEmpty()) {
                    binding.currentLocation.text = mResultList[0].adminArea
                } else {
                    binding.currentLocation.text = mResultList[0].locality
                }

            }

            viewModel.getRemoteWeatherData(latitude, longitude)
        }
    }

    private fun getLatLng(): Location? {
        var currentLatLng: Location?
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            currentLatLng = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    REQUIRED_PERMISSIONS[0]
                )
            ) {
                Toast.makeText(requireContext(), "앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT)
                    .show()
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
            currentLatLng = getLatLng()
        }
        return currentLatLng
    }


}