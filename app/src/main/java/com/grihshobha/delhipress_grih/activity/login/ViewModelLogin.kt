package com.grihshobha.delhipress_grih.activity.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grihshobha.delhipress_grih.States
import com.grihshobha.delhipress_grih.api.ApiHelper
import com.grihshobha.delhipress_grih.models.request.ConcentLoginRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ViewModelLogin(private val apiHelper: ApiHelper): ViewModel() {

    private val _mutableState = MutableStateFlow<States>(States.Idle)
    val state : StateFlow<States> = _mutableState


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }

    @DelicateCoroutinesApi
    fun getConcentToken(body: ConcentLoginRequest) {

        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            try {
                _mutableState.value = States.LoginState.Loading

                val response = withContext(Dispatchers.IO){ apiHelper.getConcentToken(body)  }
                _mutableState.value = States.LoginState.Success(response)
            }catch (e:Exception){
                _mutableState.value = States.LoginState.Error(e.toString(), 400)
            }
        }
    }


}