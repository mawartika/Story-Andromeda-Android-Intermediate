package com.example.storyandroidintermediate.data.loginstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyandroidintermediate.data.story.StoryRepository
import com.example.storyandroidintermediate.data.pref.StoryModel
import kotlinx.coroutines.launch

class LoginViewModelStory (private val repository: StoryRepository) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)

    fun saveSession(user: StoryModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}