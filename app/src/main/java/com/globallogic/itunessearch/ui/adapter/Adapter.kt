package com.globallogic.itunessearch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.globallogic.domain.entities.Song
import com.globallogic.itunessearch.R
import com.globallogic.itunessearch.databinding.ItemViewSongBinding
import com.globallogic.itunessearch.ui.activity.DetailActivity
import com.squareup.picasso.Picasso

class Adapter(var listSong: List<Song>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemViewSongBinding.bind(itemView)

        fun bind(song:  Song) {
            binding.songArtist.text = song.artistName
            binding.songAlbum.text = song.collectionName
            binding.songTitle.text = song.trackName
            binding.cardviewsong.setOnClickListener {
                val intent = DetailActivity.getIntent(itemView.context, song)
                itemView.context.startActivity(intent)
            }

            val url = song.imageCover
            Picasso.get()
                .load(url)
                .fit().centerCrop()
                .into(binding.albumCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSong[position])
    }

    override fun getItemCount(): Int = listSong.size
}
