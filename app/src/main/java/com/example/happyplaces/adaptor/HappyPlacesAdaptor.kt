package com.example.happyplaces.adaptor

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R
import com.example.happyplaces.model.HappyPlaceModel
import de.hdodenhof.circleimageview.CircleImageView

open class HappyPlacesAdapter(
    private val context: Context,
    private var list: ArrayList<HappyPlaceModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_happy_place,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]


        if (holder is MyViewHolder) {
            holder.itemView.findViewById<CircleImageView>(R.id.rv_place_image).setImageURI(Uri.parse(model.image))
            holder.itemView.findViewById<TextView>(R.id.tvTitle).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tvDescription).text = model.description
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
// END