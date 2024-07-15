package com.kareem.flute

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kareem.flute.databinding.FragmentPlayerBinding

class PlayerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var exoPlayer: ExoPlayer
    var playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            showGif(isPlaying)
        }
    }

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
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        MyExoPlayer.getCurrentSong()?.apply {
            binding.songTitle.text = title
            binding.songSubtitle.text = subtitle

            Glide.with(binding.albumArt).load(coverUrl)
                .circleCrop()
                .into(binding.albumArt)
            Glide.with(binding.songGif).load(R.drawable.media_playing)
                .circleCrop()
                .into(binding.songGif)
            exoPlayer = MyExoPlayer.getInstance()!!
            binding.playerView.player = exoPlayer
            binding.playerView.showController()
            exoPlayer.addListener(playerListener)

        }
    }



    fun showGif(show: Boolean) {
        if (show) {
            binding.songGif.visibility = View.VISIBLE
        } else {
            binding.songGif.visibility = View.GONE
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlayerFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}