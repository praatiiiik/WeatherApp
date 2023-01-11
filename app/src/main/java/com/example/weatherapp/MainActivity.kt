package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.local.LocalWeatherData
import com.example.weatherapp.prefHelper.PrefConstants
import com.example.weatherapp.prefHelper.PreferenceHelper
import com.example.weatherapp.utility.Constants
import com.example.weatherapp.utility.Status
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val vm : WeatherViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if(checkGPSStatus()){
                showToast("GPS Enable")
            }else{
                showToast("Denied")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(!checkGPSStatus()){
            showToast("Enable GPS for location")
            binding.gpsCV.visibility = View.VISIBLE
        }
        setUpObservers()
        getData()
        setUpViews()
    }

    private fun setUpObservers(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.weather.collectLatest {status->
                    when(status) {
                        is Status.Success -> setDataOnView(status.data)
                        is Status.Loading -> showToast("Loading")
                        is Status.Error -> showToast("Error")
                        else -> showToast("Something went wrong")
                    }
                }
            }
        }
    }

    private fun setUpViews(){
        binding.gpsCV.setOnClickListener {
            requestEnableGPS()
        }
        refreshBut.setOnClickListener {
            getData()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataOnView(data: List<LocalWeatherData>?) {
        if(data!=null && data.isNotEmpty()){
            binding.locTV.text = data[0].weatherResponse?.name ?: "No Data"
            binding.weatherTypeTV.text = data[0].weatherResponse?.weather?.get(0)?.description ?: ""
            binding.tempTV.text = data[0].weatherResponse?.main?.temp.toString()+"F"
            binding.humidityTV.text = "Humidity ${data[0].weatherResponse?.main?.humidity}"
            binding.dateTV.text = epochToIST(data[0].weatherResponse?.dt)

            binding.mumbaiBut.text = "${data[1].weatherResponse?.name} ${data[1].weatherResponse?.main?.temp}F"
            binding.delhiBut.text = "${data[2].weatherResponse?.name} ${data[2].weatherResponse?.main?.temp}F"
            binding.sydneyBut.text = "${data[3].weatherResponse?.name} ${data[3].weatherResponse?.main?.temp}F"
            binding.melbourneBut.text = "${data[4].weatherResponse?.name} ${data[4].weatherResponse?.main?.temp}F"
            binding.newYorkBut.text = "${data[5].weatherResponse?.name} ${data[5].weatherResponse?.main?.temp}F"
            binding.singaporeBut.text = "${data[6].weatherResponse?.name} ${data[6].weatherResponse?.main?.temp}F"
        }
    }


    private fun isLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf( Manifest.permission.ACCESS_FINE_LOCATION),
            1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1->{
                if(isLocationPermission()){
                    getData()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getData(){
        if(isLocationPermission()){
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if(it!=null){
                    vm.getWeatherDataFromServer(it.latitude,it.longitude)
                    storeLatAndLon(it.latitude,it.longitude)
                }else if(checkGPSStatus()){
                    requestLocation(fusedLocationClient)
                }else {
                    val location = getLatAndLon()
                   if(location!=null){
                       vm.getWeatherDataFromServer(location.lat,location.lon)
                   }else{
                       showToast("Enable GPS to get your current location")
                   }
                }
            }
        }else{
            requestLocationPermission()
            vm.getWeatherDataFromServer(null,null)
        }
    }

    private fun requestLocation(fusedLocationClient: FusedLocationProviderClient) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if(locationResult.lastLocation==null)return
                vm.getWeatherDataFromServer(locationResult.lastLocation!!.latitude,locationResult.lastLocation!!.longitude)
                storeLatAndLon(locationResult.lastLocation!!.latitude,locationResult.lastLocation!!.longitude)
            }
        }
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(1000)
            .setMaxUpdateDelayMillis(500)
            .build()

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

    }

    private fun storeLatAndLon(lat:Double,lon:Double){
        PreferenceHelper.getInstance(this).setString(PrefConstants.CURRENT_LOCATION_LAT,lat.toString())
        PreferenceHelper.getInstance(this).setString(PrefConstants.CURRENT_LOCATION_LON,lon.toString())
        PreferenceHelper.getInstance(this).setBoolean(PrefConstants.LAT_LON_SAVED,true)
        binding.gpsCV.visibility = View.INVISIBLE
    }

    private fun isSavedLastLoc() = PreferenceHelper.getInstance(this).getBoolean(PrefConstants.LAT_LON_SAVED)

    private fun getLatAndLon():Location?{
        val lat = PreferenceHelper.getInstance(this).getString(PrefConstants.CURRENT_LOCATION_LAT)
        val lon = PreferenceHelper.getInstance(this).getString(PrefConstants.CURRENT_LOCATION_LON)
        if(lat!="" || lon!="")return Location(lat.toDouble(), lon.toDouble())
        return null
    }

    data class Location(val lat:Double, val lon:Double)

    private fun checkGPSStatus():Boolean{
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun requestEnableGPS(){
        resultLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    @SuppressLint("SimpleDateFormat")
    private fun epochToIST(epoch: Int?): String {
        if(epoch==null)return ""
        val date = Date(epoch * 1000L)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(date)
    }

    private fun showLog(msg:String){
        Log.d("msg",msg)
    }

    private fun showToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }
}