package com.globallogic.itunessearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.globallogic.domain.entities.Song
import com.globallogic.itunessearch.databinding.ItemViewSongBinding
import com.globallogic.itunessearch.ui.activity.DetailActivity
import com.globallogic.itunessearch.ui.activity.MainActivity
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class MainAdapter :
    PagingDataAdapter<Song, SearchViewHolder>(SongDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemViewSongBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class SongDiffCallBack : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}

class SearchViewHolder(private val binding: ItemViewSongBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(song: Song) {
        binding.songArtist.text = song.artistName
        binding.songAlbum.text = song.collectionName
        binding.songTitle.text = song.trackName
        binding.cardViewSong.setOnClickListener {
            val intent = DetailActivity.getIntent(itemView.context, song)
            itemView.context.startActivity(intent)
        }

        val url = song.imageCover
        Picasso.get()
            .load(url)
            .fit()
            .transform(RoundedCornersTransformation(25, 0))
            .into(binding.albumCover)
    }
}
