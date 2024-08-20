package com.kareem.flute

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.google.firebase.firestore.FirebaseFirestore
import com.kareem.flute.models.SongModel

object MyExoPlayer {
    private var exoPlayer: ExoPlayer? = null
    private var currentSong: SongModel? = null

    fun getCurrentSong(): SongModel? {
        return currentSong
    }


    fun getInstance(): ExoPlayer? {
        return exoPlayer?.apply {
            setAudioAttributes(AudioAttributes.DEFAULT, true)
        }

    }

    fun startPlayer(context: Context, song: SongModel) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
        if (currentSong != song) {
            currentSong = song
            updateCount()
            currentSong?.url?.apply {
                val mediaItem = MediaItem.fromUri(this)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()
            }
        }

    }

    private fun updateCount() {
        currentSong?.id?.let {id ->
            FirebaseFirestore.getInstance()
                .collection("songs")
                .document(id).get().addOnSuccessListener {
                    var latestCount = it.getLong("count")
                    if (latestCount == null) {
                        latestCount = 1L

                    } else {
                        latestCount += 1
                    }
                    FirebaseFirestore.getInstance()
                        .collection("songs")
                        .document(id).update("count", latestCount)
                }
        }
    }

}