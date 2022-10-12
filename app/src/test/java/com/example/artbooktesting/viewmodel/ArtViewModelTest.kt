package com.example.artbooktesting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.artbooktesting.MainCoroutineRule
import com.example.artbooktesting.Util.Status
import com.example.artbooktesting.getOrAwaitValueTest
import com.example.artbooktesting.repo.ArtRepository
import com.example.artbooktesting.repo.FakeArtRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {
    private lateinit var viewModel: ArtViewModel
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup(){
        //Test Double
        viewModel = ArtViewModel(FakeArtRepository())
    }
    @Test
    fun `insert art without year returns error`(){
        viewModel.makeArt("Mona Lisa","Davinci","")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)

    }
    @Test
    fun `insert art without name returns error`(){
        viewModel.makeArt("","Murat","1234")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `insert art without artistName returns error`(){
        viewModel.makeArt("Rick Morty","","3456")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

}