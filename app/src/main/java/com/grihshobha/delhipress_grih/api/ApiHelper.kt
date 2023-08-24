package com.grihshobha.delhipress_grih.api

import com.grihshobha.delhipress_grih.models.request.ConcentLoginRequest
import com.grihshobha.delhipress_grih.models.response.conscent_token_response.ConcentTokenResponse
import com.grihshobha.delhipress_grih.models.response.stories_response.StoriesResponse


interface ApiHelper {

    suspend fun getConcentToken(body: ConcentLoginRequest): ConcentTokenResponse
    suspend fun getStories(): StoriesResponse


}