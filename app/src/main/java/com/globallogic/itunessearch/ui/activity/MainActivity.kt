package com.globallogic.itunessearch.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.globallogic.itunessearch.databinding.ActivityMainBinding
import com.globallogic.itunessearch.ui.adapter.LoadStateAdapter
import com.globallogic.itunessearch.ui.adapter.MainAdapter
import com.globallogic.itunessearch.ui.viewmodel.MainViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var homeMainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root
        supportActionBar?.hide()
        setContentView(rootView)
        setupObservers()
        setupPagingAdapter()
        stateLoaderHandler()
    }

    private fun setupObservers() {
        binding.textInput.doOnTextChanged { text, start, before, count ->
            val searchWord = binding.textInput.text.toString()
            binding.resultTitle.text = "Search result: $searchWord"
            lifecycleScope.launch {
                mainViewModel.getSearchResult(text.toString())
                    .collect { pagingDataSong ->
                        homeMainAdapter.submitData(pagingDataSong)
                    }
            }
        }
        binding.searchButton.setOnClickListener {
            hideKeyboard(it)
        }
    }

    private fun setupPagingAdapter() {
        homeMainAdapter = MainAdapter()
        binding.recyclerViewMain.apply {
            adapter = homeMainAdapter.withLoadStateFooter(
                footer = LoadStateAdapter {homeMainAdapter.retry()}
            )
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun stateLoaderHandler(){
        binding.buttonRetry.setOnClickListener {
            homeMainAdapter.retry()
        }
        homeMainAdapter.addLoadStateListener { loadStates ->
        if (loadStates.refresh is LoadState.Loading){
            binding.buttonRetry.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }else {
            binding.progressBar.visibility = View.GONE
            val errorState =when {
                loadStates.append is LoadState.Error -> loadStates.append as LoadState.Error
                loadStates.prepend is LoadState.Error -> loadStates.prepend as LoadState.Error
                loadStates.refresh is LoadState.Error -> {
                    binding.buttonRetry.visibility = View.VISIBLE
                    loadStates.refresh as LoadState.Error
                }
                else -> null
            }
            errorState?.let {
                Toast.makeText(this,it.error.message,Toast.LENGTH_SHORT).show()
            }
        }

        }
    }
}
