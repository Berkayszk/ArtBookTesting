package com.example.artbooktesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.example.artbooktesting.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var dao: ArtDao
    @Inject
    lateinit var database: ArtDatabase

    @Before
    fun setup(){
        /*
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),ArtDatabase::class.java)
            .allowMainThreadQueries().build()

         */
        hiltRule.inject()
        dao = database.artDao()
    }
    @After
    fun teardown(){
        database.close()
    }

    @Test
    //runBlockingTest == runBlocking
    fun insertArtTesting() = runBlockingTest{
        val exampleArt = Art("Mona Lisa","DaVinci",1982,"test.com",1)
        dao.insertArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValue()
        assertThat(list).contains(exampleArt)
    }
    @Test
    fun deleteArtTesting() = runBlockingTest{
        val exampleArtTest = Art("Ahmet","Morty",1890,"testing.com",2)
        dao.insertArt(exampleArtTest)
        dao.delete(exampleArtTest)
        val listDelete = dao.observeArts().getOrAwaitValue()
        assertThat(listDelete).doesNotContain(exampleArtTest)


    }

}