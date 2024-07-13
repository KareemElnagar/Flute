package com.kareem.flute

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.initialize
import com.kareem.flute.adapter.CategoryAdapter
import com.kareem.flute.databinding.FragmentMainBinding
import com.kareem.flute.models.CategoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainFragment : Fragment(){
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: FragmentMainBinding
    private lateinit var categoryAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        Firebase.initialize(requireContext())
        db = FirebaseFirestore.getInstance()
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)
        // load categories from firestore
        lifecycleScope.launch(Dispatchers.IO) {
            getCategories()
        }


        // start songsList Fragment
        binding.root.setOnClickListener {
            val songsListFragment = SongsListFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, songsListFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }

    }

    private fun getCategories() {
        db.collection("category")
            .get().addOnSuccessListener {
                val categoryList = it.toObjects(CategoryModel::class.java)
                setupRecyclerView(categoryList)
            }
    }

    private fun setupRecyclerView(categoryList: List<CategoryModel>) {
        categoryAdapter = CategoryAdapter(categoryList)
        binding.categoriesRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRV.adapter = categoryAdapter
    }

}