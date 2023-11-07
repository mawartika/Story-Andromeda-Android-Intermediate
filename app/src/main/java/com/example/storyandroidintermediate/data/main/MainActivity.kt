package com.example.storyandroidintermediate.data.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyandroidintermediate.R
import com.example.storyandroidintermediate.data.retrofit.Result
import com.example.storyandroidintermediate.data.Camera.CameraHomeActivity
import com.example.storyandroidintermediate.data.adapter.ListStoryAdapter
import com.example.storyandroidintermediate.data.loginstory.LoginStoryActivity
import com.example.storyandroidintermediate.data.retrofit.ListStoryItem
import com.example.storyandroidintermediate.data.view.ViewModelFactoryStory
import com.example.storyandroidintermediate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModelStory>{
        ViewModelFactoryStory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.home_story)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginStoryActivity::class.java))
                finish()
            } else {
                getStory()
                newStory()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStoryAndromeda.layoutManager = layoutManager

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                getStory()
            }
            R.id.menu2 -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.menu3 -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getStory(){
        viewModel.getStories().observe(this){story ->
            if(story != null){
                when(story){
                    is Result.Loading ->{
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val listStory = story.data
                        storyAdapter(listStory)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(story.error)
                    }
                }
            }
        }
    }

    //why submit listnya eror??//
    private fun storyAdapter(listStory: List<ListStoryItem>) {
        val adapter = ListStoryAdapter(this@MainActivity)
        adapter.submitList(listStory)
        binding.rvStoryAndromeda.adapter = adapter
    }

    private fun newStory() {
        binding.buttonNewStory.setOnClickListener {
            startActivity(Intent(this, CameraHomeActivity::class.java))
        }
    }

    private fun logout() {
        viewModel.logout()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicatorBagianstory.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}