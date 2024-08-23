package com.lornamobileappsdev.my_marketplace.ui

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.room.TypeConverter
import com.example.model.UpdateMyAvator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentUpdateAvatarBinding
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.utils.FileUtils
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import okhttp3.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.NumberFormat
import java.util.*


class UpdateAvatarFragment : AppCompatActivity() {

    lateinit var _bind:FragmentUpdateAvatarBinding

    val bind get() = _bind

    private var cardView: CardView? = null
    private var imageView: ImageView? = null
    private var button: Button? = null
    private val options = arrayOf<CharSequence>("Gallery", "Cancel")
    private var fabMulti: FloatingActionButton? = null
    private var selectedImage: String? = null
    private var userId: Int? = null
    var bitmaped:Bitmap? = null

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _bind  = FragmentUpdateAvatarBinding.inflate(layoutInflater)

        setContentView(bind.root)

        supportActionBar!!.hide()

        cardView = findViewById(R.id.cardview)
        imageView = findViewById(R.id.imageview)
        button = findViewById(R.id.button)
        fabMulti = findViewById(R.id.fab_multi)

        requirePermission()

        cardView!!.setOnClickListener{
            val builder=  AlertDialog.Builder(this@UpdateAvatarFragment)
            builder.setTitle("Select Image")
            builder.setItems(options,
                DialogInterface.OnClickListener { dialog, which ->
                    if (options[which] == "Camera") {
                        val takePic = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(takePic, 0)
                    } else if (options[which] == "Gallery") {
                        val gallery = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(gallery, 1)
                    } else {
                        dialog.dismiss()
                    }
                })
            builder.show()
        }
        button!!.setOnClickListener {
            val updateMyAvator = UpdateMyAvator(
                userId = userId,
                avatar = bitmaped!!.toByteArray(20)
            )
            uploadFileToServer(updateMyAvator)
        }
       /* fabMulti.setOnClickListener{
            val `in` = Intent(this@UpdateAvatarFragment, MultiUpload::class.java)
            startActivity(`in`)
        }*/
        viewModelClass.queryUserDetails().asLiveData().observe(this, Observer {p->
            lifecycleScope.launchWhenStarted {
                try {
                    Log.i("userIId", "${p.id}")
                    userId = p.id
                } catch (e: Exception) {
                }
            }
        })

        bind.backk.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(android.R.id.content, MyProfile()).commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val image = data.extras!!["data"] as Bitmap?
                    selectedImage = FileUtils.getPath(this@UpdateAvatarFragment, getImageUri(this@UpdateAvatarFragment, image))
                    imageView!!.setImageBitmap(image)
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val image: Uri? = data.data
                    val `is`: InputStream? = this@UpdateAvatarFragment.contentResolver.openInputStream(image!!)
                    val bitmap = BitmapFactory.decodeStream(`is`)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                    val btmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true)
                    bitmaped = btmap
                    imageView!!.setImageBitmap(btmap)
                }
            }
        }
    }

    fun getImageUri(context: Context, bitmap: Bitmap?): Uri {
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "myImage", "")
        return Uri.parse(path)
    }

    fun requirePermission() {
        ActivityCompat.requestPermissions(
            this@UpdateAvatarFragment,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
    }

    fun uploadFileToServer(updateMyAvator: UpdateMyAvator) {

        if (viewModelClass.isNetworkAvailable(test_website)) {

            try {

                try {
                    val dialogue = Dialog(this@UpdateAvatarFragment)
                    dialogue.setContentView(R.layout.activity_progress_loader_3)
                    dialogue.setCancelable(false)
                    dialogue.show()
                    Singleton.Singleton.apiClient.api.updateMyAvator(updateMyAvator)
                        .enqueue(object: retrofit2.Callback<Boolean> {

                            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                dialogue.hide()
                                Toast.makeText(this@UpdateAvatarFragment, t.message, Toast.LENGTH_SHORT).show()

                            }

                            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                                val fileModel = response.body()
                                if(fileModel!! == true){
                                    dialogue.hide()
                                    val alertDialog = AlertDialog.Builder(this@UpdateAvatarFragment)
                                    alertDialog.setTitle("Success!!")
                                    alertDialog.setCancelable(false)
                                    alertDialog.setMessage("Profile picture successfully updated.")
                                    alertDialog.setPositiveButton("Login now"){_,_->
                                        try {
                                            viewModelClass.deleteProfileDatatbase()
                                            val intent = Intent(this@UpdateAvatarFragment, SignInFragment::class.java)
                                            startActivity(intent)
                                        }catch(e:Exception){}
                                    }
                                    alertDialog.show()
                                }else{
                                    dialogue.hide()
                                    Toast.makeText(this@UpdateAvatarFragment, "Try again", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                }catch (e:Exception){}


            }catch (e:Exception){}


        } else {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    private fun Bitmap.toByteArray(quality: Int = 50): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

    private fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            bitmap
        } catch (e: java.lang.Exception) {
            e.message
            null
        }
    }

    fun currencyFormat(amount: String): String? {
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "GH"))
        return formatter.format(amount)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun stringToBitMap(encodedString: ByteArray?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            bitmap
        } catch (e: java.lang.Exception) {
            e.message
            null
        }
    }

    fun resizePhoto(bitmap: Bitmap): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val aspRat = w / h
        val W = 400
        val H = W * aspRat
        val b = Bitmap.createScaledBitmap(bitmap, W, H, false)

        return b
    }

    fun resizeImageUri(context: Context, uri: Uri, maxWidth: Int, maxHeight: Int): Uri {
        val resolver = context.contentResolver
        val inputStream = resolver.openInputStream(uri)

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        var width = options.outWidth
        var height = options.outHeight
        var scale = 1

        while (width / scale > maxWidth || height / scale > maxHeight) {
            scale *= 2
        }

        val options2 = BitmapFactory.Options()
        options2.inSampleSize = scale
        val bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options2)

        val outputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        val bytes = outputStream.toByteArray()

        val filename = UUID.randomUUID().toString() + ".jpg"
        val fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
        fos.write(bytes)
        fos.close()

        return Uri.fromFile(context.getFileStreamPath(filename))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                supportFragmentManager.beginTransaction().replace(android.R.id.content, MyProfile()).addToBackStack(null).commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}