package com.lornamobileappsdev.my_marketplace.ui


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.lornamobileappsdev.my_marketplace.BuildConfig
import com.lornamobileappsdev.my_marketplace.databinding.ActivityWebPostUploadBinding
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class WebPostUploadFragment : Fragment() {

    lateinit var _bind: ActivityWebPostUploadBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    private lateinit var webView: WebView
    lateinit var progressBar: ProgressBar

    private val FILE_CHOOSER_RESULT_CODE = 1
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = ActivityWebPostUploadBinding.inflate(layoutInflater)

        val window: Window = requireActivity().window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.black)

        val toolbar:Toolbar = bind.webToolbar
        bind.bkBtn.setOnClickListener {
            try{
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, DashboardFragment()).commit()
            }catch (e:Exception){}
        }

        // Initializing Webview and
        // progressBar from the layout file
        webView = bind.webView
        progressBar = bind.progressBar


        val mWebView: WebView = bind.webView
        // Enable JavaScript (optional)
        mWebView.settings.javaScriptEnabled = true

        // Set up WebViewClient to handle page navigation and requests
        mWebView.webViewClient = WebViewClient()

        // Set up WebChromeClient to display progress and alert dialogs
        mWebView.webChromeClient = WebChromeClient()

        // Load the HTML file from the assets folder
        mWebView.loadUrl("${BuildConfig.API_URL}singapore_uploads/android_posting_upload_two.php")
        mWebView.webViewClient = WebViewClient()
        // Loading a URL

        return bind.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setHasOptionsMenu(false)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun openFileChooser(filePathCallback: ValueCallback<Array<Uri>>?) {
        mFilePathCallback = filePathCallback
        val packageManager = requireActivity().packageManager
        var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                ex.printStackTrace()
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.absolutePath
                takePictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile)
                )
            } else {
                null
            }
        }

        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
        contentSelectionIntent.type = "image/*"

        val intentArray: Array<Intent?> = takePictureIntent.let {
            arrayOf(
                it
            )
        }

        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)

        startActivityForResult(chooserIntent, FILE_CHOOSER_RESULT_CODE)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val results = if (data.data != null) {
                        arrayOf(data.data!!)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            val clipData = data.clipData
                            val numSelectedFiles = clipData?.itemCount ?: 0
                            val results = arrayOfNulls<Uri>(numSelectedFiles)
                            for (i in 0 until numSelectedFiles) {
                                results[i] = clipData?.getItemAt(i)?.uri
                            }
                            results
                        } else {
                            null
                        }
                    }
                    mFilePathCallback?.onReceiveValue(results as Array<Uri>?)
                } else {
                    mFilePathCallback?.onReceiveValue(null)
                }
            } else {
                mFilePathCallback?.onReceiveValue(null)
            }
            mFilePathCallback = null
        }
    }

}
