package com.kareem.flute

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
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
            currentSong?.url?.apply {
                val mediaItem = MediaItem.fromUri(this)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()
            }
        }

    }

}