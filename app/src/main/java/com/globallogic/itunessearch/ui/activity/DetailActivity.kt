package com.globallogic.itunessearch.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
        selectedSong?.previewUrl?.let { addMP3(it) }
    }

    override fun onResume() {
        super.onResume()
        setupPlayer()
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
        Picasso.get().load(url).fit().into(binding.albumCover)
    }

    private fun setupRecycler() {
        detailAdapter = DetailAdapter(emptyList()) { song ->
            setupView(song)
            song.previewUrl?.let {
                addMP3(it)
                exoPlayer.play()
            }
        }
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
        styledPlayerView.useArtwork = true
        styledPlayerView.defaultArtwork
        exoPlayer.addListener(this)
    }

    private fun addMP3(mp3Url: String) {
        // Build the media item.
        val mediaItem = MediaItem.fromUri(mp3Url)
        exoPlayer.setMediaItem(mediaItem)
        // Prepare the player.
        exoPlayer.prepare()
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
