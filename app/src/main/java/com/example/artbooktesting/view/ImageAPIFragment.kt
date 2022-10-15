package com.example.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.example.artbooktesting.R
import com.example.artbooktesting.Util.Status
import com.example.artbooktesting.adapter.ImageRecyclerAdapter
import com.example.artbooktesting.databinding.FragmentImageApiBinding
import com.example.artbooktesting.viewmodel.ArtViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class ImageAPIFragment @Inject constructor(
    private val imageRecyclerAdapter: ImageRecyclerAdapter
): Fragment(R.layout.fragment_image_api) {

    lateinit var viewModel : ArtViewModel
    private var fragmentBinding : FragmentImageApiBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)
        subscribeToObservers()
        val binding = FragmentImageApiBinding.bind(view)
        fragmentBinding = binding

        binding.imageRecyclerView.adapter = imageRecyclerAdapter
        binding.imageRecyclerView.layoutManager = GridLayoutManager(requireContext(),3) //images grid count=3
        var job: Job? = null
        binding.searchText.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if (it.toString().isNotEmpty()){
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }

        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)
        }
    }
    private fun subscribeToObservers(){
        viewModel.imageList.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS -> {
                   val urls = it.data?.hits?.map { imageResult -> imageResult.previewURL  } //map: Transform hits to totalHits
                    imageRecyclerAdapter.images = urls ?: listOf()

                    fragmentBinding?.progressBar?.visibility = View.GONE
                }
                Status.LOADING ->{
                    Toast.makeText(requireContext(), it.message ?: "Loading...", Toast.LENGTH_SHORT).show()
                    fragmentBinding?.progressBar?.visibility = View.VISIBLE
                }
                Status.ERROR ->{
                    fragmentBinding?.progressBar?.visibility = View.GONE
                }
            }
        })
    }
}