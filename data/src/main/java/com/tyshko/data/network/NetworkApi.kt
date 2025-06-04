package com.tyshko.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import javax.inject.Inject

class NetworkApi @Inject constructor(private val client: HttpClient){
    suspend fun getUserIP() : String{
        return client.get("https://api.ipify.org/").bodyAsText()
    }
}