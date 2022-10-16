package com.example.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.artbooktesting.R
import com.example.artbooktesting.Util.Status
import com.example.artbooktesting.databinding.FragmentArtDetailsBinding
import com.example.artbooktesting.databinding.FragmentArtsBinding
import com.example.artbooktesting.viewmodel.ArtViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArtDetailsFragment @Inject constructor(
    val glide : RequestManager
) : Fragment(R.layout.fragment_art_details) {
    lateinit var viewModel : ArtViewModel
    private var fragmentBinding : FragmentArtDetailsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)
        val binding = FragmentArtDetailsBinding.bind(view)
        fragmentBinding = binding
        subscribeToObservers()
        binding.artImageView.setOnClickListener{
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageAPIFragment())
        }
        val callBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callBack)

        binding.saveButton.setOnClickListener {
            viewModel.makeArt(binding.artistName.text.toString(),binding.nameText.text.toString(),binding.yearText.text.toString())
        }

    }
    private fun subscribeToObservers(){
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer { url ->
            print(url)
            fragmentBinding?.let {
                glide.load(url).into(it.artImageView)
            }
        })
        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireActivity(), "Succes!!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    viewModel.resetInsertArtMsg() //Insert message reset
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message ?:"Error!!", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {

                }
            }
        })
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}