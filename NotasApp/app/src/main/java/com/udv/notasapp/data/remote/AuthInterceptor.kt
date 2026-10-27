package com.udv.notasapp.data.remote

import com.udv.notasapp.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Agrega automáticamente el header "Authorization: Bearer <token>" a toda petición saliente,
 * usando el token guardado en la sesión. Así no hay que repetirlo en cada llamada.
 */
class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = sessionManager.obtenerToken()

        val request = if (!token.isNullOrBlank()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}
