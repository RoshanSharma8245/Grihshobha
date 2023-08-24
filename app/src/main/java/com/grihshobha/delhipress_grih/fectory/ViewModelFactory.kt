package com.grihshobha.delhipress_grih.fectory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grihshobha.delhipress_grih.activity.home.ViewModelHome
import com.grihshobha.delhipress_grih.activity.login.ViewModelLogin
import com.grihshobha.delhipress_grih.api.ApiHelper

import kotlinx.coroutines.ExperimentalCoroutinesApi


class ViewModelFactory(private val apiHelper: ApiHelper) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelLogin::class.java)) {
            return ViewModelLogin(apiHelper) as T
        }

        else  if (modelClass.isAssignableFrom(ViewModelHome::class.java)) {
            return ViewModelHome(apiHelper) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }

}
