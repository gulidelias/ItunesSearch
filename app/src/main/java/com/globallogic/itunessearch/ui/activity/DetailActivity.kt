package com.globallogic.itunessearch.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.globallogic.domain.entities.Song
import com.globallogic.itunessearch.databinding.DetailActivityBinding
import com.globallogic.itunessearch.ui.adapter.DetailAdapter
import com.globallogic.itunessearch.ui.viewmodel.DetailViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var styledPlayerView: StyledPlayerView
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: DetailActivityBinding
    private lateinit var detailAdapter: DetailAdapter
    private val exoPlayer: ExoPlayer by inject()
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        val rootView = binding.root
        supportActionBar?.hide()
        setContentView(rootView)
        progressBar = binding.progressBar

        val selectedSong: Song? = intent.extras?.getParcelable(ARG_SONG)
        setupView(selectedSong)
        setupRecycler()
        getSongsByAlbumId(selectedSong)
        setupPlayerView()

        detailViewModel.liveDataListOfSong.observe(this) {
            detailAdapter.listSong = it
            detailAdapter.notifyDataSetChanged()
        }

        detailViewModel.liveDataError.observe(this) { text ->
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStop() {
        super.onStop()
        detailViewModel.onStop()
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
            .transform(RoundedCornersTransformation(16, 2))
            .into(binding.albumCover)
    }

    private fun setupRecycler() {
        detailAdapter = DetailAdapter(emptyList()) { song ->
            setupView(song)
            song.previewUrl?.let {
                detailViewModel.onSongClicked(it)
            }
        }
        binding.recyclerViewDetails.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupPlayerView() {
        styledPlayerView = binding.exoPlayerView
        styledPlayerView.player = exoPlayer
        styledPlayerView.controllerShowTimeoutMs = 0
        styledPlayerView.showController()
        styledPlayerView.controllerHideOnTouch = false
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
