package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.SettingInjectorService
import android.media.audiofx.Equalizer
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        tvLatitude = findViewById(R.id.tv_latitude)
        tvLongitude = findViewById(R.id.tv_longitude)

        getCurrentLocation()






        val intent = Intent(this@MainActivity, MapsActivity::class.java)
        startActivity(intent)


    }

    private fun getCurrentLocation()
    {
        if(checkPermissions())

        {
            if(isLocationEnebled())
            {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task->
                    val location: Location?=task.result
                    if (location==null)
                    {
                        Toast.makeText(this,"Null Recived", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(this, "Get Success", Toast.LENGTH_SHORT).show()
                        tvLatitude.text=""+location.latitude
                        tvLongitude.text=""+location.longitude
                    }
                }

            }
            else
            {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent=Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)

            }

        }
        else
        {
            requestPermission()

        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION) ,
        PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object
    {
        private const val   PERMISSION_REQUEST_ACCESS_LOCATION=100
    }
    private fun isLocationEnebled():Boolean{
        val locationManager:LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean
    {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }

        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION)
        {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }
            else {
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}