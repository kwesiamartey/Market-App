package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.lornamobileappsdev.my_marketplace.adapter.CategoryListAdapter
import com.lornamobileappsdev.my_marketplace.databinding.ActivityCategoriesMenuBarsBinding
import com.lornamobileappsdev.my_marketplace.useCases.CustomItemAnimator
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel


class CategoryIconButtonFragment : Fragment() {

    lateinit var _bind: ActivityCategoriesMenuBarsBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity())[MyViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = ActivityCategoriesMenuBarsBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)


        bind.apply {
            backk.setOnClickListener {
                try {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(android.R.id.content, DashboardFragment()).addToBackStack(null)
                        .commit()
                } catch (e: Exception) {
                }
            }
        }

        categoryButtons()

        return bind.root
    }

    fun categoryButtons() {

        viewModelClass.catesListing.observe(viewLifecycleOwner, Observer { cate->
            val layoutManager = GridLayoutManager(requireContext(), 2)
            val customItemAnimator = CustomItemAnimator()
            bind.cateListingRecyclerview.itemAnimator = customItemAnimator
            layoutManager.isSmoothScrollbarEnabled = true
            layoutManager.reverseLayout = false
            bind.cateListingRecyclerview.layoutManager = layoutManager
            bind.cateListingRecyclerview.visibility = View.VISIBLE

            val adapter = CategoryListAdapter(
                requireContext(),
                cate,
                requireActivity(),
                viewModelClass
            )
            bind.cateListingRecyclerview.adapter = adapter
        })

        /*
bind.homeWare.setOnClickListener {
   viewModelClass.category.value = "HomeWare"
   lifecycleScope.launchWhenStarted { delay(2000L) }
   try {

       (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

   } catch (e: Exception) { }
}

bind.realEstate.setOnClickListener {
   viewModelClass.category.value = "Real Estate"
   lifecycleScope.launchWhenStarted { delay(2000L) }
   try {

       (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

   } catch (e: Exception) { }
}

bind.wristWatch.setOnClickListener {
   viewModelClass.category.value = "Wristwatches"
   lifecycleScope.launchWhenStarted { delay(2000L) }
   try {

       (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

   } catch (e: Exception) { }
}

bind.fishing.setOnClickListener {
   viewModelClass.category.value = "Fishing"
   lifecycleScope.launchWhenStarted { delay(2000L) }
   try {

       (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

   } catch (e: Exception) { }
}

bind.sneakers.setOnClickListener {
   viewModelClass.category.value = "Sneakers"
   lifecycleScope.launchWhenStarted { delay(2000L) }
   try {

       (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

   } catch (e: Exception) { }
}

bind.accessories.setOnClickListener {
       viewModelClass.category.value = "Accessories"
       lifecycleScope.launchWhenStarted { delay(2000L) }
       try {

           (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

       } catch (e: Exception) { }
   }

bind.smartPhones.setOnClickListener {
   viewModelClass.category.value = "Smartphones"
   lifecycleScope.launchWhenStarted { delay(2000L) }
   try {

       (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

   } catch (e: Exception) { }
}

bind.radio.setOnClickListener {
   viewModelClass.category.value = "Radio"
   lifecycleScope.launchWhenStarted { delay(2000L) }
   try {
       (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
   } catch (e: Exception) { }
}

bind.movies.setOnClickListener {
   viewModelClass.category.value = "Movies/Tv"
   lifecycleScope.launchWhenStarted { delay(2000L) }
   try {
       *//* val intent = Intent(requireContext(), CategoryFragment::class.java)
                 startActivity(intent)*//*
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

            } catch (e: Exception) {
            }
        }

        bind.restaurant.setOnClickListener {
            viewModelClass.category.value = "Restaurant"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.frozenFoods.setOnClickListener {
          iAds  viewModelClass.category.value = "Frozen Foods"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.vegtables.setOnClickListener {
            viewModelClass.category.value = "Vegetables/Farm"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.appliance.setOnClickListener {
            viewModelClass.category.value = "Appliance"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.health.setOnClickListener {

            viewModelClass.category.value = "Health"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.education.setOnClickListener {
            viewModelClass.category.value = "Education"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.business.setOnClickListener {
            viewModelClass.category.value = "Business"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.automobile.setOnClickListener {
            viewModelClass.category.value = "AutoMobile"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.clothing.setOnClickListener {
            viewModelClass.category.value = "Clothing/Fashion"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.electronic.setOnClickListener {
            viewModelClass.category.value = "Electronic"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.funiture.setOnClickListener {
            viewModelClass.category.value = "Furniture"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.appartment.setOnClickListener {
            viewModelClass.category.value = "Apartment"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.services.setOnClickListener {
            viewModelClass.category.value = "Service"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.jobs.setOnClickListener {
            viewModelClass.category.value = "Arts"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.animalPet.setOnClickListener {
            viewModelClass.category.value = "Animals/Pets"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.babiesKids.setOnClickListener {
            viewModelClass.category.value = "Kids and Babies"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.pharmacy.setOnClickListener {
            viewModelClass.category.value = "Pharmacy"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.everythingElse.setOnClickListener {
            viewModelClass.category.value = "Everything Else"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.grocery.setOnClickListener {
            viewModelClass.category.value = "Grocery"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) { }
        }

        bind.adult.setOnClickListener {
            viewModelClass.category.value = "Adult"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            val dialogue = AlertDialog.Builder(requireContext())
            dialogue.setTitle("Adult Page")
            dialogue.setMessage("This page contains adult materials which is not recommended for people under 18years. \nYou must be 18years and above to view this section. \n\n\"Are you sure you are above 18?\"")
            dialogue.setNegativeButton("No"){_,_ ->

            }
            dialogue.setPositiveButton("Yes"){_,_->
                try {
                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
                } catch (e: Exception) {
                }
            }
            dialogue.show()
        }

        bind.beauty.setOnClickListener {
            viewModelClass.category.value = "Beauty"
            lifecycleScope.launchWhenStarted { delay(2000L) }
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }*/
    }
}