package com.example.storyandroidintermediate.data.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.storyandroidintermediate.R
import com.example.storyandroidintermediate.data.retrofit.ApiConfig
import com.example.storyandroidintermediate.data.retrofit.ListStoryItem
import com.example.storyandroidintermediate.data.retrofit.StoryResponse
import com.example.storyandroidintermediate.utils.Constant
import retrofit2.Call
import retrofit2.Response


class StackRemoteStackFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()
    private var isDataLoaded = false

    override fun onCreate() {}

    override fun onDataSetChanged() { if(!isDataLoaded){
        var listStory: List<ListStoryItem> = emptyList()

        //token manual
        val client = ApiConfig.getApiService(Constant.kwhy_token).getStoriesWidget()
        client.enqueue(object : retrofit2.Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        listStory = responseBody.listStory
                    }

                    var i = 0
                    val maxItems = 10
                    for (story in listStory) {
                        if (i >= maxItems) break
                        getBitmapFromURL(mContext, story.photoUrl, mWidgetItems){
                            val appWidgetManager = AppWidgetManager.getInstance(mContext)
                            val thisAppWidget = ComponentName(mContext, WidgetStoryAndromeda::class.java)
                            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
                            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view_widget)
                        }
                        i++
                        Log.d("WidgetImages:",story.photoUrl)
                        Log.d("WidgetItems:" , mWidgetItems.size.toString())
                    }

                    isDataLoaded = true


                } else {
                    Log.e("RequestWidget", "onFailure: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e("RequestWidget", "onFailure: ${t.message}")

            }
        })


    }
    }

    fun getBitmapFromURL(context: Context, imageUrl: String, bitmapList: ArrayList<Bitmap>, callback: () -> Unit) {
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmapList.add(resource)
                    callback.invoke()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return mWidgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item_story)
        rv.setImageViewBitmap(R.id.previewimage_bagianstory, mWidgetItems[position])

        val extras = bundleOf(
            WidgetStoryAndromeda.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.previewimage_bagianstory, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}