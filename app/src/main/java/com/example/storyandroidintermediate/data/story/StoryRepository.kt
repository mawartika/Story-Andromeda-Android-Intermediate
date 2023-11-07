package com.example.storyandroidintermediate.data.story


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyandroidintermediate.data.pref.StoryModel
import com.example.storyandroidintermediate.data.pref.StoryPreference
import com.example.storyandroidintermediate.data.retrofit.ApiConfig
import com.example.storyandroidintermediate.data.retrofit.ApiService
import com.example.storyandroidintermediate.data.retrofit.DetailStoryResponse
import com.example.storyandroidintermediate.data.retrofit.ListStoryItem
import com.example.storyandroidintermediate.data.retrofit.LoginResponse
import com.example.storyandroidintermediate.data.retrofit.SignUpResponse
import com.example.storyandroidintermediate.data.retrofit.Result
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository private constructor(private val storyPreference: StoryPreference, private val apiService: ApiService) {

    fun signup(name: String, email: String, password: String): LiveData<Result<SignUpResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.signup(name, email, password)
            if(response.error ==false){
                emit(Result.Success(response))
            }else{
                emit(Result.Error(response.message ?: "Eror"))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, SignUpResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Sign Up Failed: $errorMessage"))
        } catch (e: Exception){
            emit(Result.Error("Something Error"))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            if(response.error ==false){
                val user = StoryModel(
                    email = email,
                    token = response.loginResult?.token ?: "Loading",
                    isLogin = true
                )
                ApiConfig.token = response.message ?: "Success"
                storyPreference.saveSession(user)
                emit(Result.Success(response))
            }else{
                emit(Result.Error(response.message ?: "Eror"))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Login Failed: $errorMessage"))
        } catch (e: Exception){
            emit(Result.Error("Something Error"))
        }
    }

    fun getStory(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            var storyList: List<ListStoryItem>
            val user = runBlocking { storyPreference.getSession().first() }
            val response = ApiConfig.getApiService(user.token)
            val storyResponse =response.getStories()
            storyList = storyResponse.listStory

            if (storyResponse.error == false) {
                emit(Result.Success(storyList))
            } else {
                emit(Result.Error(storyResponse.message))
            }
        }catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Cannot Get Stories: $errorMessage"))
        } catch (e: Exception){
            emit(Result.Error("Something Error"))
        }
    }

    fun getDetailStory(id : String): LiveData<Result<DetailStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val user = runBlocking { storyPreference.getSession().first() }
            val response = ApiConfig.getApiService(user.token)
            val detailStoryResponse =response.getDetailStory(id)

            if (detailStoryResponse !=null) {
                emit(Result.Success(detailStoryResponse))
            } else {
                emit(Result.Error(detailStoryResponse.message))
            }
        }catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error("Cannot Get Stories: $errorMessage"))
        } catch (e: Exception){
            emit(Result.Error("Something Error"))
        }
    }

    fun postStory(file: MultipartBody.Part, description: RequestBody): LiveData<Result<NewStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val user = runBlocking { storyPreference.getSession().first() }
            val response = ApiConfig.getApiService(user.token)
            val responsedetail = response.createStory(file, description)
            emit(Result.Success(responsedetail))
        } catch (e: Exception) {
            Log.e("CreateStoryViewModel", "postStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveSession(user: StoryModel) {
        storyPreference.saveSession(user)
    }

    fun getSession(): Flow<StoryModel> {
        return storyPreference.getSession()
    }

    suspend fun logout() {
        storyPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyPreference: StoryPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyPreference, apiService)
            }.also { instance = it }
    }
}
