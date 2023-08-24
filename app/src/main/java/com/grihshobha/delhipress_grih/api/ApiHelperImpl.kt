package com.grihshobha.delhipress_grih.api

import com.grihshobha.delhipress_grih.models.request.ConcentLoginRequest
import com.grihshobha.delhipress_grih.models.response.conscent_token_response.ConcentTokenResponse
import com.grihshobha.delhipress_grih.models.response.stories_response.StoriesResponse


class ApiHelperImpl (private val apiService: ApiService) : ApiHelper {


    override suspend fun getConcentToken(body: ConcentLoginRequest): ConcentTokenResponse {
        return apiService.getConcentToken(body)
    }

    override suspend fun getStories(): StoriesResponse {
        return apiService.getStories()
    }

}