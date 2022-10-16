package com.example.artbooktesting.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.artbooktesting.Util.Resource
import com.example.artbooktesting.model.ImageResponse
import com.example.artbooktesting.roomdb.Art

class FakeArtRepositoryTest : ArtRepositoryInterface {
    private val arts = mutableListOf<Art>() //insert null list
    private  val artLiveData = MutableLiveData<List<Art>>(arts)

    override suspend fun insertArt(art: Art) {
        arts.add(art)
        refleshData()
    }

    override suspend fun deleteArt(art: Art) {
        arts.remove(art)
        refleshData()
    }

    override fun getArt(): LiveData<List<Art>> {
        return artLiveData
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
       return Resource.success(ImageResponse(listOf(),0,0))
    }
    private fun refleshData(){
        artLiveData.postValue(arts)
    }
}