package au.edu.swin.sdmd.swinmealapp.services

import akathon.cos30017.swin_meal_backend.datamodel.SuggestMeal
import au.edu.swin.sdmd.swinmealapp.datamodels.MenuItem
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

class MenuItemServices {
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    fun getMenuItems(): List<MenuItem> {
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/menu/")
            .build()

        val response = httpClient.newCall(request).execute()
        println(response)
        if (!response.isSuccessful) {
            throw RuntimeException("Failed to get menu items")
        }

        val responseBody = response.body?.string()

        return jacksonObjectMapper().readValue(responseBody ?: "", object : TypeReference<List<MenuItem>>() {})
    }

    fun getMenuSuggest(): List<SuggestMeal> {
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/suggest-meals")
            .build()

        val response = httpClient.newCall(request).execute()
        println(response)
        if (!response.isSuccessful) {
            throw RuntimeException("Failed to get menu items")
        }

        val responseBody = response.body?.string()

        return jacksonObjectMapper().readValue(responseBody ?: "", object : TypeReference<List<SuggestMeal>>() {})
    }
}