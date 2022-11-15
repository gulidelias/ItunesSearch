package com.globallogic.itunessearch.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.globallogic.itunessearch.databinding.ActivityMainBinding
import com.globallogic.itunessearch.ui.adapter.Adapter
import com.globallogic.itunessearch.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var homeAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        setupView()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.liveDataSong.observe(this) {
            homeAdapter.listSong = it
            homeAdapter.notifyDataSetChanged()
        }
    }

    private fun setupView() {
        setupRecycler()
    }

    private fun setupObservers() {
        binding.searchButton.setOnClickListener {
            getInputText()
            hideKeyboard(it)
        }
    }

    private fun setupRecycler() {
        homeAdapter = Adapter(emptyList())
        binding.recyclerViewMain.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun getInputText() {
        mainViewModel.getSearchResult(binding.textInput.text.toString())
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
