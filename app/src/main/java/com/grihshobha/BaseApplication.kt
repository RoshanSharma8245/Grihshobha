package com.grihshobha


import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.conscent.framework.core.ConscentConfiguration
import com.conscent.framework.core.ConscentWrapper

class BaseApplication : MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        MultiDex.install(this)


        ConscentWrapper.configure(
            application = this,
            clientId = "6462098510fb035ffd1d91eb",
            colorAccent = Color.parseColor("#DD2163"),
            appMode = ConscentConfiguration.APP_MODE.DEBUG,
            apiMode = ConscentConfiguration.MODE.SANDBOX,
        )
    }
}