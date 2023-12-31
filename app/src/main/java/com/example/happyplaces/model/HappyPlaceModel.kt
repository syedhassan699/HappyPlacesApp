package com.example.happyplaces.model
import java.io.Serializable

data class HappyPlaceModel(
    val id: Int,
    val title:String?,
    val description:String?,
    val date:String?,
    val location:String?,
    val latitude:Double,
    val longitude:Double,
    val image:String?

): Serializable