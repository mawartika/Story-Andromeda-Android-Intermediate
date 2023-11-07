package com.example.storyandroidintermediate.data.story

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyandroidintermediate.R
import com.example.storyandroidintermediate.data.retrofit.DetailStoryResponse
import com.example.storyandroidintermediate.data.view.ViewModelFactoryStory
import com.example.storyandroidintermediate.databinding.ActivityStoryBinding
import com.example.storyandroidintermediate.data.retrofit.Result
import com.example.storyandroidintermediate.utils.convertDate2


class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private val viewModel by viewModels<StoryViewModel>(){
        ViewModelFactoryStory.getInstance(applicationContext)
    }

    private var id: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.detail_story)

        binding.tvdescBagianstory.movementMethod = ScrollingMovementMethod();

        id = intent.getStringExtra(ID)

        // masalah yang sama kenapa getdetailnya ga bisa nangkep loading, eror, succses//

        viewModel.getDetailStory(id.toString()).observe(this){item ->
            if(item != null){
                when(item){
                    is Result.Loading ->{
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val detailStory = item.data
                        setDetailStory(detailStory)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(item.error)
                    }
                }
            }
        }
    }

    private fun setDetailStory(detailstory: DetailStoryResponse){
        binding.previewimageBagianstory.visibility = View.VISIBLE
        binding.apply {
            Glide.with(previewimageBagianstory.context)
                .load(detailstory.story.photoUrl)
                .into(previewimageBagianstory)
        }

        binding.tvnameBagianactivitystory.visibility = View.VISIBLE
        binding.tvnameBagianactivitystory.text = detailstory.story.name
        binding.tvdescBagianstory.visibility = View.VISIBLE
        binding.tvdescBagianstory.text = detailstory.story.description
        binding.tvdescBagianstory.visibility = View.VISIBLE
        binding.tvdateBagianstory.visibility = View.VISIBLE
        binding.tvdateBagianstory.text = detailstory.story.createdAt.convertDate2()

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicatorBagianstory.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val ID = ""
    }
}