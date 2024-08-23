package com.lornamobileappsdev.my_marketplace.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.lornamobileappsdev.my_marketplace.BuildConfig
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.responses.MessagingChatData
import com.lornamobileappsdev.my_marketplace.responses.SignupResponses
import com.lornamobileappsdev.my_marketplace.singletons.KtorClient
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

class chatListAdapter(val context:Context, val chatDataList: MutableList<MessagingChatData>, val lifecycleOwner: LifecycleOwner, val viewModelClass: MyViewModel, val userId:Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class MyViewHolderTwo(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 0){
            val view = LayoutInflater.from(context).inflate(R.layout.fragment_user_chat_list_items, parent, false)
            return MyViewHolderTwo(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.fragment_guest_chat_list_items, parent, false)
            return MyViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hold = chatDataList[position]
        if(hold.user_id == userId){
            val imageView = holder.itemView.findViewById<ImageView>(R.id.pr_img)
            holder.itemView.findViewById<TextView>(R.id.txt_user_chat_wrapper).text = hold.message
            holder.itemView.findViewById<TextView>(R.id.user_txt_chat_time_check).text = hold.date_and_time.toString()
            holder.itemView.findViewById<TextView>(R.id.user_txt_chat_time_checked).text = hold.date_and_time.toString()
            //Glide.with(context).load(chatDataList.image).placeholder(R.drawable.placeholder).into(imageView)

            //get product owner avatar
            lifecycleOwner.lifecycleScope.launchWhenStarted {
                try {

                    val its = getUsers(hold.user_id)
                    its.let {
                        Log.i("web_user", "${it}")

                        val circularProgressDrawable =
                            CircularProgressDrawable(context)
                        circularProgressDrawable.strokeWidth = 5f
                        circularProgressDrawable.centerRadius = 30f
                        circularProgressDrawable.start()

                        if(it!!.avatar!!.isEmpty()){
                            Glide.with(context)
                                .asBitmap()
                                .load(it.avatar!!)
                                .transform(CircleCrop())
                                .placeholder(R.drawable.ic_avatar)
                                .fitCenter()
                                .into(imageView)
                        }else{
                            Glide.with(context)
                                .asBitmap()
                                .circleCrop()
                                .load(it.avatar)
                                .placeholder(circularProgressDrawable)
                                .fitCenter()
                                .into(imageView)
                        }
                    }
                } catch (e: Exception) {
                }
            }

        }else{
            val imageView = holder.itemView.findViewById<ImageView>(R.id.pr_img)
            holder.itemView.findViewById<TextView>(R.id.txt_guest_chat_wrapper).text = hold.message
            holder.itemView.findViewById<TextView>(R.id.guest_txt_chat_time_checked).text = hold.date_and_time.toString()
            holder.itemView.findViewById<TextView>(R.id.guest_txt_chat_time_check).text = hold.date_and_time.toString()
            //Glide.with(context).load(chatDataList.image).placeholder(R.drawable.placeholder).into(imageView)
            //get product owner avatar
            lifecycleOwner.lifecycleScope.launchWhenStarted {
                try {

                    val its = getUsers(hold.user_id!!)
                    its.let {
                        Log.i("web_user", "${it}")

                        val circularProgressDrawable =
                            CircularProgressDrawable(context)
                        circularProgressDrawable.strokeWidth = 5f
                        circularProgressDrawable.centerRadius = 30f
                        circularProgressDrawable.start()

                        if(it!!.avatar!!.isEmpty()){
                            Glide.with(context)
                                .asBitmap()
                                .load(it.avatar!!)
                                .transform(CircleCrop())
                                .placeholder(R.drawable.ic_avatar)
                                .fitCenter()
                                .into(imageView)
                        }else{
                            Glide.with(context)
                                .asBitmap()
                                .circleCrop()
                                .load(it.avatar)
                                .placeholder(circularProgressDrawable)
                                .fitCenter()
                                .into(imageView)
                        }
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return chatDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (chatDataList[position].user_id!! == userId) {
            return 0
        }else {
            return 1
        }
        return 2
    }

    suspend fun getUsers(userId:Int) : SignupResponses? {
        delay(1000L)
        try {
            val httpClient = KtorClient.ktorHttpClient
            val response = httpClient.get(BuildConfig.API_URL+"find_user/${userId}")
            val res:String = response.body()
            if(res.length != 0){
                Log.i("pstt", res.toString())
                return Json.decodeFromString(res)!!

            }else{
                return null!!
            }

        } catch (e: Exception) { }

        return null
    }
}