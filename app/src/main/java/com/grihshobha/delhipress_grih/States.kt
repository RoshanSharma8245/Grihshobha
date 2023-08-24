package com.grihshobha.delhipress_grih

import com.grihshobha.delhipress_grih.models.response.conscent_token_response.ConcentTokenResponse
import com.grihshobha.delhipress_grih.models.response.stories_response.StoriesResponse


sealed class States{
    object Idle : States()

    sealed class HomeState: States(){
        object Loading : HomeState()
        data class Success(val response: StoriesResponse) : HomeState()
        data class Error(val errorMsg : String, val errorCode : Int) : HomeState()

    }
    sealed class LoginState: States(){
        object Loading : LoginState()
        data class Success(val response: ConcentTokenResponse) : LoginState()
        data class Error(val errorMsg : String, val errorCode : Int) : LoginState()

    }

}
