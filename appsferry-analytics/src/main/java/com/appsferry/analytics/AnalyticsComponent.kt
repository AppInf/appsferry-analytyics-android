package com.appsferry.analytics

import android.content.Context
import androidx.annotation.RestrictTo
import com.appsferry.analytics.reporter.Reporter
import com.appsferry.core.analytics.EventReporter
import com.appsferry.core.lifecycle.AppLifecycleAware
import com.google.auto.service.AutoService

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/2
 */
@AutoService(AppLifecycleAware::class)
class AnalyticsComponent: AppLifecycleAware {

    companion object {
        @RestrictTo(RestrictTo.Scope.LIBRARY)
        internal lateinit var appContext: Context

    }


    override fun onAppStart(appContext: Context) {
        AnalyticsComponent.appContext = appContext
        EventReporter.setDelegate(Reporter.INSTANCE)
    }

    override fun onForeground() {

    }

    override fun onBackground() {

    }
}