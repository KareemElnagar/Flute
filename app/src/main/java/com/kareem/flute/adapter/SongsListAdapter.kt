package com.kareem.flute.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.kareem.flute.MainActivity
import com.kareem.flute.MyExoPlayer
import com.kareem.flute.ui.PlayerFragment
import com.kareem.flute.R
import com.kareem.flute.databinding.SongListRecyclerRowBinding
import com.kareem.flute.models.SongModel

class SongsListAdapter(private val songIdList: List<String>) :
    RecyclerView.Adapter<SongsListAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: SongListRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // bind data
        fun bind(songId: String) {
            FirebaseFirestore.getInstance().collection("songs")
                .document(songId).get()
                .addOnSuccessListener {
                    val song = it.toObject(SongModel::class.java)
                    song?.apply {

                        binding.songTitleTv.text = song.title
                        binding.songSubtitleTv.text = song.subtitle
                        Glide.with(binding.songCoverIv).load(song.coverUrl)
                            .apply(
                                RequestOptions().transform(CenterCrop())
                                    .transform(RoundedCorners(32))
                            )
                            .into(binding.songCoverIv)

                        binding.root.setOnClickListener {
                            MyExoPlayer.startPlayer(binding.root.context, song)
                            val playerFragment = PlayerFragment()
                            val transaction =
                                (binding.root.context as MainActivity).supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.frame_layout, playerFragment)
                            transaction.addToBackStack(null)
                            transaction.commit()

                        }
                    }

                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            SongListRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songIdList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(songIdList[position])
    }
}