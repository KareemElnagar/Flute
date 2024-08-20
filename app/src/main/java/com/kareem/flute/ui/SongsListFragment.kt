package com.kareem.flute.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kareem.flute.adapter.SongsListAdapter
import com.kareem.flute.databinding.FragmentSongsListBinding
import com.kareem.flute.models.CategoryModel


class SongsListFragment : Fragment() {
    lateinit var category: CategoryModel
    private lateinit var binding: FragmentSongsListBinding
    private lateinit var songsListAdapter: SongsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSongsListBinding.inflate(inflater, container, false)
        binding.songTitle.text = category.name  // set song title
        Glide.with(binding.root).load(category.coverUrl)
            .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(32)))
            .into(binding.songImage)
        setupSongsListRecycler()

        binding.root.setOnClickListener {


        }
        return binding.root
    }

    private fun setupSongsListRecycler() {
        songsListAdapter = SongsListAdapter(category.songs)
        binding.songsListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.songsListRv.adapter = songsListAdapter


    }

}