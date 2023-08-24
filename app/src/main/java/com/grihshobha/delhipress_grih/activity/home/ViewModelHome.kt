package com.grihshobha.delhipress_grih.activity.home

import com.grihshobha.delhipress_grih.api.ApiHelper
import com.grihshobha.delhipress_grih.States

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ViewModelHome(private val apiHelper: ApiHelper): ViewModel() {

    private val _mutableState = MutableStateFlow<States>(States.Idle)
    val state : StateFlow<States> = _mutableState


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }

    @DelicateCoroutinesApi
    fun getStories() {

        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            try {
                _mutableState.value = States.HomeState.Loading

                val response = withContext(Dispatchers.IO){ apiHelper.getStories()  }
                _mutableState.value = States.HomeState.Success(response)
            }catch (e:Exception){
                _mutableState.value = States.HomeState.Error(e.toString(),400)
            }
        }
    }


}