package com.lornamobileappsdev.my_marketplace.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.responses.ChatDataResponse
import com.lornamobileappsdev.my_marketplace.ui.ChatPageFragment
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import java.text.DecimalFormat

class MessagesListAdapter(val context:Context, private val messageDataList: MutableList<ChatDataResponse>, val lifecycleOwner: LifecycleOwner, val viewModelClass: MyViewModel, val userId:Int) : RecyclerView.Adapter<MessagesListAdapter.MyViewHolder>(){
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun setData(messageDataList: ChatDataResponse, position: Int){

            val imageView = itemView.findViewById<ImageView>(R.id.country_list_img)
            itemView.findViewById<TextView>(R.id.txt_messgae).text = messageDataList.message
            itemView.findViewById<TextView>(R.id.txt_mesg_user).text = messageDataList.user_name.toString()
            itemView.findViewById<TextView>(R.id.txt_product_price).text = "GH₵ " + messageDataList.product_price.toString()
            itemView.findViewById<TextView>(R.id.product_title).text = messageDataList.product_name.toString()
            itemView.findViewById<TextView>(R.id.dataeAndtime).text = messageDataList.date_and_time.toString()

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            if(messageDataList.product_owner_image!!.isEmpty()){
                Glide.with(context)
                    .asBitmap()
                    .load(messageDataList.product_owner_image)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.ic_avatar)
                    .fitCenter()
                    .into(itemView.findViewById(R.id.guest_image)!!)
            }else{
                Glide.with(context)
                    .asBitmap()
                    .load(messageDataList.product_owner_image)
                    .circleCrop()
                    .placeholder(circularProgressDrawable)
                    .fitCenter()
                    .into(itemView.findViewById(R.id.guest_image)!!)
            }





        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_message_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hold = messageDataList[position]
        holder.setData(hold, position)
        holder.itemView.findViewById<LinearLayout>(R.id.mesg_wrapper).setOnClickListener {
               viewModelClass.chatId.value = hold.id!!.toInt()
               viewModelClass.otherIdChat.value = hold.other_id
               viewModelClass.productId.value = hold.product_id.toString()
               viewModelClass.userIdChat.value = hold.user_id!!.toInt()
               viewModelClass.OtherUser_avatar.value = hold.product_owner_image
               viewModelClass.OtherUser_productname.value = hold.product_name
               viewModelClass.Otheruser_name.value = hold.user_name
               viewModelClass.OtherUser_price.value = hold.product_price
               //viewModelClass.other_phone_number.value = hold.p


            try{
                (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, ChatPageFragment()).addToBackStack(null).commit()
            }catch (e:Exception){}
        }
        /*  holder.itemView.findViewById<TextView>(R.id.pr_chat_owner).setOnClickListener {
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, ChatPageFragment()).commit()
        }*/
    }

    override fun getItemCount(): Int {
        return messageDataList.size
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }

}