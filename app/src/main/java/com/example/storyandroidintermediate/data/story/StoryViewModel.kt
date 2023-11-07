package com.example.storyandroidintermediate.data.story

import androidx.lifecycle.ViewModel

class StoryViewModel (private val repository: StoryRepository) : ViewModel() {

    fun getDetailStory(id : String) = repository.getDetailStory(id)
}