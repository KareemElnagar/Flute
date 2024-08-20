package com.kareem.flute.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.google.firebase.initialize
import com.kareem.flute.MyExoPlayer
import com.kareem.flute.R
import com.kareem.flute.adapter.CategoryAdapter
import com.kareem.flute.adapter.SectionSongListAdapter
import com.kareem.flute.databinding.FragmentMainBinding
import com.kareem.flute.models.CategoryModel
import com.kareem.flute.models.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: FragmentMainBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private val TAG = "MainFragment"
    private val songsListFragment = SongsListFragment()
    private val playerFragment = PlayerFragment()


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
            getSections("section_1", binding.Section1Title, binding.trendingRV, binding.Section1)
            getSections("section_2", binding.Section2Title, binding.section2RV, binding.Section2)
            setupMostPlayed("section_3", binding.Section3Title, binding.section3RV, binding.Section3)
        }


        // start songsList Fragment
        binding.root.setOnClickListener {
            replaceFragment(songsListFragment)

        }

    }

    override fun onResume() {
        super.onResume()
        showCurrentSong()
    }

    private fun showCurrentSong() {
        MyExoPlayer.getCurrentSong()?.let {
            binding.currentSong.visibility = View.VISIBLE
            binding.currentSongTitle.text = "Now Playing: " + it.title
            Glide.with(binding.currentSongImage).load(it.coverUrl)
                .apply(
                    RequestOptions().transform(CenterCrop())
                        .transform(RoundedCorners(32))
                )
                .into(binding.currentSongImage)
            binding.currentSong.setOnClickListener {
                replaceFragment(playerFragment)
            }
        } ?: run {
            binding.currentSong.visibility = View.GONE
        }
    }

    // replace fragment
    private fun replaceFragment(fragment: Fragment) {

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun getCategories() {
        db.collection("category")
            .get().addOnSuccessListener {
                val categoryList = it.toObjects(CategoryModel::class.java)
                setupRecyclerView(categoryList)
            }
    }

    // get sections
    private fun getSections(
        id: String,
        sectionTitle: TextView,
        recyclerView: RecyclerView,
        relativeLayout: RelativeLayout
    ) {
        db.collection("sections")
            .document(id)
            .get().addOnSuccessListener {
                val sectionList = it.toObject(CategoryModel::class.java)
                sectionList?.apply {
                    relativeLayout.visibility = View.VISIBLE
                    sectionTitle.text = name
                    recyclerView.adapter = SectionSongListAdapter(songs)
                    recyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    relativeLayout.setOnClickListener {
                        songsListFragment.category = sectionList
                        replaceFragment(songsListFragment)

                    }
                }
            }

    }

    private fun setupMostPlayed(
        id: String,
        sectionTitle: TextView,
        recyclerView: RecyclerView,
        relativeLayout: RelativeLayout
    ) {
        db.collection("sections")
            .document(id)
            .get().addOnSuccessListener {
                FirebaseFirestore.getInstance().collection("songs")
                    .orderBy("count", Query.Direction.DESCENDING)
                    .limit(5).get().addOnSuccessListener { songsListSnapShot ->
                        val songsModelList = songsListSnapShot.toObjects<SongModel>()
                        val songsIdList = songsModelList.map {
                            it.id
                        }.toList()

                        val sectionList = it.toObject(CategoryModel::class.java)
                        sectionList?.apply {
                            sectionList.songs = songsIdList
                            relativeLayout.visibility = View.VISIBLE
                            sectionTitle.text = name
                            recyclerView.adapter = SectionSongListAdapter(songs)
                            recyclerView.layoutManager =
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                            relativeLayout.setOnClickListener {
                                songsListFragment.category = sectionList
                                replaceFragment(songsListFragment)

                            }
                        }
                    }

            }

    }

    private fun setupRecyclerView(categoryList: List<CategoryModel>) {
        categoryAdapter = CategoryAdapter(categoryList)
        binding.categoriesRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRV.adapter = categoryAdapter
    }

}