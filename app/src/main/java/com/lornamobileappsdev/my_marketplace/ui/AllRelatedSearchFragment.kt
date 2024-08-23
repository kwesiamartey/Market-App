package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lornamobileappsdev.my_marketplace.adapter.RelatedSearchListAdapter
import com.lornamobileappsdev.my_marketplace.databinding.FragmentAllRelatedSearchBinding
import com.lornamobileappsdev.my_marketplace.entity.RelatedSearchData
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel



class AllRelatedSearchFragment : Fragment() {

    lateinit var _bind: FragmentAllRelatedSearchBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }
    lateinit var layoutManager : LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentAllRelatedSearchBinding.inflate(layoutInflater)


        layoutManager = GridLayoutManager(requireContext(),2)
        layoutManager.orientation = GridLayoutManager.VERTICAL
        layoutManager.isSmoothScrollbarEnabled = true
        bind.recyclerRelatedSearchView.layoutManager = layoutManager

        bind.bk.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, DashboardFragment()).commit()
            /*val inent = Intent(requireContext(), DashboardFragment::class.java)
            startActivity(inent)*/
        }


        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar!!.hide()


        lifecycleScope.launchWhenStarted {
            viewModelClass.queryRelatedSeachDatatbase().asLiveData().observe(viewLifecycleOwner, Observer {
                try {
                    // Toast.makeText(requireContext(), "${it.id}", Toast.LENGTH_LONG).show()
                    if(it == null){
                        bind.recyclerRelatedSearchView.visibility = View.GONE
                    }else{
                        bind.recyclerRelatedSearchView.visibility = View.VISIBLE
                        try {
                            val adapter = RelatedSearchListAdapter(
                                requireContext(),
                                it as MutableList<RelatedSearchData>,
                                requireActivity(),
                                viewModelClass
                            )
                            bind.recyclerRelatedSearchView.adapter = adapter

                        } catch (e: Exception) { }
                    }

                } catch (e: Exception) {
                    Log.i("TAG", e.message.toString())
                }
            })
        }
    }
}