package com.globallogic.itunessearch.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.globallogic.domain.entities.Song
import com.globallogic.itunessearch.databinding.DetailActivityBinding
import com.globallogic.itunessearch.ui.adapter.Adapter
import com.globallogic.itunessearch.ui.viewmodel.DetailViewModel
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: DetailActivityBinding
    private lateinit var detailAdapter: Adapter
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        val selectedSong: Song? = intent.extras?.getParcelable(ARG_SONG)
        setupView(selectedSong)
        setupRecycler()
        getSongsByAlbumId(selectedSong)
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.liveDataListOfSong.observe(this) {
            detailAdapter.listSong = it
            detailAdapter.notifyDataSetChanged()
        }
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

    private fun setupRecycler() {
        detailAdapter = Adapter(emptyList())
        binding.recyclerViewDetails.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
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
