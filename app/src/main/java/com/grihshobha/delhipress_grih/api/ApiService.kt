package com.grihshobha.delhipress_grih.api
import com.grihshobha.delhipress_grih.models.request.ConcentLoginRequest
import com.grihshobha.delhipress_grih.models.response.conscent_token_response.ConcentTokenResponse
import com.grihshobha.delhipress_grih.models.response.stories_response.StoriesResponse
import retrofit2.http.*

interface ApiService {


    @POST("client/generate-temp-token")
    suspend fun getConcentToken(@Body body: ConcentLoginRequest): ConcentTokenResponse

    @GET("homepage")
    suspend fun getStories(): StoriesResponse

}