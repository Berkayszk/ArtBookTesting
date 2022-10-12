package com.example.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.example.artbooktesting.Util.Resource
import com.example.artbooktesting.model.ImageResponse
import com.example.artbooktesting.roomdb.Art

interface ArtRepositoryInterface {
    suspend fun insertArt (art: Art)

    suspend fun deleteArt(art: Art)

    fun getArt() : LiveData<List<Art>>

    suspend fun searchImage(imageString: String) : Resource<ImageResponse>
}