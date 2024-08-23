package com.lornamobileappsdev.my_marketplace.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.databinding.FragmentRatingsBinding
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.*
import java.time.LocalDate


class RatingsFragment : Fragment() {

    lateinit var _bind: FragmentRatingsBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }
    val rating: MutableLiveData<Int>? by lazy {
        MutableLiveData<Int>()
    }
    var user_id:Int? = null
    val product_id:Int?=null
    val rates:Int? = null
    val date_and_time:String?=null

    var myUserId:Int? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentRatingsBinding.inflate(layoutInflater)

        lifecycleScope.launchWhenStarted {
            viewModelClass.queryUserDetails().collect{ user->
                // Toast.makeText(requireContext(), "${it}", Toast.LENGTH_LONG).show()
                viewModelClass.productId.asFlow().collect{productId->
                    try {
                        if (user == null) {


                        } else {
                            Log.i("rt", "${user.id} --- ${productId}")

                            lifecycleScope.launchWhenStarted {
                                //Toast.makeText(requireContext(), "${viewModelClass.productId.value!!.toInt()} == ${it.id}", Toast.LENGTH_LONG).show()
                                getRatingsOfProduct(productId.toInt(), user.id!!)
                            }
                        }

                    } catch (e: Exception) { }
                }
            }
        }

        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("ooooo", viewModelClass.Itemsratings.value.toString())
        try {
            if (viewModelClass.Itemsratings.value == null) {
                try{
                    bind.ratingWrapper.visibility = View.VISIBLE
                    bind.ratingsValue.text = calculateDoubleRating(1).toString()
                }catch (e:Exception){}
            } else {
                try{
                    bind.ratingWrapper.visibility = View.VISIBLE
                    bind.ratingsValue.text = calculateDoubleRating(viewModelClass.Itemsratings.value!!).toString()
                }catch (e:Exception){}
            }
        }catch (e:Exception){}


        bind.ratings1.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
                    if (it == null) {
                        Snackbar.make(
                            bind.ratings1,
                            "You must login first before you can save",
                            Snackbar.LENGTH_LONG
                        ).show()

                        lifecycleScope.launchWhenStarted {
                            delay(2000L)
                            val intent =
                                Intent(requireContext(), SignInFragment::class.java)
                            startActivity(intent)
                        }

                        //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()

                    } else {
                        ratingsStar()
                    }
                })
            }
        }
        bind.ratings2.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
                    if (it == null) {
                        Snackbar.make(
                            bind.ratings1,
                            "You must login first before you can save",
                            Snackbar.LENGTH_LONG
                        ).show()

                        lifecycleScope.launchWhenStarted {
                            delay(2000L)
                            val intent =
                                Intent(requireContext(), SignInFragment::class.java)
                            startActivity(intent)
                        }

                        //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()

                    } else {
                        ratingsStar()
                    }
                })
            }
        }
        bind.ratings3.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
                    if (it == null) {
                        Snackbar.make(
                            bind.ratings1,
                            "You must login first before you can save",
                            Snackbar.LENGTH_LONG
                        ).show()

                        lifecycleScope.launchWhenStarted {
                            delay(2000L)
                            val intent =
                                Intent(requireContext(), SignInFragment::class.java)
                            startActivity(intent)
                        }

                        //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()

                    } else {
                        ratingsStar()
                    }
                })
            }
        }
        bind.ratings4.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
                    if (it == null) {
                        Snackbar.make(
                            bind.ratings1,
                            "You must login first before you can save",
                            Snackbar.LENGTH_LONG
                        ).show()

                        lifecycleScope.launchWhenStarted {
                            delay(2000L)
                            val intent =
                                Intent(requireContext(), SignInFragment::class.java)
                            startActivity(intent)
                        }

                        //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()

                    } else {
                        ratingsStar()
                    }
                })
            }
        }
        bind.ratings5.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer {
                    if (it == null) {
                        Snackbar.make(
                            bind.ratings1,
                            "You must login first before you can save",
                            Snackbar.LENGTH_LONG
                        ).show()

                        lifecycleScope.launchWhenStarted {
                            delay(2000L)
                            val intent =
                                Intent(requireContext(), SignInFragment::class.java)
                            startActivity(intent)
                        }

                        //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()

                    } else {
                        ratingsStar()
                    }
                })
            }
        }


        viewModelClass.ratings.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> {
                    //1
                    val drawables = bind.ratings1.compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables[0] = newDrawable
                    bind.ratings1.setCompoundDrawablesWithIntrinsicBounds(
                        drawables[0],
                        drawables[1],
                        drawables[2],
                        drawables[3]
                    )
                    bind.ratings1.setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = bind.ratings2.compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables2[0] = newDrawable2
                    bind.ratings2.setCompoundDrawablesWithIntrinsicBounds(
                        drawables2[0],
                        drawables2[1],
                        drawables2[2],
                        drawables2[3]
                    )
                    bind.ratings2.setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = bind.ratings3.compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables3[0] = newDrawable3
                    bind.ratings3.setCompoundDrawablesWithIntrinsicBounds(
                        drawables3[0],
                        drawables3[1],
                        drawables3[2],
                        drawables3[3]
                    )
                    bind.ratings3.setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = bind.ratings4.compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables4[0] = newDrawable4
                    bind.ratings4.setCompoundDrawablesWithIntrinsicBounds(
                        drawables4[0],
                        drawables4[1],
                        drawables4[2],
                        drawables4[3]
                    )
                    bind.ratings4.setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = bind.ratings5.compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables5[0] = newDrawable5
                    bind.ratings5.setCompoundDrawablesWithIntrinsicBounds(
                        drawables5[0],
                        drawables5[1],
                        drawables5[2],
                        drawables5[3]
                    )
                    bind.ratings5.setPadding(6, 6, 6, 6)
                }
                1 -> {
                    //1
                    val drawables = bind.ratings1.compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.star_yellow)
                    drawables[0] = newDrawable
                    bind.ratings1.setCompoundDrawablesWithIntrinsicBounds(
                        drawables[0],
                        drawables[1],
                        drawables[2],
                        drawables[3]
                    )
                    bind.ratings1.setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = bind.ratings2.compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables2[0] = newDrawable2
                    bind.ratings2.setCompoundDrawablesWithIntrinsicBounds(
                        drawables2[0],
                        drawables2[1],
                        drawables2[2],
                        drawables2[3]
                    )
                    bind.ratings2.setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = bind.ratings3.compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables3[0] = newDrawable3
                    bind.ratings3.setCompoundDrawablesWithIntrinsicBounds(
                        drawables3[0],
                        drawables3[1],
                        drawables3[2],
                        drawables3[3]
                    )
                    bind.ratings3.setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = bind.ratings4.compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables4[0] = newDrawable4
                    bind.ratings4.setCompoundDrawablesWithIntrinsicBounds(
                        drawables4[0],
                        drawables4[1],
                        drawables4[2],
                        drawables4[3]
                    )
                    bind.ratings4.setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = bind.ratings5.compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables5[0] = newDrawable5
                    bind.ratings5.setCompoundDrawablesWithIntrinsicBounds(
                        drawables5[0],
                        drawables5[1],
                        drawables5[2],
                        drawables5[3]
                    )
                    bind.ratings5.setPadding(6, 6, 6, 6)
                }
                2 -> {
                    //1
                    val drawables = bind.ratings1.compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.star_yellow)
                    drawables[0] = newDrawable
                    bind.ratings1.setCompoundDrawablesWithIntrinsicBounds(
                        drawables[0],
                        drawables[1],
                        drawables[2],
                        drawables[3]
                    )
                    bind.ratings1.setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = bind.ratings2.compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.star_yellow)
                    drawables2[0] = newDrawable2
                    bind.ratings2.setCompoundDrawablesWithIntrinsicBounds(
                        drawables2[0],
                        drawables2[1],
                        drawables2[2],
                        drawables2[3]
                    )
                    bind.ratings2.setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = bind.ratings3.compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables3[0] = newDrawable3
                    bind.ratings3.setCompoundDrawablesWithIntrinsicBounds(
                        drawables3[0],
                        drawables3[1],
                        drawables3[2],
                        drawables3[3]
                    )
                    bind.ratings3.setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = bind.ratings4.compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables4[0] = newDrawable4
                    bind.ratings4.setCompoundDrawablesWithIntrinsicBounds(
                        drawables4[0],
                        drawables4[1],
                        drawables4[2],
                        drawables4[3]
                    )
                    bind.ratings4.setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = bind.ratings5.compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables5[0] = newDrawable5
                    bind.ratings5.setCompoundDrawablesWithIntrinsicBounds(
                        drawables5[0],
                        drawables5[1],
                        drawables5[2],
                        drawables5[3]
                    )
                    bind.ratings5.setPadding(6, 6, 6, 6)
                }
                3 -> {
                    //1
                    val drawables = bind.ratings1.compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.star_yellow)
                    drawables[0] = newDrawable
                    bind.ratings1.setCompoundDrawablesWithIntrinsicBounds(
                        drawables[0],
                        drawables[1],
                        drawables[2],
                        drawables[3]
                    )
                    bind.ratings1.setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = bind.ratings2.compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.star_yellow)
                    drawables2[0] = newDrawable2
                    bind.ratings2.setCompoundDrawablesWithIntrinsicBounds(
                        drawables2[0],
                        drawables2[1],
                        drawables2[2],
                        drawables2[3]
                    )
                    bind.ratings2.setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = bind.ratings3.compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.star_yellow)
                    drawables3[0] = newDrawable3
                    bind.ratings3.setCompoundDrawablesWithIntrinsicBounds(
                        drawables3[0],
                        drawables3[1],
                        drawables3[2],
                        drawables3[3]
                    )
                    bind.ratings3.setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = bind.ratings4.compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables4[0] = newDrawable4
                    bind.ratings4.setCompoundDrawablesWithIntrinsicBounds(
                        drawables4[0],
                        drawables4[1],
                        drawables4[2],
                        drawables4[3]
                    )
                    bind.ratings4.setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = bind.ratings5.compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables5[0] = newDrawable5
                    bind.ratings5.setCompoundDrawablesWithIntrinsicBounds(
                        drawables5[0],
                        drawables5[1],
                        drawables5[2],
                        drawables5[3]
                    )
                    bind.ratings5.setPadding(6, 6, 6, 6)
                }
                4 -> {
                    //1
                    val drawables = bind.ratings1.compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.star_yellow)
                    drawables[0] = newDrawable
                    bind.ratings1.setCompoundDrawablesWithIntrinsicBounds(
                        drawables[0],
                        drawables[1],
                        drawables[2],
                        drawables[3]
                    )
                    bind.ratings1.setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = bind.ratings2.compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.star_yellow)
                    drawables2[0] = newDrawable2
                    bind.ratings2.setCompoundDrawablesWithIntrinsicBounds(
                        drawables2[0],
                        drawables2[1],
                        drawables2[2],
                        drawables2[3]
                    )
                    bind.ratings2.setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = bind.ratings3.compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.star_yellow)
                    drawables3[0] = newDrawable3
                    bind.ratings3.setCompoundDrawablesWithIntrinsicBounds(
                        drawables3[0],
                        drawables3[1],
                        drawables3[2],
                        drawables3[3]
                    )
                    bind.ratings4.setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = bind.ratings4.compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.star_yellow)
                    drawables4[0] = newDrawable4
                    bind.ratings4.setCompoundDrawablesWithIntrinsicBounds(
                        drawables4[0],
                        drawables4[1],
                        drawables4[2],
                        drawables4[3]
                    )
                    bind.ratings4.setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = bind.ratings5.compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables5[0] = newDrawable5
                    bind.ratings5.setCompoundDrawablesWithIntrinsicBounds(
                        drawables5[0],
                        drawables5[1],
                        drawables5[2],
                        drawables5[3]
                    )
                    bind.ratings5.setPadding(6, 6, 6, 6)
                }
                5 -> {
                    //1
                    val drawables = bind.ratings1.compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.star_yellow)
                    drawables[0] = newDrawable
                    bind.ratings1.setCompoundDrawablesWithIntrinsicBounds(
                        drawables[0],
                        drawables[1],
                        drawables[2],
                        drawables[3]
                    )
                    bind.ratings1.setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = bind.ratings2.compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.star_yellow)
                    drawables2[0] = newDrawable2
                    bind.ratings2.setCompoundDrawablesWithIntrinsicBounds(
                        drawables2[0],
                        drawables2[1],
                        drawables2[2],
                        drawables2[3]
                    )
                    bind.ratings2.setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = bind.ratings3.compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.star_yellow)
                    drawables3[0] = newDrawable3
                    bind.ratings3.setCompoundDrawablesWithIntrinsicBounds(
                        drawables3[0],
                        drawables3[1],
                        drawables3[2],
                        drawables3[3]
                    )
                    bind.ratings3.setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = bind.ratings4.compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.star_yellow)
                    drawables4[0] = newDrawable4
                    bind.ratings4.setCompoundDrawablesWithIntrinsicBounds(
                        drawables4[0],
                        drawables4[1],
                        drawables4[2],
                        drawables4[3]
                    )
                    bind.ratings4.setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = bind.ratings5.compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.star_half_yellow)
                    drawables5[0] = newDrawable5
                    bind.ratings5.setCompoundDrawablesWithIntrinsicBounds(
                        drawables5[0],
                        drawables5[1],
                        drawables5[2],
                        drawables5[3]
                    )
                    bind.ratings5.setPadding(6, 6, 6, 6)
                }
            }
        })
    }

    fun ratingsStar() {
        val today = LocalDate.now()
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.ratings_dialog_layout)
        dialog.setCancelable(false)
        dialog.findViewById<TextView>(R.id.ratings1).setOnClickListener {
            if (viewModelClass.ratings.value == 1) {
                viewModelClass.ratings.value = 0
                rating!!.value = 0
            } else {
                viewModelClass.ratings.value = 1
                rating!!.value = 1
            }
        }
        dialog.findViewById<TextView>(R.id.ratings2).setOnClickListener {
            if (viewModelClass.ratings.value == 2) {
                viewModelClass.ratings.value = 0
                rating!!.value = 0
            } else {
                viewModelClass.ratings.value = 2
                rating!!.value = 2
            }
        }
        dialog.findViewById<TextView>(R.id.ratings3).setOnClickListener {
            if (viewModelClass.ratings.value == 3) {
                viewModelClass.ratings.value = 0
                rating!!.value = 0
            } else {
                viewModelClass.ratings.value = 3
                rating!!.value = 3
            }
        }
        dialog.findViewById<TextView>(R.id.ratings4).setOnClickListener {
            if (viewModelClass.ratings.value == 4) {
                viewModelClass.ratings.value = 0
                rating!!.value = 0
            } else {
                viewModelClass.ratings.value = 4
                rating!!.value = 4
            }
        }
        dialog.findViewById<TextView>(R.id.ratings5).setOnClickListener {
            if (viewModelClass.ratings.value == 5) {
                viewModelClass.ratings.value = 0
                rating!!.value = 0
            } else {
                viewModelClass.ratings.value = 5
                rating!!.value = 5
            }
        }
        dialog.findViewById<TextView>(R.id.cancel_ratings).setOnClickListener {
            viewModelClass.ratings.value = 0
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.continue_ratings).setOnClickListener {
            //Toast.makeText(requireContext(), "Click", Toast.LENGTH_LONG).show()
            rating!!.value = viewModelClass.ratings.value!!

            lifecycleScope.launchWhenStarted {
                viewModelClass.queryUserDetails().collect{ user->
                    // Toast.makeText(requireContext(), "${it}", Toast.LENGTH_LONG).show()
                    viewModelClass.productId.asFlow().collect{productId->
                        try {
                            if (user == null) {


                            } else {
                                Log.i("rt", "${user.id} --- ${productId}")

                                lifecycleScope.launchWhenStarted {
                                    //Toast.makeText(requireContext(), "${viewModelClass.productId.value!!.toInt()} == ${it.id}", Toast.LENGTH_LONG).show()
                                    getRatingsOfProduct(productId.toInt(), user.id!!)

                                    viewModelClass.updateProductRatingsTOServer(user.id!!, productId.toInt(), rating!!.value!!, today.toString())
                                }
                            }

                        } catch (e: Exception) { }
                    }
                }
            }


            dialog.dismiss()

            secondDialogue()
        }
        viewModelClass.ratings.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> {
                    //1
                    val drawables = dialog.findViewById<TextView>(R.id.ratings1).compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables[0] = newDrawable
                    dialog.findViewById<TextView>(R.id.ratings1)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[1],
                            drawables[2],
                            drawables[3])
                    dialog.findViewById<TextView>(R.id.ratings1).setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = dialog.findViewById<TextView>(R.id.ratings2).compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables2[0] = newDrawable2
                    dialog.findViewById<TextView>(R.id.ratings2)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables2[0],
                            drawables2[1],
                            drawables2[2],
                            drawables2[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings2).setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = dialog.findViewById<TextView>(R.id.ratings3).compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables3[0] = newDrawable3
                    dialog.findViewById<TextView>(R.id.ratings3)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables3[0],
                            drawables3[1],
                            drawables3[2],
                            drawables3[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings3).setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = dialog.findViewById<TextView>(R.id.ratings4).compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables4[0] = newDrawable4
                    dialog.findViewById<TextView>(R.id.ratings4)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables4[0],
                            drawables4[1],
                            drawables4[2],
                            drawables4[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings4).setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = dialog.findViewById<TextView>(R.id.ratings5).compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables5[0] = newDrawable5
                    dialog.findViewById<TextView>(R.id.ratings5)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables5[0],
                            drawables5[1],
                            drawables5[2],
                            drawables5[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings5).setPadding(6, 6, 6, 6)
                }
                1 -> {
                    //1
                    val drawables = dialog.findViewById<TextView>(R.id.ratings1).compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.star_yellow)
                    drawables[0] = newDrawable
                    dialog.findViewById<TextView>(R.id.ratings1)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[1],
                            drawables[2],
                            drawables[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings1).setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = dialog.findViewById<TextView>(R.id.ratings2).compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables2[0] = newDrawable2
                    dialog.findViewById<TextView>(R.id.ratings2)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables2[0],
                            drawables2[1],
                            drawables2[2],
                            drawables2[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings2).setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = dialog.findViewById<TextView>(R.id.ratings3).compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables3[0] = newDrawable3
                    dialog.findViewById<TextView>(R.id.ratings3)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables3[0],
                            drawables3[1],
                            drawables3[2],
                            drawables3[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings3).setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = dialog.findViewById<TextView>(R.id.ratings4).compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables4[0] = newDrawable4
                    dialog.findViewById<TextView>(R.id.ratings4)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables4[0],
                            drawables4[1],
                            drawables4[2],
                            drawables4[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings4).setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = dialog.findViewById<TextView>(R.id.ratings5).compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables5[0] = newDrawable5
                    dialog.findViewById<TextView>(R.id.ratings5)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables5[0],
                            drawables5[1],
                            drawables5[2],
                            drawables5[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings5).setPadding(6, 6, 6, 6)
                }
                2 -> {
                    //1
                    val drawables = dialog.findViewById<TextView>(R.id.ratings1).compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.star_yellow)
                    drawables[0] = newDrawable
                    dialog.findViewById<TextView>(R.id.ratings1)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[1],
                            drawables[2],
                            drawables[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings1).setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = dialog.findViewById<TextView>(R.id.ratings2).compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.star_yellow)
                    drawables2[0] = newDrawable2
                    dialog.findViewById<TextView>(R.id.ratings2)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables2[0],
                            drawables2[1],
                            drawables2[2],
                            drawables2[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings2).setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = dialog.findViewById<TextView>(R.id.ratings3).compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables3[0] = newDrawable3
                    dialog.findViewById<TextView>(R.id.ratings3)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables3[0],
                            drawables3[1],
                            drawables3[2],
                            drawables3[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings3).setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = dialog.findViewById<TextView>(R.id.ratings4).compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables4[0] = newDrawable4
                    dialog.findViewById<TextView>(R.id.ratings4)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables4[0],
                            drawables4[1],
                            drawables4[2],
                            drawables4[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings4).setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = dialog.findViewById<TextView>(R.id.ratings5).compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables5[0] = newDrawable5
                    dialog.findViewById<TextView>(R.id.ratings5)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables4[0],
                            drawables4[1],
                            drawables4[2],
                            drawables4[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings5).setPadding(6, 6, 6, 6)
                }
                3 -> {
                    //1
                    val drawables = dialog.findViewById<TextView>(R.id.ratings1).compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.star_yellow)
                    drawables[0] = newDrawable
                    dialog.findViewById<TextView>(R.id.ratings1)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[1],
                            drawables[2],
                            drawables[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings1).setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = dialog.findViewById<TextView>(R.id.ratings2).compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.star_yellow)
                    drawables2[0] = newDrawable2
                    dialog.findViewById<TextView>(R.id.ratings2)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables2[0],
                            drawables2[1],
                            drawables2[2],
                            drawables2[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings2).setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = dialog.findViewById<TextView>(R.id.ratings3).compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.star_yellow)
                    drawables3[0] = newDrawable3
                    dialog.findViewById<TextView>(R.id.ratings3)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables3[0],
                            drawables3[1],
                            drawables3[2],
                            drawables3[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings3).setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = dialog.findViewById<TextView>(R.id.ratings4).compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables4[0] = newDrawable4
                    dialog.findViewById<TextView>(R.id.ratings4)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables4[0],
                            drawables4[1],
                            drawables4[2],
                            drawables4[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings4).setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = dialog.findViewById<TextView>(R.id.ratings5).compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables5[0] = newDrawable5
                    dialog.findViewById<TextView>(R.id.ratings5)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables4[0],
                            drawables4[1],
                            drawables4[2],
                            drawables4[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings5).setPadding(6, 6, 6, 6)
                }
                4 -> {
                    //1
                    val drawables = dialog.findViewById<TextView>(R.id.ratings1).compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.star_yellow)
                    drawables[0] = newDrawable
                    dialog.findViewById<TextView>(R.id.ratings1)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[1],
                            drawables[2],
                            drawables[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings1).setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = dialog.findViewById<TextView>(R.id.ratings2).compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.star_yellow)
                    drawables2[0] = newDrawable2
                    dialog.findViewById<TextView>(R.id.ratings2)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables2[0],
                            drawables2[1],
                            drawables2[2],
                            drawables2[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings2).setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = dialog.findViewById<TextView>(R.id.ratings3).compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.star_yellow)
                    drawables3[0] = newDrawable3
                    dialog.findViewById<TextView>(R.id.ratings3)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables3[0],
                            drawables3[1],
                            drawables3[2],
                            drawables3[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings3).setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = dialog.findViewById<TextView>(R.id.ratings4).compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.star_yellow)
                    drawables4[0] = newDrawable4
                    dialog.findViewById<TextView>(R.id.ratings4)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables4[0],
                            drawables4[1],
                            drawables4[2],
                            drawables4[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings4).setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = dialog.findViewById<TextView>(R.id.ratings5).compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.ic_rating_star_outline_24)
                    drawables5[0] = newDrawable5
                    dialog.findViewById<TextView>(R.id.ratings5).setCompoundDrawablesWithIntrinsicBounds(
                        drawables5[0],
                        drawables5[1],
                        drawables5[2],
                        drawables5[3]
                    )
                    dialog.findViewById<TextView>(R.id.ratings5).setPadding(6, 6, 6, 6)
                }
                5 -> {
                    //1
                    val drawables = dialog.findViewById<TextView>(R.id.ratings1).compoundDrawables
                    val newDrawable = resources.getDrawable(R.drawable.star_yellow)
                    drawables[0] = newDrawable
                    dialog.findViewById<TextView>(R.id.ratings1)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[1],
                            drawables[2],
                            drawables[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings1).setPadding(6, 6, 6, 6)
                    //2
                    val drawables2 = dialog.findViewById<TextView>(R.id.ratings2).compoundDrawables
                    val newDrawable2 = resources.getDrawable(R.drawable.star_yellow)
                    drawables2[0] = newDrawable2
                    dialog.findViewById<TextView>(R.id.ratings2)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables2[0],
                            drawables2[1],
                            drawables2[2],
                            drawables2[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings2).setPadding(6, 6, 6, 6)
                    //3
                    val drawables3 = dialog.findViewById<TextView>(R.id.ratings3).compoundDrawables
                    val newDrawable3 = resources.getDrawable(R.drawable.star_yellow)
                    drawables3[0] = newDrawable3
                    dialog.findViewById<TextView>(R.id.ratings3)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables3[0],
                            drawables3[1],
                            drawables3[2],
                            drawables3[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings3).setPadding(6, 6, 6, 6)
                    //4
                    val drawables4 = dialog.findViewById<TextView>(R.id.ratings4).compoundDrawables
                    val newDrawable4 = resources.getDrawable(R.drawable.star_yellow)
                    drawables4[0] = newDrawable4
                    dialog.findViewById<TextView>(R.id.ratings4)
                        .setCompoundDrawablesWithIntrinsicBounds(
                            drawables4[0],
                            drawables4[1],
                            drawables4[2],
                            drawables4[3]
                        )
                    dialog.findViewById<TextView>(R.id.ratings4).setPadding(6, 6, 6, 6)
                    //5
                    val drawables5 = dialog.findViewById<TextView>(R.id.ratings5).compoundDrawables
                    val newDrawable5 = resources.getDrawable(R.drawable.star_yellow)
                    drawables5[0] = newDrawable5
                    dialog.findViewById<TextView>(R.id.ratings5).setCompoundDrawablesWithIntrinsicBounds(
                            drawables5[0],
                            drawables5[1],
                            drawables5[2],
                            drawables5[3])
                    dialog.findViewById<TextView>(R.id.ratings5).setPadding(6, 6, 6, 6)
                }
            }
        })

        dialog.show()
    }

    fun secondDialogue(){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.second_ratings_dialog_layout)
        dialog.setCancelable(false)
        dialog.findViewById<TextView>(R.id.continue_ratings).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }



    fun calculateDoubleRating(intRating: Int): Double {
        // Perform any necessary calculations or conversions here
        val doubleRating = intRating.toDouble() / 10.0 // Assuming the integer rating is on a scale of 1 to 10
        return doubleRating
    }

    suspend fun getRatingsOfProduct(product_id:Int, user_id:Int){
        delay(100L)

        try{
            val response = Singleton.Singleton.apiClient.api.ratingsOfProduct(product_id, user_id)
            val mResponse = response.body()
            if(response.isSuccessful)
            {
                if(response.code() == 200){
                    Log.i("logginnnng", "200 "+mResponse!!.size.toString())
                    viewModelClass.ratings.value = response.body()!![0].ratings
                }else{
                    Log.i("logginnnng", "300 "+mResponse!!.size.toString())
                }
            }else{
                Log.i("logginnnng", "error "+mResponse!!.size.toString())
            }
        }catch (e:Exception){}
    }
}