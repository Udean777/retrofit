package com.example.testretrofit.data

import com.example.testretrofit.models.CatFact
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/fact")
    suspend fun getRandomFacts(): Response<CatFact>
}