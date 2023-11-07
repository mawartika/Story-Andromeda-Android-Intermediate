package com.example.storyandroidintermediate.data.Camera

import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.example.storyandroidintermediate.data.story.StoryRepository

class CameraHomeViewModel (private val storyRepository: StoryRepository): ViewModel(){
        fun postStory(file: MultipartBody.Part, description: RequestBody) = storyRepository.postStory(file, description)

    }