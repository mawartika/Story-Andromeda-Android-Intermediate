package com.example.storyandroidintermediate.data.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyandroidintermediate.data.Camera.CameraHomeViewModel
import com.example.storyandroidintermediate.data.Injection
import com.example.storyandroidintermediate.data.story.StoryRepository
import com.example.storyandroidintermediate.data.loginstory.LoginViewModelStory
import com.example.storyandroidintermediate.data.main.MainViewModelStory
import com.example.storyandroidintermediate.data.story.StoryViewModel
import com.example.storyandroidintermediate.data.view.signupstory.SignupViewModel



class ViewModelFactoryStory (private val repository: StoryRepository) : ViewModelProvider.NewInstanceFactory (){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModelStory::class.java) -> {
                LoginViewModelStory(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModelStory::class.java) -> {
                MainViewModelStory(repository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CameraHomeViewModel::class.java) -> {
                CameraHomeViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryStory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactoryStory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactoryStory::class.java) {
                    INSTANCE = ViewModelFactoryStory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactoryStory
        }
    }
}