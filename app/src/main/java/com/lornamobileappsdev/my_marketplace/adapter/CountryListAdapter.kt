package com.lornamobileappsdev.my_marketplace.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.entity.CountryListData
import com.lornamobileappsdev.my_marketplace.ui.DashboardFragment
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel

class CountryListAdapter(
    val context: Context,
    val countryList: List<CountryListData>,
    val lifecycleOwner: LifecycleOwner,
    val viewModel: MyViewModel
) : RecyclerView.Adapter<CountryListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(countryList: CountryListData, position: Int) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(context)
                .asBitmap()
                .load(
                "${countryList.img}"
            ).placeholder(circularProgressDrawable).fitCenter()
                .into(itemView.findViewById(R.id.country_list_img))
            itemView.findViewById<TextView>(R.id.country_list_names).text = countryList.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.activity_countrylist_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hold = countryList[position]
        holder.setData(hold, position)
        holder.itemView.findViewById<LinearLayout>(R.id.country_list_item_wrapper)
            .setOnClickListener {

                try {
                    viewModel.selectedCountryName.postValue(hold.name)
                    Log.i("country name", "3 ="+hold.name.toString())
                    val intent = Intent(context, DashboardFragment::class.java)
                    context.startActivity(intent)
                } catch (e: Exception) {
                }
            }
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

}