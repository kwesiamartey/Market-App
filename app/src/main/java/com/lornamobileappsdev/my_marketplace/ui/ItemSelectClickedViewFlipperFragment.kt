package com.lornamobileappsdev.my_marketplace.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.lornamobileappsdev.my_marketplace.constant.app_name
import com.lornamobileappsdev.my_marketplace.constant.server_folder
import com.lornamobileappsdev.my_marketplace.databinding.FragmentItemSelectClickedBinding
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream


class ItemSelectClickedViewFlipperFragment : Fragment() {

    lateinit var _bind: FragmentItemSelectClickedBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    val productImagesList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentItemSelectClickedBinding.inflate(layoutInflater)

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.tagged.text = "${viewModelClass.user_name.value} \n ${app_name}"

        try {

            if(viewModelClass.negotiation.value == "1"){
                bind.negotiateTxt.visibility = View.VISIBLE
            }else{
                bind.negotiateTxt.visibility = View.GONE
            }

            viewModelClass.image.value?.let { productImagesList.add(it) }
            viewModelClass.image_two.value?.let { productImagesList.add(it) }
            viewModelClass.image_three.value?.let { productImagesList.add(it) }
            viewModelClass.image_four.value?.let { productImagesList.add(it) }
            viewModelClass.image_five.value?.let { productImagesList.add(it) }
            viewModelClass.image_six.value?.let { productImagesList.add(it) }

            bind.viewFlipperCount.text = "1 to ${productImagesList.count()}"

            for (photo in productImagesList) { //or something like this

                val circularProgressDrawable = CircularProgressDrawable(requireContext())
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                val image = ImageView(requireContext())
                image.scaleType = ImageView.ScaleType.CENTER_INSIDE
                //loadByteArrayIntoImageView(photo, image)

                Glide.with(requireContext())
                    .load("https://structuredappsstreaming.win/${server_folder}/upload/"+ photo)
                    .transform(CenterCrop(), RoundedCorners(1))
                    .placeholder(circularProgressDrawable)
                    .dontAnimate()
                    .into(image)


                bind.singleItemSliderr.addView(image)
                bind.singleItemSliderr.setOnClickListener {
                    try {
                        (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                            .replace(android.R.id.content, PriviewImagesFragment()).addToBackStack(null).commit()
                    } catch (e: Exception) { }
                }
            }

            bind.singleItemSliderr.flipInterval = 5000//5s intervals
            bind.singleItemSliderr.startFlipping()
            bind.prevBtn.setOnClickListener {
                bind.singleItemSliderr.showPrevious()
            }
            bind.nextBtn.setOnClickListener {
                bind.singleItemSliderr.showNext()
            }

        } catch (e: Exception) { }

    }

    fun loadByteArrayIntoImageView(imageByteArray: ByteArray, imageView: ImageView) {
        val quality = 40 // Set the desired compression quality here (0-100)
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        imageView.setImageBitmap(bitmap)
    }

    fun resizeByteArray(originalArray: ByteArray, newSize: Int): ByteArray {
        val resizedArray = ByteArray(newSize)
        val copyLength = minOf(originalArray.size, newSize)
        System.arraycopy(originalArray, 0, resizedArray, 0, copyLength)
        return resizedArray
    }

    fun decodeGzipToByteArray(gzipData: ByteArray): ByteArray {
        val inputStream = ByteArrayInputStream(gzipData)
        val gzipInputStream = GZIPInputStream(inputStream)
        val outputStream = ByteArrayOutputStream()

        val buffer = ByteArray(1024)
        var len: Int
        while (gzipInputStream.read(buffer).also { len = it } > 0) {
            outputStream.write(buffer, 0, len)
        }

        gzipInputStream.close()
        outputStream.close()

        return outputStream.toByteArray()
    }

}