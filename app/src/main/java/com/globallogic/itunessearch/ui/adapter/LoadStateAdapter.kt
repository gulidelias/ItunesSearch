package com.globallogic.itunessearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.globallogic.itunessearch.databinding.LoadStateViewBinding

class LoadStateAdapter(private val retry: () -> Unit) :
    androidx.paging.LoadStateAdapter<LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        val progressbar = holder.loadStateViewBinding.loadStateProgress
        val buttonRetry = holder.loadStateViewBinding.loadStateRetry
        val textErrorMessage = holder.loadStateViewBinding.loadStateErrorMessage

        progressbar.isVisible = loadState is LoadState.Loading
        buttonRetry.isVisible = loadState !is LoadState.Loading
        textErrorMessage.isVisible = loadState !is LoadState.Loading

        if (loadState is LoadState.Error) {
            textErrorMessage.text = loadState.error.localizedMessage
        }

        buttonRetry.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LoadStateViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}

class LoadStateViewHolder(val loadStateViewBinding: LoadStateViewBinding) :
    ViewHolder(loadStateViewBinding.root)
