package com.example.storyandroidintermediate.data.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.example.storyandroidintermediate.R


class WidgetStoryAndromeda : AppWidgetProvider() {
    companion object {
        private const val TOAST_ACTION = "com.example.storyandroidintermediate.TOAST_ACTION"
        const val EXTRA_ITEM = "com.example.storyandroidintermediate.EXTRA_ITEM"
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val intent = Intent(context, StoryAndromedaWidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

        val views = RemoteViews(context.packageName, R.layout.story_widget)
        views.setRemoteAdapter(R.id.stack_view_widget, intent)
        views.setEmptyView(R.id.stack_view_widget, R.id.empty_view_widget)

        val toastIntent = Intent(context, WidgetStoryAndromeda::class.java)
        toastIntent.action = TOAST_ACTION
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

        val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else 0
        )

        views.setPendingIntentTemplate(R.id.stack_view_widget, toastPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == TOAST_ACTION){
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }
}