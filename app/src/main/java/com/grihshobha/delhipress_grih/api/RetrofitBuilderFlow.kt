package com.grihshobha.delhipress_grih.api

import com.grihshobha.delhipress_grih.BuildConfig
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilderFlow {
    private val logging = HttpLoggingInterceptor()

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(AuthenticationInterceptor(
            "J1EFAQR-H0N4921-QCXKVNH-6W9ZYY9",
            "CFR472795Q42TTQJFV84M37A5G4SJ1EFAQRH0N4921QCXKVNH6W9ZYY9"))

    private fun getRetrofit(): Retrofit {
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        if (BuildConfig.DEBUG) httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl("https://stage.tsbdev.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }



    private fun getRetrofitGH(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://demo.grihshobha.in/wp-json/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientGH())
            .build()
    }

    private fun clientGH(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
    val apiServiceGH: ApiService = getRetrofitGH().create(ApiService::class.java)
}


class AuthenticationInterceptor(user: String, password: String) : Interceptor {

    private val credentials: String = Credentials.basic(user, password)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder().header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

}