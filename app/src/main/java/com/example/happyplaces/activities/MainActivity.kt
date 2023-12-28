package com.example.happyplaces.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyplaces.database.DatabaseHandler
import com.example.happyplaces.databinding.ActivityMainBinding
import com.example.happyplaces.model.HappyPlaceModel
import com.example.happyplaces.adaptor.HappyPlacesAdapter

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabAddPlace?.setOnClickListener{
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }
       // getHappyPlaceListFromLocaldb()
    }

    private fun getHappyPlaceListFromLocaldb(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlacesList = dbHandler.getHappyPlacesList()
        if (getHappyPlacesList.size > 0) {
            binding?.rvHappy?.visibility = View.VISIBLE
            binding?.tvNoRecords?.visibility = View.GONE
            setupHappyPlacesRecycleView(getHappyPlacesList)
        }
        else{
            binding?.rvHappy?.visibility = View.GONE
            binding?.tvNoRecords?.visibility = View.VISIBLE
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
    private fun setupHappyPlacesRecycleView(happyPlaceList:ArrayList<HappyPlaceModel>){
        binding?.rvHappy?.layoutManager = LinearLayoutManager(this)
        binding?.rvHappy?.setHasFixedSize(true)
        val placesAdapter = HappyPlacesAdapter(this,happyPlaceList)
        binding?.rvHappy?.adapter= placesAdapter

    }
}