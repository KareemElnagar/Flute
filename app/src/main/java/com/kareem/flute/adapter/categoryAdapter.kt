package com.kareem.flute.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kareem.flute.R
import com.kareem.flute.ui.SongsListFragment
import com.kareem.flute.databinding.CategoryItemRecyclerRowBinding
import com.kareem.flute.models.CategoryModel

class CategoryAdapter(private val categoryList: List<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: CategoryItemRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryModel) {
            binding.categoryNameTv.text = category.name
            Glide.with(binding.root).load(category.coverUrl)
                .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(32)))
                .into(binding.coverImage)
            Log.i("SONGS", category.songs.size.toString())

            binding.root.setOnClickListener{
                val songsListFragment = SongsListFragment()
                songsListFragment.category = category
                val transaction = (binding.root.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_layout, songsListFragment)
                transaction.addToBackStack(null)
                transaction.commit()

            }


        }
    }
    interface OnItemClickListener {
        fun onItemClick(item: CategoryModel) // Pass any relevant data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CategoryItemRecyclerRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }
}