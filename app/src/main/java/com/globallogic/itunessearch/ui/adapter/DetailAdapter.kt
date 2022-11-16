package com.globallogic.itunessearch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.globallogic.domain.entities.Song
import com.globallogic.itunessearch.R
import com.globallogic.itunessearch.databinding.ItemViewAlbumSongBinding
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class DetailAdapter(var listSong: List<Song>, private val onSongClicked: (Song) -> Unit) :
    RecyclerView.Adapter<SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_album_song, parent, false)
        return SongViewHolder(view, onSongClicked)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(listSong[position])
    }

    override fun getItemCount(): Int = listSong.size
}


class SongViewHolder(itemView: View, private val onSongClicked: (Song) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemViewAlbumSongBinding.bind(itemView)

    fun bind(song:  Song) {
        binding.songTitle.text = song.trackName
        binding.cardViewAlbumSong.setOnClickListener {
            onSongClicked(song)
        }
        val url = song.imageCover
        Picasso.get()
            .load(url)
            .fit()
            .transform(RoundedCornersTransformation(16, 0))
            .into(binding.albumCover)
    }
}
