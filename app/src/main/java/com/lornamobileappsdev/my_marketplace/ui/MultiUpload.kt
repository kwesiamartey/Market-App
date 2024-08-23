/*
package com.yupee.yupee.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imageproject.FileModel
import com.example.imageproject.HttpService
import com.example.imageproject.RetrofitBuilder
import com.example.imageproject.adapter.ImageListAdapter
import com.yupee.yupee.R
import com.yupee.yupee.utils.FileUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class MultiUpload : AppCompatActivity() {
    private var btnAdd: Button? = null
    private var btnSubmit: Button? = null
    private var imageList: RecyclerView? = null
    private val images: MutableList<Uri> = ArrayList()

    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101

    private val STORAGE_PERMISSION_CODE = 4655

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_upload)
        btnAdd = findViewById<Button>(R.id.btn_add)
        btnSubmit = findViewById(R.id.btn_upload)
        imageList = findViewById(R.id.image_list)
        btnAdd!!.setOnClickListener{

            if(checkAndRequestPermissions(this@MultiUpload)){
                //selectImageFromGallery()
                //Toast.makeText(requireContext(), "Click", Toast.LENGTH_LONG).show()

                if (checkAndRequestPermissions(this@MultiUpload)) {
                    val gallery = Intent(Intent.ACTION_GET_CONTENT)
                    gallery.type = "image/*" //allow any image file type.
                    gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    startActivityForResult(gallery, 1)
                }
            }
        }
        btnSubmit!!.setOnClickListener{
            uploadToServer()
        }

        lifecycleScope.launchWhenStarted {
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this@MultiUpload,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) else return


        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@MultiUpload,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(
            this@MultiUpload,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@MultiUpload,
                    "Permission granted now you can read the storage",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(this@MultiUpload, "Oops you just denied the permission you will not be able to post an Ads until you grant it", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val WExtstorePermission = ContextCompat.checkSelfPermission(
            this@MultiUpload,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            this@MultiUpload,
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = java.util.ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this@MultiUpload, listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK && data != null) {
                val count = data.clipData!!.itemCount
                var i = 0
                while (i < count) {
                    val image: Uri = data.clipData!!.getItemAt(i).uri
                    val imagePath: String = FileUtils.getPath(this@MultiUpload, image)
                    images.add(Uri.parse(imagePath))
                    i++
                }
                val layoutManager = LinearLayoutManager(this@MultiUpload)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                imageList!!.layoutManager = layoutManager
                val adapter = ImageListAdapter(this@MultiUpload, images)
                imageList!!.setAdapter(adapter)
            }
        }
    }

    //method to call api to upload files to server
    fun uploadToServer() {
        val list: MutableList<MultipartBody.Part> = ArrayList()
        for (uri in images) {
            list.add(prepairFiles("file[]", uri))
        }
        Log.i("inkm", list.toString() )

        val service = RetrofitBuilder.Singleton
        val call = service.retrofitApii.postMultipleUploadApi(list)
        call!!.enqueue(object : Callback<FileModel?> {
            override fun onResponse(call: Call<FileModel?>?, response: Response<FileModel?>) {
                val model: FileModel = response.body()!!
                Toast.makeText(this@MultiUpload, model.message, Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<FileModel?>?, t: Throwable) {
                Toast.makeText(this@MultiUpload, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun prepairFiles(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.getPath())
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody)
    }
}*/