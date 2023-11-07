package com.example.storyandroidintermediate.data.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyandroidintermediate.data.story.StoryRepository
import com.example.storyandroidintermediate.data.pref.StoryModel
import kotlinx.coroutines.launch

class MainViewModelStory (private val repository: StoryRepository) : ViewModel () {
    fun getSession(): LiveData<StoryModel> {
        return repository.getSession().asLiveData()

    }

    fun getStories() = repository.getStory()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}