package com.example.artbooktesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.example.artbooktesting.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import com.example.artbooktesting.R
import com.example.artbooktesting.getOrAwaitValue
import com.example.artbooktesting.repo.FakeArtRepositoryTest
import com.example.artbooktesting.roomdb.Art
import com.example.artbooktesting.viewmodel.ArtViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtDetailsFragmentTest {

    @get:Rule
    var hiltrule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup(){
        hiltrule.inject()
    }
    @Test
    fun testNavigationFromArtDetailsFragmentToImageAPI(){
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<ArtDetailsFragment>(factory = fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
        }
        Espresso.onView(ViewMatchers.withId(R.id.artDetailsFragment)).perform(click())
        Mockito.verify(navController).navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageAPIFragment())
    }
    @Test
    fun testOnBackPress(){
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(factory = fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
        }
        Espresso.pressBack()
        Mockito.verify(navController).popBackStack()
    }
    @Test
    fun testSave(){
        val testViewModel = ArtViewModel(FakeArtRepositoryTest())
        launchFragmentInHiltContainer<ArtDetailsFragment>(factory = fragmentFactory){
            viewModel = testViewModel
        }
        Espresso.onView(ViewMatchers.withId(R.id.nameText)).perform(replaceText("Lisa"))
        Espresso.onView(ViewMatchers.withId(R.id.artistName)).perform(replaceText("Chemical Lona"))
        Espresso.onView(ViewMatchers.withId(R.id.yearText)).perform(replaceText("1452"))
        Espresso.onView(ViewMatchers.withId(R.id.saveButton)).perform(click())

        assertThat(testViewModel.artList.getOrAwaitValue()).contains(
            Art("Lisa","Chemical Lona",1452,"",null)
        )
    }
}