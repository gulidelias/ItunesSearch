package com.globallogic.itunessearch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.globallogic.domain.entities.Song
import com.globallogic.itunessearch.R
import com.globallogic.itunessearch.databinding.ItemViewAlbumSongBinding
import com.globallogic.itunessearch.databinding.ItemViewSongBinding
import com.globallogic.itunessearch.ui.activity.DetailActivity
import com.squareup.picasso.Picasso

class DetailAdapter(var listSong: List<Song>) :
    RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemViewAlbumSongBinding.bind(itemView)

        fun bind(song:  Song) {
            binding.songTitle.text = song.trackName
            binding.cardViewAlbumSong.setOnClickListener {
            }

            val url = song.imageCover
            Picasso.get()
                .load(url)
                .fit()
                .into(binding.albumCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_album_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSong[position])
    }

    override fun getItemCount(): Int = listSong.size
}
