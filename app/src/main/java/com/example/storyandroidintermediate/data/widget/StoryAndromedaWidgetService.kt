package com.example.storyandroidintermediate.data.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StoryAndromedaWidgetService : RemoteViewsService(){
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory = StackRemoteStackFactory(this.applicationContext)
}