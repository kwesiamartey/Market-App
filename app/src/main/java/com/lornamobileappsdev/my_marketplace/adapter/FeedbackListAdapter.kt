package com.lornamobileappsdev.my_marketplace.adapter


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.responses.FeedbackData
import com.lornamobileappsdev.my_marketplace.ui.SignUpFragment
import com.lornamobileappsdev.my_marketplace.ui.VerifyEmailAdressFragment
import com.lornamobileappsdev.my_marketplace.useCases.*
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import java.text.DecimalFormat
import java.util.*


class FeedbackListAdapter(
    val context: Context,
    private val messageDataList: List<FeedbackData>,
    val lifecycleOwner: LifecycleOwner,
    val viewModelClass: MyViewModel,
    val replyOnClickListener: OnClickListernersRecyclervire,
    val onReplyLikeListernersRecyclervire: OnReplyLikeListernersRecyclervire,
    val onCommentLikeListernersRecyclervire: OnCommentLikeListernersRecyclervire,
    val replyLikesCounts: ReplyLikesCounts,
    val commentLikesCounts: CommentLikesCounts,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderTwo(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 0) {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.activity_feedback_list_item, parent, false)
            return ViewHolder(view)
        } else {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.activity_reply_feedback_list_item, parent, false)
            return ViewHolderTwo(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hold = messageDataList[position]

        lifecycleOwner.lifecycleScope.launchWhenStarted {
            try {
                if (replyLikesCounts.replyLikesCounts(
                        hold.product_id!!,
                        hold.id!!,
                        hold.user_id!!
                    ).size == 0
                ) {

                } else {
                    holder.itemView.findViewById<TextView>(R.id.reply_like_count).text =
                        " " + replyLikesCounts.replyLikesCounts(
                            hold.product_id,
                            hold.id,
                            hold.user_id
                        ).size.toString()
                }

                viewModelClass.queryUserDetails().collect {

                    replyLikesCounts.replyLikesCounts(
                        hold.product_id,
                        hold.id,
                        hold.user_id
                    ).asFlow().collect{k ->

                        if (it == null) {
                            val img: Drawable =
                                holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                    .context.resources.getDrawable(R.drawable.ic_thumb_2)
                            holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                .setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)

                        } else if (it.id == k.my_id) {
                            val img: Drawable =
                                holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                    .context.resources.getDrawable(R.drawable.ic_thumb)
                            holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                .setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
                        } else {
                            val img: Drawable =
                                holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                    .context.resources.getDrawable(R.drawable.ic_thumb_2)
                            holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                .setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
                        }

                    }

                }


            } catch (e: Exception) { }

        }

        lifecycleOwner.lifecycleScope.launchWhenStarted {
            try {

                if (commentLikesCounts.commentLikesCounts(
                        hold.product_id!!,
                        hold.id!!,
                        hold.user_id!!
                    ).size == 0
                ) {

                } else {
                    holder.itemView.findViewById<TextView>(R.id.like_count).text =
                        " " + commentLikesCounts.commentLikesCounts(
                            hold.product_id,
                            hold.id,
                            hold.user_id
                        ).size.toString()
                }


                viewModelClass.queryUserDetails().collect {

                    commentLikesCounts.commentLikesCounts(
                        hold.product_id,
                        hold.id,
                        hold.user_id
                    ).asFlow().collect{g ->


                        if (it == null) {
                            val img: Drawable =
                                holder.itemView.findViewById<TextView>(R.id.like_count).context
                                    .resources.getDrawable(R.drawable.ic_thumb_2)
                            holder.itemView.findViewById<TextView>(R.id.like_count)
                                .setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)

                        } else if (it.id == g.my_id
                        ) {
                            val img: Drawable =
                                holder.itemView.findViewById<TextView>(R.id.like_count).context
                                    .resources.getDrawable(R.drawable.ic_thumb)
                            holder.itemView.findViewById<TextView>(R.id.like_count)
                                .setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
                        } else {
                            val img: Drawable =
                                holder.itemView.findViewById<TextView>(R.id.like_count).context
                                    .resources.getDrawable(R.drawable.ic_thumb_2)
                            holder.itemView.findViewById<TextView>(R.id.like_count)
                                .setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)

                        }

                    }

                }


            } catch (e: Exception) {
            }
        }

        if (hold.type == "comments") {


            val imageView = holder.itemView.findViewById<ImageView>(R.id.country_list_img)
            holder.itemView.findViewById<TextView>(R.id.feedback_message_txt).text = hold.message
            holder.itemView.findViewById<TextView>(R.id.txt_feedback_user).text =
                hold.user_name.toString()
            holder.itemView.findViewById<TextView>(R.id.txt_dataandtime).text =
                hold.date_and_time.toString()
            holder.itemView.findViewById<TextView>(R.id.like_count).text = hold.liked.toString()

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            when (hold.avatar) {

                null -> {
                    Glide.with(context)
                        .asBitmap()
                        .circleCrop()
                        .placeholder(R.drawable.ic_avatar)
                        .fitCenter()
                        .into(holder.itemView.findViewById(R.id.feedback_image)!!)
                }

                else -> {
                    Glide.with(context)
                        .asBitmap()
                        .load(hold.avatar)
                        .circleCrop()
                        .placeholder(circularProgressDrawable)
                        .fitCenter()
                        .into(holder.itemView.findViewById(R.id.feedback_image)!!)
                }
            }

            holder.itemView.findViewById<TextView>(R.id.reply_comment).setOnClickListener {

                lifecycleOwner.lifecycleScope.launchWhenStarted {

                    lifecycleOwner.lifecycleScope.launchWhenStarted {

                        viewModelClass.queryUserDetails().collect {
                            Log.i("detailing", "${it}")
                            if (it == null) {
                                try {
                                    Snackbar.make(
                                        holder.itemView.findViewById<TextView>(R.id.like_comment),
                                        "Please register / Sign in",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                } catch (e: Exception) {
                                }
                                lifecycleOwner.lifecycleScope.launchWhenStarted {
                                    delay(2000L)
                                }
                                try {
                                    val intent = Intent(context, SignUpFragment::class.java)
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                }
                            } else if (it.accountStatus == "Verified") {
                                try {
                                    replyOnClickListener.onReplyCellClickListener(hold)
                                } catch (e: Exception) {
                                }

                            } else if (it.accountStatus == "Not verified") {
                                viewModelClass.email.value = it.email
                                //startActivity(Intent.createChooser(getEmailIntent(mResponse.email!!, "Verification Code", mResponse.verification_code), "Send mail"))
                                val alertDialog =
                                    androidx.appcompat.app.AlertDialog.Builder(context)
                                alertDialog.setCancelable(false)
                                alertDialog.setTitle("Account not verified")
                                alertDialog.setCancelable(true)
                                alertDialog.setMessage("Your account is created successfully but not verified, Please verify your account.")
                                alertDialog.setPositiveButton("Continue") { _, _ ->
                                    val intent =
                                        Intent(context, VerifyEmailAdressFragment::class.java)
                                    context.startActivity(intent)
                                }
                                alertDialog.show()
                            }
                        }
                    }

                }

            }


            //like buttons for comment feed post-------------------------------------------------------------------------------------------------
            holder.itemView.findViewById<TextView>(R.id.like_comment).setOnClickListener {

                try {
                    holder.itemView.findViewById<ProgressBar>(R.id.comment_like_count_prgress).visibility =
                        View.VISIBLE
                }catch (e:Exception){}

                val likeButton = holder.itemView.findViewById<TextView>(R.id.like_count)
                val animationSet = AnimationSet(true)
                animationSet.addAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.like_button_anim
                    )
                )
                likeButton.startAnimation(animationSet)

                lifecycleOwner.lifecycleScope.launchWhenStarted {

                    lifecycleOwner.lifecycleScope.launchWhenStarted {

                        viewModelClass.queryUserDetails().collect {
                            Log.i("detailing", "${it}")
                            if (it == null) {
                                try {
                                    Snackbar.make(
                                        holder.itemView.findViewById<TextView>(R.id.like_comment),
                                        "Please register / Sign in",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                } catch (e: Exception) {
                                }
                                lifecycleOwner.lifecycleScope.launchWhenStarted {
                                    delay(2000L)
                                }
                                try {
                                    val intent = Intent(context, SignUpFragment::class.java)
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                }
                            } else if (it.accountStatus == "Verified") {
                                try {
                                    //holder.itemView.findViewById<TextView>(R.id.comment_like_count_prgress).visibility = View.VISIBLE
                                    //Toast.makeText(context, "${hold.id}", Toast.LENGTH_LONG).show()

                                    try {
                                        holder.itemView.findViewById<TextView>(R.id.like_count).text =
                                            " " + onCommentLikeListernersRecyclervire.onCommentLikeListernersRecyclervire(
                                                hold.product_id!!,
                                                hold.user_id!!,
                                                hold.id!!,
                                                it.id!!,
                                                1
                                            ).toString()


                                        if (it == null) {
                                            try {
                                                val img: Drawable =
                                                    holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                                        .context.resources
                                                        .getDrawable(R.drawable.ic_thumb_2)
                                                holder.itemView.findViewById<TextView>(R.id.like_count)
                                                    .setCompoundDrawablesWithIntrinsicBounds(
                                                        img,
                                                        null,
                                                        null,
                                                        null
                                                    )
                                                holder.itemView.findViewById<ProgressBar>(R.id.comment_like_count_prgress).visibility =
                                                    View.GONE
                                            } catch (e: Exception) {
                                            }


                                        } else if (commentLikesCounts.commentLikesCounts(
                                                hold.product_id,
                                                hold.id,
                                                hold.user_id
                                            ).size == 0
                                        ) {
                                            try {
                                                holder.itemView.findViewById<ProgressBar>(R.id.comment_like_count_prgress).visibility = View.GONE

                                                val img: Drawable =
                                                    holder.itemView.findViewById<TextView>(R.id.like_count)
                                                        .context.resources
                                                        .getDrawable(R.drawable.ic_thumb_2)
                                                holder.itemView.findViewById<TextView>(R.id.like_count)
                                                    .setCompoundDrawablesWithIntrinsicBounds(
                                                        img,
                                                        null,
                                                        null,
                                                        null
                                                    )

                                            } catch (e: Exception) {
                                            }

                                        } else {

                                            try {
                                                holder.itemView.findViewById<ProgressBar>(R.id.comment_like_count_prgress).visibility = View.GONE

                                                holder.itemView.findViewById<TextView>(R.id.like_count).text =
                                                    " " + commentLikesCounts.commentLikesCounts(
                                                        hold.product_id,
                                                        hold.id,
                                                        hold.user_id
                                                    ).size.toString()

                                            } catch (e: Exception) {
                                            }

                                        }

                                        lifecycleOwner.lifecycleScope.launchWhenStarted {

                                            viewModelClass.queryUserDetails().collect {
                                                if (it == null) {
                                                    try {
                                                        val img: Drawable =
                                                            holder.itemView.findViewById<TextView>(R.id.like_count)
                                                                .context.resources
                                                                .getDrawable(R.drawable.ic_thumb_2)
                                                        holder.itemView.findViewById<TextView>(R.id.like_count)
                                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                                img,
                                                                null,
                                                                null,
                                                                null
                                                            )
                                                    }catch (e:Exception){}

                                                }  else if (commentLikesCounts.commentLikesCounts(
                                                        hold.product_id,
                                                        hold.id,
                                                        hold.user_id
                                                    ).size == 0
                                                ) {
                                                    try {
                                                        val img: Drawable =
                                                            holder.itemView.findViewById<TextView>(R.id.like_count)
                                                                .context.resources
                                                                .getDrawable(R.drawable.ic_thumb_2)
                                                        holder.itemView.findViewById<TextView>(R.id.like_count)
                                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                                img,
                                                                null,
                                                                null,
                                                                null
                                                            )
                                                    }catch (e:Exception){}


                                                }
                                                else if (it.id == commentLikesCounts.commentLikesCounts(
                                                        hold.product_id,
                                                        hold.id,
                                                        hold.user_id
                                                    )[0].my_id
                                                ) {
                                                   try {
                                                       val img: Drawable =
                                                           holder.itemView.findViewById<TextView>(R.id.like_count)
                                                               .context.resources
                                                               .getDrawable(R.drawable.ic_thumb)
                                                       holder.itemView.findViewById<TextView>(R.id.like_count)
                                                           .setCompoundDrawablesWithIntrinsicBounds(
                                                               img,
                                                               null,
                                                               null,
                                                               null
                                                           )
                                                   }catch (e:Exception){}
                                                } else {
                                                    try {
                                                        val img: Drawable =
                                                            holder.itemView.findViewById<TextView>(R.id.like_count)
                                                                .context.resources
                                                                .getDrawable(R.drawable.ic_thumb_2)
                                                        holder.itemView.findViewById<TextView>(R.id.like_count)
                                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                                img,
                                                                null,
                                                                null,
                                                                null
                                                            )
                                                    }catch (e:Exception){}

                                                }

                                            }
                                        }


                                    } catch (e: Exception) {
                                    }


                                } catch (e: Exception) {
                                }

                            } else if (it.accountStatus == "Not verified") {
                                viewModelClass.email.value = it.email
                                //startActivity(Intent.createChooser(getEmailIntent(mResponse.email!!, "Verification Code", mResponse.verification_code), "Send mail"))
                                val alertDialog =
                                    androidx.appcompat.app.AlertDialog.Builder(context)
                                alertDialog.setCancelable(false)
                                alertDialog.setTitle("Account not verified")
                                alertDialog.setCancelable(true)
                                alertDialog.setMessage("Your account is created successfully but not verified, Please verify your account.")
                                alertDialog.setPositiveButton("Continue") { _, _ ->
                                    val intent =
                                        Intent(context, VerifyEmailAdressFragment::class.java)
                                    context.startActivity(intent)
                                }
                                alertDialog.show()
                            }
                        }
                    }

                }
            }

        } else if (hold.type == "reply") {

            val imageView = holder.itemView.findViewById<ImageView>(R.id.country_list_img)
            holder.itemView.findViewById<TextView>(R.id.feedback_message_txt).text = hold.message
            holder.itemView.findViewById<TextView>(R.id.txt_feedback_user).text =
                hold.user_name.toString()
            holder.itemView.findViewById<TextView>(R.id.txt_dataandtime).text =
                hold.date_and_time.toString()
            holder.itemView.findViewById<TextView>(R.id.reply_like_count).text =
                hold.liked.toString()

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            when (hold.avatar) {

                null -> {
                    Glide.with(context)
                        .asBitmap()
                        .circleCrop()
                        .placeholder(R.drawable.ic_avatar)
                        .fitCenter()
                        .into(holder.itemView.findViewById(R.id.feedback_image)!!)
                }

                else -> {
                    Glide.with(context)
                        .asBitmap()
                        .load(hold.avatar)
                        .circleCrop()
                        .placeholder(circularProgressDrawable)
                        .fitCenter()
                        .into(holder.itemView.findViewById(R.id.feedback_image)!!)
                }

            }


            holder.itemView.findViewById<TextView>(R.id.reply_comment).setOnClickListener {

                lifecycleOwner.lifecycleScope.launchWhenStarted {

                    viewModelClass.queryUserDetails().collect {
                        Log.i("detailing", "${it}")
                        if (it == null) {
                            try {
                                Snackbar.make(
                                    holder.itemView.findViewById<TextView>(R.id.like_comment),
                                    "Please register / Sign in",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            } catch (e: Exception) {
                            }
                            lifecycleOwner.lifecycleScope.launchWhenStarted {
                                delay(2000L)
                            }
                            try {
                                val intent = Intent(context, SignUpFragment::class.java)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                            }
                        } else if (it.accountStatus == "Verified") {
                            try {
                                replyOnClickListener.onReplyCellClickListener(hold)
                            } catch (e: Exception) {
                            }

                        } else if (it.accountStatus == "Not verified") {
                            viewModelClass.email.value = it.email
                            //startActivity(Intent.createChooser(getEmailIntent(mResponse.email!!, "Verification Code", mResponse.verification_code), "Send mail"))
                            val alertDialog =
                                androidx.appcompat.app.AlertDialog.Builder(context)
                            alertDialog.setCancelable(false)
                            alertDialog.setTitle("Account not verified")
                            alertDialog.setCancelable(true)
                            alertDialog.setMessage("Your account is created successfully but not verified, Please verify your account.")
                            alertDialog.setPositiveButton("Continue") { _, _ ->
                                val intent = Intent(context, VerifyEmailAdressFragment::class.java)
                                context.startActivity(intent)
                            }
                            alertDialog.show()
                        }
                    }
                }
            }

            //like button for reply on post feedback-----------------------------------------------------------------------------------------
            holder.itemView.findViewById<TextView>(R.id.reply_like_comment).setOnClickListener {

                try {
                    holder.itemView.findViewById<ProgressBar>(R.id.reply_like_count_prgress).visibility =
                        View.VISIBLE
                }catch (e:Exception){}

                val likeButton = holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                val animationSet = AnimationSet(true)
                animationSet.addAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.like_button_anim
                    )
                )
                likeButton.startAnimation(animationSet)


                lifecycleOwner.lifecycleScope.launchWhenStarted {

                    viewModelClass.queryUserDetails().collect {
                        Log.i("detailing", "${it}")
                        if (it == null) {
                            try {
                                Snackbar.make(
                                    holder.itemView.findViewById<TextView>(R.id.reply_like_comment),
                                    "Please register / Sign in",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            } catch (e: Exception) {
                            }
                            lifecycleOwner.lifecycleScope.launchWhenStarted {
                                delay(2000L)
                            }
                            try {
                                val intent = Intent(context, SignUpFragment::class.java)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                            }
                        } else if (it.accountStatus == "Verified") {
                            try {
                                //like button-------------------------------------------------------------------------------------
                                //holder.itemView.findViewById<TextView>(R.id.reply_like_count_prgress).visibility = View.VISIBLE
                                // Toast.makeText(context, "${hold.comment_id}", Toast.LENGTH_LONG).show()


                                try {
                                    holder.itemView.findViewById<TextView>(R.id.reply_like_count).text =
                                        " " + onReplyLikeListernersRecyclervire.onReplyLikeListernersRecyclervire(
                                            hold.product_id!!,
                                            hold.user_id!!,
                                            hold.id!!,
                                            it.id!!,
                                            1
                                        ).toString()

                                    if (replyLikesCounts.replyLikesCounts(
                                            hold.product_id,
                                            hold.id,
                                            hold.user_id
                                        ).size == 0
                                    ) {
                                        val img: Drawable = holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                                .context.resources
                                                .getDrawable(R.drawable.ic_thumb_2)
                                        holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                img,
                                                null,
                                                null,
                                                null
                                            )

                                    } else {
                                        holder.itemView.findViewById<TextView>(R.id.reply_like_count).text =
                                            " " + replyLikesCounts.replyLikesCounts(
                                                hold.product_id,
                                                hold.id,
                                                hold.user_id
                                            ).size.toString()
                                    }

                                    lifecycleOwner.lifecycleScope.launchWhenStarted {

                                        viewModelClass.queryUserDetails().collect {
                                            if (it == null) {
                                                try {
                                                    val img: Drawable =
                                                        holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                                            .context.resources
                                                            .getDrawable(R.drawable.ic_thumb_2)
                                                    holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                                        .setCompoundDrawablesWithIntrinsicBounds(
                                                            img,
                                                            null,
                                                            null,
                                                            null
                                                        )
                                                    holder.itemView.findViewById<ProgressBar>(R.id.reply_like_count_prgress).visibility =
                                                        View.GONE

                                                } catch (e: Exception) {
                                                }

                                            } else if (replyLikesCounts.replyLikesCounts(
                                                    hold.product_id,
                                                    hold.id,
                                                    hold.user_id
                                                ).size == 0
                                            ) {
                                                try {
                                                    val img: Drawable =
                                                        holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                                            .context.resources
                                                            .getDrawable(R.drawable.ic_thumb_2)
                                                    holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                                        .setCompoundDrawablesWithIntrinsicBounds(
                                                            img,
                                                            null,
                                                            null,
                                                            null
                                                        )
                                                    holder.itemView.findViewById<ProgressBar>(R.id.reply_like_count_prgress).visibility =
                                                        View.GONE

                                                } catch (e: Exception) {
                                                }


                                            } else if (it.id == replyLikesCounts.replyLikesCounts(
                                                    hold.product_id,
                                                    hold.id,
                                                    hold.user_id
                                                )[0].my_id
                                            ) {
                                                try {
                                                    holder.itemView.findViewById<ProgressBar>(R.id.reply_like_count_prgress).visibility =
                                                        View.GONE
                                                    val img: Drawable =
                                                        holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                                            .context.resources
                                                            .getDrawable(R.drawable.ic_thumb)
                                                    holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                                        .setCompoundDrawablesWithIntrinsicBounds(
                                                            img,
                                                            null,
                                                            null,
                                                            null
                                                        )

                                                } catch (e: Exception) {
                                                }


                                            } else {
                                                try {
                                                    val img: Drawable =
                                                        holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                                            .context.resources
                                                            .getDrawable(R.drawable.ic_thumb_2)
                                                    holder.itemView.findViewById<TextView>(R.id.reply_like_count)
                                                        .setCompoundDrawablesWithIntrinsicBounds(
                                                            img,
                                                            null,
                                                            null,
                                                            null
                                                        )
                                                    holder.itemView.findViewById<ProgressBar>(R.id.reply_like_count_prgress).visibility =
                                                        View.GONE
                                                } catch (e: Exception) {
                                                }


                                            }

                                        }


                                    }


                                } catch (e: Exception) {
                                }


                            } catch (e: Exception) {
                            }

                        } else if (it.accountStatus == "Not verified") {
                            viewModelClass.email.value = it.email
                            //startActivity(Intent.createChooser(getEmailIntent(mResponse.email!!, "Verification Code", mResponse.verification_code), "Send mail"))
                            val alertDialog =
                                androidx.appcompat.app.AlertDialog.Builder(context)
                            alertDialog.setCancelable(false)
                            alertDialog.setTitle("Account not verified")
                            alertDialog.setCancelable(true)
                            alertDialog.setMessage("Your account is created successfully but not verified, Please verify your account.")
                            alertDialog.setPositiveButton("Continue") { _, _ ->
                                val intent =
                                    Intent(context, VerifyEmailAdressFragment::class.java)
                                context.startActivity(intent)
                            }
                            alertDialog.show()
                        }
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messageDataList[position].type!! == "comments") {
            return 0
        } else {
            return 1
        }
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

    fun formatRelativeDate(date: Date): String {
        val now = Calendar.getInstance().timeInMillis
        val diffInMillis = now - date.time
        val diffInDays = diffInMillis / (24 * 60 * 60 * 1000)
        val diffInWeeks = diffInDays / 7

        return when {
            diffInDays < 1 -> "today"
            diffInDays == 1L -> "1 day ago"
            diffInDays < 7 -> "$diffInDays days ago"
            diffInWeeks == 1L -> "1 week ago"
            diffInWeeks < 52 -> "$diffInWeeks weeks ago"
            else -> "over a year ago"
        }
    }

}