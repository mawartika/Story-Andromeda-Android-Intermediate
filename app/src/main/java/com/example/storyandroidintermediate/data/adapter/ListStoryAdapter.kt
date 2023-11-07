package com.example.storyandroidintermediate.data.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyandroidintermediate.data.retrofit.ListStoryItem
import com.example.storyandroidintermediate.data.story.StoryActivity
import com.example.storyandroidintermediate.databinding.StoryItemBinding
import com.example.storyandroidintermediate.utils.convertDate


class ListStoryAdapter (private val context: Context) : ListAdapter<ListStoryItem, ListStoryAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
        holder.itemView.setOnClickListener {
            val moveDataUserIntent = Intent(holder.itemView.context, StoryActivity::class.java)
            moveDataUserIntent.putExtra(StoryActivity.ID, story.id)
            holder.itemView.context.startActivity(moveDataUserIntent)
        }

    }
    class MyViewHolder(val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem){
            binding.nameTextViewBagianstory.text = story.name
            binding.dateTextViewBagianstory.text = story.createdAt.convertDate()
            Glide.with(binding.root.context).load(story.photoUrl)
                .into(binding.storyImageView)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}
