package com.globallogic.itunessearch.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.globallogic.domain.entities.Song
import com.globallogic.itunessearch.databinding.DetailActivityBinding
import com.globallogic.itunessearch.ui.adapter.DetailAdapter
import com.globallogic.itunessearch.ui.viewmodel.DetailViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity(), Player.Listener {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var styledPlayerView: StyledPlayerView
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: DetailActivityBinding
    private lateinit var detailAdapter: DetailAdapter
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        progressBar = binding.progressBar

        val selectedSong: Song? = intent.extras?.getParcelable(ARG_SONG)
        setupView(selectedSong)
        setupRecycler()
        getSongsByAlbumId(selectedSong)
        setupPlayer()
        addMP3("https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/a2/d3/b3/a2d3b33f-e6b3-cbd8-6efb-fa9ecb9c668a/mzaf_16013347745276122340.plus.aac.p.m4a")

        if (savedInstanceState != null) {
            if (savedInstanceState.getInt("mediaItem") != 0) {
                val restoredMediaItem = savedInstanceState.getInt("mediaItem")
                val seekTime = savedInstanceState.getLong("SeekTime")
                exoPlayer.seekTo(restoredMediaItem, seekTime)
                exoPlayer.play()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupPlayer()
        addMP3("https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/a2/d3/b3/a2d3b33f-e6b3-cbd8-6efb-fa9ecb9c668a/mzaf_16013347745276122340.plus.aac.p.m4a")
        detailViewModel.liveDataListOfSong.observe(this) {
            detailAdapter.listSong = it
            detailAdapter.notifyDataSetChanged()
        }
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.release()
    }

    private fun getSongsByAlbumId(song: Song?) {
        song?.collectionId?.let { detailViewModel.getSongsByAlbum(it) }
    }

    private fun setupView(song: Song?) {
        binding.songArtist.text = song?.artistName
        binding.songAlbum.text = song?.collectionName
        binding.songTitle.text = song?.trackName
        val url = song?.imageCover
        Picasso.get()
            .load(url)
            .fit()
            .into(binding.albumCover)
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> {
                progressBar.visibility = View.VISIBLE
            }
            Player.STATE_READY -> {
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupRecycler() {
        detailAdapter = DetailAdapter(emptyList())
        binding.recyclerViewDetails.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupPlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        styledPlayerView = binding.exoPlayerView
        styledPlayerView.player = exoPlayer
        styledPlayerView.controllerShowTimeoutMs = 0

        exoPlayer.addListener(this)
    }

    private fun addMP3(mp3Url: String) {
        // Build the media item.
        val mediaItem = MediaItem.fromUri(mp3Url)
        exoPlayer.setMediaItem(mediaItem)
        // Set the media item to be played.
        exoPlayer.setMediaItem(mediaItem)
        // Prepare the player.
        exoPlayer.prepare()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // current play position
        outState.putLong("SeekTime", exoPlayer.currentPosition)
        // current mediaItem
        outState.putInt("mediaItem", exoPlayer.currentMediaItemIndex)
    }

    companion object {

        private const val ARG_SONG = "ARG_SONG"

        fun getIntent(context: Context, song: Song): Intent {
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(ARG_SONG, song)
            }
        }
    }
}
