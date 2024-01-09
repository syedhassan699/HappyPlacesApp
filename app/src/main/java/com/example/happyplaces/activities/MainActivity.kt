@file:Suppress("DEPRECATION")

package com.example.happyplaces.activities

import DatabaseHandler
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.adaptor.HappyPlacesAdapter
import com.example.happyplaces.databinding.ActivityMainBinding
import com.example.happyplaces.model.HappyPlaceModel
import com.example.happyplaces.utils.SwipeToDeleteCallback
import com.example.happyplaces.utils.SwipeToEditCallback

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding?= null
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabAddPlace?.setOnClickListener{
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
                startActivityForResult(intent, ADD_HAPPY_PLACE_REQUEST_CODE)
        }
             getHappyPlaceListFromLocaldb()
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

        placesAdapter.setOnClickListener(object: HappyPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {
               val intent= Intent(this@MainActivity,HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS,model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter= binding?.
                rvHappy?.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity,viewHolder.adapterPosition,
                    ADD_HAPPY_PLACE_REQUEST_CODE)
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding?.rvHappy)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter= binding?.
                rvHappy?.adapter as HappyPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlaceListFromLocaldb()
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding?.rvHappy)
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onActivityResult(requestCode, resultCode, data)",
        "androidx.appcompat.app.AppCompatActivity"
    ))
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_HAPPY_PLACE_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
               getHappyPlaceListFromLocaldb()
            }else{
                Log.e("Activity","Cancelled or Back Pressed")
            }
        }

    }

    companion object{
        private const val ADD_HAPPY_PLACE_REQUEST_CODE = 1
         const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}