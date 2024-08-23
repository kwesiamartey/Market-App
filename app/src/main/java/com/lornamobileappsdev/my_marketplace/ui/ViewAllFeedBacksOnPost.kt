package com.lornamobileappsdev.my_marketplace.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.adapter.FeedbackListAdapter
import com.lornamobileappsdev.my_marketplace.databinding.FragmentViewFeedBacksOnPostBinding
import com.lornamobileappsdev.my_marketplace.models.CommentData
import com.lornamobileappsdev.my_marketplace.models.PostLikesData
import com.lornamobileappsdev.my_marketplace.responses.FeedbackData
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.useCases.CommentLikesCounts
import com.lornamobileappsdev.my_marketplace.useCases.OnClickListernersRecyclervire
import com.lornamobileappsdev.my_marketplace.useCases.OnCommentLikeListernersRecyclervire
import com.lornamobileappsdev.my_marketplace.useCases.OnReplyLikeListernersRecyclervire
import com.lornamobileappsdev.my_marketplace.useCases.ReplyLikesCounts
import com.lornamobileappsdev.my_marketplace.useCases.SearchProduct
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class ViewAllFeedBacksOnPost : Fragment(), OnClickListernersRecyclervire,

    OnCommentLikeListernersRecyclervire, OnReplyLikeListernersRecyclervire, CommentLikesCounts,
    ReplyLikesCounts {


    lateinit var _bind:FragmentViewFeedBacksOnPostBinding

    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    val viewModelClass1: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }
    lateinit var layoutManager1: LinearLayoutManager

    val expandIn by lazy {
        AnimationUtils.loadAnimation(context, R.anim.anim)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _bind = FragmentViewFeedBacksOnPostBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            getProductFeedback()
        }


        bind.bk.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(android.R.id.content, Single_Selected_item_show()).addToBackStack(null).commit()
        }

        return bind.root
    }

    suspend fun CoroutineScope.getProductFeedback() {

        launch {

            layoutManager1 = LinearLayoutManager(requireContext())
            layoutManager1.orientation = LinearLayoutManager.VERTICAL
            layoutManager1.isSmoothScrollbarEnabled = true
            bind.feedbackRecyclerview.layoutManager = layoutManager1
            bind.feedbackLoading.visibility = View.VISIBLE
            try {
                launch{
                    val client = Singleton.Singleton.apiClient.api.getProductFeedback(viewModelClass.productId.value!!.toInt())
                    if(client.isSuccessful){
                        if(client.code() == 200){
                            launch {
                                client.body()?.let { y ->
                                    if (y.size == 0) {
                                        bind.feedWrapper.visibility = View.GONE
                                    } else if (y.isNullOrEmpty()) {
                                        bind.feedWrapper.visibility = View.GONE
                                    } else {
                                        bind.feedWrapper.visibility = View.VISIBLE
                                        try {

                                            val adapter = FeedbackListAdapter(
                                                requireContext(), y, requireActivity(),
                                                viewModelClass,
                                                this@ViewAllFeedBacksOnPost,
                                                this@ViewAllFeedBacksOnPost,
                                                this@ViewAllFeedBacksOnPost,
                                                this@ViewAllFeedBacksOnPost,
                                                this@ViewAllFeedBacksOnPost
                                            )
                                            bind.feedbackRecyclerview.adapter = adapter
                                            bind.feedbackLoading.visibility = View.GONE
                                        } catch (e: Exception) {
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) { }


        }

    }



    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReplyCellClickListener(feed: FeedbackData) {

        val s = SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
        val format: String = s.format(Date())
        val timeStamp = format

        val ss = SimpleDateFormat("hh:mm:ss") //,hh:mm:ss
        val format_time: String = ss.format(Date())
        val timeSt = format_time
        lifecycleScope.launchWhenStarted {
            viewModelClass.queryUserDetails().collect {
                if (it == null) {
                    val alertDialog = AlertDialog.Builder(requireContext())
                    alertDialog.setMessage("You haven't registered or signed in please do so.")
                    alertDialog.setPositiveButton("Continue") { _, _ ->
                        val intent = Intent(requireContext(), SignUpFragment::class.java)
                        startActivity(intent)
                    }
                    alertDialog.show()
                } else {
                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                    bottomSheetDialog.setContentView(R.layout.activity_comment_reply_box)
                    bottomSheetDialog.findViewById<TextView>(R.id.send_a_comment)!!
                        .setOnClickListener {
                            it.startAnimation(expandIn)
                            bottomSheetDialog.findViewById<ProgressBar>(R.id.submittingrrrprogress)!!.visibility =
                                View.VISIBLE
                            val comment =
                                bottomSheetDialog.findViewById<EditText>(R.id.comment_txt)!!.text
                            if (comment!!.isEmpty()) {
                                bottomSheetDialog.findViewById<ProgressBar>(R.id.submittingrrrprogress)!!.visibility =
                                    View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    "Reply shouldn't be empty",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                it.startAnimation(expandIn)
                                lifecycleScope.launchWhenStarted {
                                    viewModelClass.queryUserDetails().asLiveData(Dispatchers.IO)
                                        .observe(viewLifecycleOwner, Observer { b ->
                                            if (b == null) {
                                                val alertDialog =
                                                    AlertDialog.Builder(requireContext())
                                                alertDialog.setMessage("You haven't registered or signed in please do so.")
                                                alertDialog.setPositiveButton("Continue") { _, _ ->
                                                    val intent = Intent(
                                                        requireContext(),
                                                        SignUpFragment::class.java
                                                    )
                                                    startActivity(intent)
                                                }
                                                alertDialog.show()
                                            } else {

                                                viewModelClass.productId.observe(
                                                    viewLifecycleOwner,
                                                    Observer { p ->
                                                        lifecycleScope.launchWhenCreated {
                                                            val data = SearchProduct.searchProduct(
                                                                "approved",
                                                                p.toInt()
                                                            )
                                                            data.forEach { i ->
                                                                try{
                                                                    val replyData = CommentData(
                                                                        user_id = b.id,
                                                                        product_id = feed.product_id,
                                                                        comment = comment.toString(),
                                                                        liked = 0,
                                                                        date_and_time = "$timeStamp: $timeSt",
                                                                        comment_id = feed.id,
                                                                        type = "reply"
                                                                    )
                                                                    Log.i(
                                                                        "cb",
                                                                        replyData.toString()
                                                                    )
                                                                    //commentingOnPost(myUserId!!.toInt(), chatData)
                                                                    Singleton.Singleton.apiClient.api.comment(
                                                                        b.id!!.toInt(),
                                                                        replyData
                                                                    ).enqueue(object :
                                                                        Callback<Boolean> {
                                                                        override fun onResponse(
                                                                            call: Call<Boolean>,
                                                                            response: Response<Boolean>
                                                                        ) {
                                                                            val mResponse =
                                                                                response.body()
                                                                            if (response.isSuccessful) {
                                                                                if (response.code() == 200) {
                                                                                    lifecycleScope.launchWhenStarted {
                                                                                        GlobalScope.launch(
                                                                                            Dispatchers.IO
                                                                                        ) {
                                                                                            getProductFeedback()
                                                                                        }
                                                                                    }
                                                                                    bottomSheetDialog.findViewById<ProgressBar>(
                                                                                        R.id.submittingrrrprogress
                                                                                    )!!.visibility =
                                                                                        View.GONE
                                                                                    bottomSheetDialog.findViewById<EditText>(
                                                                                        R.id.comment_txt
                                                                                    )!!
                                                                                        .setText("Reply sent.")
                                                                                } else {
                                                                                    lifecycleScope.launchWhenStarted {
                                                                                        getProductFeedback()
                                                                                    }
                                                                                    bottomSheetDialog.findViewById<ProgressBar>(
                                                                                        R.id.submittingrrrprogress
                                                                                    )!!.visibility =
                                                                                        View.GONE
                                                                                    Snackbar.make(
                                                                                        bind.feedWrapper,
                                                                                        "Error Occured",
                                                                                        Snackbar.LENGTH_LONG
                                                                                    ).show()
                                                                                }
                                                                            } else if (response.code() == 404) {
                                                                                lifecycleScope.launchWhenStarted {
                                                                                    getProductFeedback()
                                                                                }
                                                                                bottomSheetDialog.findViewById<ProgressBar>(
                                                                                    R.id.submittingrrrprogress
                                                                                )!!.visibility =
                                                                                    View.GONE
                                                                                Snackbar.make(
                                                                                    bind.feedWrapper,
                                                                                    "Error Occured",
                                                                                    Snackbar.LENGTH_LONG
                                                                                ).show()
                                                                            } else if (response.code() == 500) {
                                                                                lifecycleScope.launchWhenStarted {
                                                                                    getProductFeedback()
                                                                                }
                                                                                bottomSheetDialog.findViewById<ProgressBar>(
                                                                                    R.id.submittingrrrprogress
                                                                                )!!.visibility =
                                                                                    View.GONE
                                                                                Snackbar.make(
                                                                                    bind.feedWrapper,
                                                                                    "Error Occured",
                                                                                    Snackbar.LENGTH_LONG
                                                                                ).show()
                                                                            }
                                                                        }

                                                                        override fun onFailure(
                                                                            call: Call<Boolean>,
                                                                            t: Throwable
                                                                        ) {
                                                                            Snackbar.make(
                                                                                bind.feedWrapper,
                                                                                "Error Occured",
                                                                                Snackbar.LENGTH_LONG
                                                                            ).show()
                                                                        }
                                                                    })
                                                                }catch (e:Exception){}
                                                            }
                                                        }
                                                    })
                                            }
                                        })
                                }
                            }
                        }
                    bottomSheetDialog.setCancelable(true)
                    bottomSheetDialog.show()
                }
            }
        }
    }

    override suspend fun onCommentLikeListernersRecyclervire(comment_id: Int, userId: Int,comment_user_id: Int,my_id:Int , like: Int) : Long{
        //Toast.makeText(requireContext(), "$comment_id/$userId/$like", Toast.LENGTH_LONG).show()

        try {
            val client = Singleton.Singleton.apiClient.api.comment_like(comment_id, userId,comment_user_id,my_id, like)
            if (client.isSuccessful) {
                if (client.code() == 200) {
                    return client.body()!!
                    Log.i("retrofit", client.body()!!.toString())
                } else {
                    0
                    Log.i("retrofit", client.body()!!.toString())
                }
            } else {
                0
                Log.i("retrofit", client.body()!!.toString())
            }

        } catch (e: Exception) { }


        return 0
    }

    override suspend fun onReplyLikeListernersRecyclervire(reply_id: Int, user_id: Int,comment_user_id: Int,my_id:Int ,like:Int) : Long{

        //Toast.makeText(requireContext(), "$reply_id/$user_id/$comment_user_id/$my_id /$like", Toast.LENGTH_LONG).show()

        try {
            val client = Singleton.Singleton.apiClient.api.reply_like(reply_id,
                user_id,comment_user_id,my_id, like)
            if (client.isSuccessful) {
                if (client.code() == 200) {
                    return client.body()!!
                    Log.i("retrofit", client.body()!!.toString())
                } else {
                    0
                    Log.i("retrofit", client.body()!!.toString())
                }
            } else {
                0
                Log.i("retrofit", client.body()!!.toString())
            }

        } catch (e: Exception) { }

        return 0
    }

    override suspend fun commentLikesCounts(post_id: Int, comment_id_user_id: Int, user_id: Int): List<PostLikesData> {
        try {
            val client = Singleton.Singleton.apiClient.api.commentLikesCounts(post_id, comment_id_user_id, user_id)
            if (client.isSuccessful) {
                if (client.code() == 200) {
                    return client.body()!!
                    Log.i("commentCount", client.body()!!.toString())
                } else {
                    return emptyList()
                    Log.i("commentCount", client.body()!!.toString())
                }
            } else {
                return emptyList()
                Log.i("commentCount", client.body()!!.toString())
            }

        } catch (e: Exception) { }

        return emptyList()
    }

    override suspend fun replyLikesCounts(post_id: Int, comment_id_user_id: Int, user_id: Int): List<PostLikesData> {
        try {
            val client = Singleton.Singleton.apiClient.api.replyLikesCounts(post_id, comment_id_user_id, user_id)
            if (client.isSuccessful) {
                if (client.code() == 200) {
                    return client.body()!!
                    Log.i("commentCount", client.body()!!.toString())
                } else {
                    return emptyList()
                    Log.i("commentCount", client.body()!!.toString())
                }
            } else {
                return emptyList()
                Log.i("commentCount", client.body()!!.toString())
            }

        } catch (e: Exception) { }

        return emptyList()
    }

}