package com.lornamobileappsdev.my_marketplace.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.adapter.chatListAdapter
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentChatPageBinding
import com.lornamobileappsdev.my_marketplace.models.ChatData
import com.lornamobileappsdev.my_marketplace.responses.ChatDataResponse
import com.lornamobileappsdev.my_marketplace.responses.MessagingChatData
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.useCases.*
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*


class ChatPageFragment : Fragment() {

    lateinit var _bind: FragmentChatPageBinding
    val bind get() = _bind

    val viewModel: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    val chatData = mutableListOf<ChatData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentChatPageBinding.inflate(layoutInflater)

        val window: Window = requireActivity().window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(requireActivity(), android.R.color.black)


        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        layoutManager.isSmoothScrollbarEnabled = true
        bind.chatRecyclerview.layoutManager = layoutManager


        return bind.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //bind.noMessagesBanner.visibility = View.GONE
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).supportActionBar!!.title = "Messages"
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val s = SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
        val format: String = s.format(Date())
        val timeStamp = format

        val ss = SimpleDateFormat("hh:mm:ss") //,hh:mm:ss
        val format_time: String = ss.format(Date())
        val timeSt = format_time

        try {
            Log.i(
                "chat",
                "${viewModel.productId.value}  ${viewModel.userIdChat.value}, ${viewModel.otherIdChat.value}"
            )
        } catch (e: Exception) { }

        lifecycleScope.launch {
            getMessages()
        }

        bind.sendAMessage.setOnClickListener {
            try {
                bind.submittingChatP.visibility = View.VISIBLE
            } catch (e: Exception) { }

            lifecycleScope.launchWhenStarted {
                /*   val chatData = com.yupee.yupee.entity.ChatData(
                     user_id = viewModel.userIdChat.value,
                     chat_id = viewModel.chatId.value,
                     product_id = viewModel.productId.value!!.toInt(),
                     message = bind.chatTxt.text.toString(),
                     date_and_time = "$timeStamp: $timeSt",
                 )
                 sendMessageToStore(viewModel.userIdChat.value!!, chatData)
             }*/

                viewModel.queryUserDetails().asLiveData(Dispatchers.IO).observe(viewLifecycleOwner, Observer {b->

                    viewModel.productId.observe(viewLifecycleOwner, Observer {
                        lifecycleScope.launchWhenCreated {
                            val data = SearchProduct.searchProduct("approved", it.toInt())
                            data.forEach {
                                val chatData = com.lornamobileappsdev.my_marketplace.entity.ChatData(
                                    user_id = b.id,
                                    other_id = viewModel.userIdChat.value,
                                    user_name = it.user_name,
                                    product_id = it.id,
                                    message = bind.chatTxt.text.toString(),
                                    date_and_time = "$timeStamp: $timeSt",
                                )
                                Log.i("d", chatData.toString())
                                sendMessageToStore(viewModel.userIdChat.value!!, chatData)
                            }
                        }
                    })
                })

            }
        }

        bind.callUserImg.setOnClickListener {
            val phoneNumber = "${viewModel.pst_phone_number.value}"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }

        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        if(viewModel.OtherUser_avatar.value == null){
            Glide.with(requireContext())
                .asBitmap()
                .load(viewModel.OtherUser_avatar.value)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_avatar)
                .fitCenter()
                .into(bind.uImage)
        }else{
            Glide.with(requireContext())
                .asBitmap()
                .load(viewModel.OtherUser_avatar.value)
                .circleCrop()
                .placeholder(circularProgressDrawable)
                .fitCenter()
                .into(bind.uImage)
        }

        bind.uName.text = viewModel.Otheruser_name.value
        bind.pName.text = "Price : " +viewModel.OtherUser_price.value + " / " +viewModel.OtherUser_productname.value
        bind.icBack.setOnClickListener {
            try {
                val intent = Intent(requireContext(), MessagesFragment::class.java)
                startActivity(intent)
            }catch (e:Exception){}
        }
        bind.attachAFile.setOnClickListener {
            Snackbar.make(bind.attachAFile, "Coming soon..", Snackbar.LENGTH_LONG).show()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.user_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(requireContext(), MessagesFragment::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Logout")
                alertDialog.setMessage("Are you sure you want to logout?")
                alertDialog.setNegativeButton("Cancel") { _, _ ->

                }
                alertDialog.setPositiveButton("Logout") { _, _ ->
                    viewModel.deleteProfileDatatbase()
                    /*val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)*/
                    (activity as AppCompatActivity).finishAffinity()
                }

                alertDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }

    suspend fun sendMessageToStore(userId: Int, chatData: com.lornamobileappsdev.my_marketplace.entity.ChatData) {
        delay(100L)
        if (viewModel.isNetworkAvailable(test_website)) {

            try {

                Singleton.Singleton.apiClient.chat(userId, chatData).enqueue(object :
                    Callback<ChatDataResponse> {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onResponse(
                        call: Call<ChatDataResponse>,
                        response: Response<ChatDataResponse>
                    ) {
                        val mResponse = response.body()
                        if (response.isSuccessful) {
                            if (response.code() == 200) {

                                try {
                                    var d = bind.chatTxt
                                    d.setText("Message Sent")
                                    bind.submittingChatP.visibility = View.GONE
                                    lifecycleScope.launch {
                                        getMessages()
                                    }

                                } catch (e: Exception) { }

                            } else {
                                try {
                                    lifecycleScope.launch {
                                        getMessages()
                                    }
                                    bind.submittingChatP.visibility = View.GONE
                                } catch (e: Exception) { }
                                //Toast.makeText(requireContext(), mResponse.toString(), Toast.LENGTH_LONG).show()
                            }
                        } else {
                            try {
                                lifecycleScope.launch {
                                    getMessages()
                                }
                                bind.submittingChatP.visibility = View.GONE
                            } catch (e: Exception) {
                            }
                            //Toast.makeText(requireContext(), mResponse.toString(), Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<ChatDataResponse>, t: Throwable) {
                        try {
                            bind.submittingChatP.visibility = View.GONE
                        } catch (e: Exception) {
                        }
                        Toast.makeText(requireContext(), "Network error / slow internet" + t.message.toString(), Toast.LENGTH_LONG).show()
                    }
                })

            }catch (e:Exception){}


        } else {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }

    }

    fun CoroutineScope.getMessages(){
       launch {
           viewModel.queryUserDetails().asLiveData(Dispatchers.IO).observe(viewLifecycleOwner, Observer { myUserId ->
               lifecycleScope.launchWhenStarted {

                   if (viewModel.isNetworkAvailable(test_website)) {

                       try {
                           launch {
                               val message = Messages_list.messages_list(viewModel.chatId.value!!.toInt())
                               if (message.isNotEmpty()) {
                                   try {
                                       bind.chatShimmer.visibility = View.GONE
                                       bind.chatRecyclerview.visibility = View.VISIBLE
                                       Log.i("chats_message", message.toString())
                                       val adapter = chatListAdapter(
                                           requireContext(),
                                           message as MutableList<MessagingChatData>,
                                           requireActivity(),
                                           viewModel,
                                           myUserId.id!!
                                       )
                                       bind.chatRecyclerview.adapter = adapter
                                       //bind.noMeratedingssagesBanner.visibility = View.GONE
                                   } catch (e: Exception) {
                                   }
                               } else {
                                   try {
                                       bind.chatShimmer.visibility = View.GONE
                                       bind.chatRecyclerview.visibility = View.GONE
                                       bind.noMessagesBanner.visibility = View.VISIBLE
                                   } catch (e: Exception) { }
                               }
                           }

                       }catch (e:Exception){}


                   } else {
                       (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                           .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                   }

               }
           })
       }
    }
}