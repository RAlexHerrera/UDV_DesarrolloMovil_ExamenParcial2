package com.udv.notasapp.data.remote

import com.udv.notasapp.data.remote.dto.LoginRequest
import com.udv.notasapp.data.remote.dto.LoginResponse
import com.udv.notasapp.data.remote.dto.Nota
import com.udv.notasapp.data.remote.dto.NotaRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Contrato de la API consumida por la app, implementado y verificado en NotasApi
 * (carpeta hermana ../NotasApi). Ver NotasApi/README.md para correr el backend.
 */
interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/notas")
    suspend fun crearNota(@Body request: NotaRequest): Response<Nota>

    @GET("api/notas")
    suspend fun obtenerNotas(): Response<List<Nota>>
}
