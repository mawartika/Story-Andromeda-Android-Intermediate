package com.example.storyandroidintermediate.data

import android.content.Context
import com.example.storyandroidintermediate.data.pref.StoryPreference
import com.example.storyandroidintermediate.data.pref.dataStore
import com.example.storyandroidintermediate.data.retrofit.ApiConfig
import com.example.storyandroidintermediate.data.retrofit.ApiService
import com.example.storyandroidintermediate.data.story.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = StoryPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(pref,apiService)
    }
}