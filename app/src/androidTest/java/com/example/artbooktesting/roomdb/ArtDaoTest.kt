package com.example.artbooktesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.example.artbooktesting.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@ExperimentalCoroutinesApi
class ArtDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var dao: ArtDao
    private lateinit var database: ArtDatabase

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),ArtDatabase::class.java)
            .allowMainThreadQueries().build()
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