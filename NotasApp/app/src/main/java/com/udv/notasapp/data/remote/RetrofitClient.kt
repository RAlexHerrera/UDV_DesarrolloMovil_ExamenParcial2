package com.udv.notasapp.data.remote

import com.udv.notasapp.data.local.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Backend local (NotasApi) corriendo en la misma PC.
    // - Emulador de Android Studio: 10.0.2.2 apunta al localhost de la PC anfitriona.
    // - Dispositivo físico en la misma red Wi-Fi: cambiar por la IP de la PC que imprime
    //   NotasApi al arrancar (ej. "http://192.168.1.50:3000/").
    private const val BASE_URL = "http://10.0.2.2:3000/"

    fun create(sessionManager: SessionManager): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
