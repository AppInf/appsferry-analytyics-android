@file:Suppress("unused")

package com.appsferry.analytics.reporter.session

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.text.TextUtils
import com.appsferry.analytics.AnalyticsComponent
import com.appsferry.core.encrypt.Md5Utils
import com.appsferry.core.parameters.Parameters
import com.appsferry.core.storage.JetpackDataStore
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

object SessionManager {
    private val THRESHOLD_FOR_CHANGE_SESSION_FROM_BG_2_FG = TimeUnit.MINUTES.toMillis(1)

    private val sessionIdStore by lazy {
        StringStorage("data_ayanlytics_session")
    }

    private val lastHideTimestamp by lazy {
        LongStorage("data_ayanlytics_LAST_HIDE_TIMESTAMP")
    }

    private val hasInitialize = AtomicBoolean(false)
    private val showingPages: MutableList<String> = ArrayList()

    private fun initializeIfNot() {
        if (hasInitialize.compareAndSet(true, true)) {
            return
        }
        if (TextUtils.isEmpty(sessionIdStore.get())) {
            sessionIdStore.set(genSession())
        }
        (AnalyticsComponent.appContext as Application).registerActivityLifecycleCallbacks(object :
            ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                onPageShow(activity)
            }

            override fun onActivityPaused(activity: Activity) {
                onPageHide(activity)
            }

            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(
                activity: Activity,
                outState: Bundle
            ) {
            }

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    private fun onPageHide(activity: Any) {
        showingPages.remove(getPageKey(activity))
        val current = System.currentTimeMillis()
        lastHideTimestamp.set(current)
    }

    private fun onPageShow(activity: Any) {
        val lastIsEmpty = showingPages.isEmpty()
        val pageKey = getPageKey(activity)
        showingPages.add(pageKey)
        if (lastIsEmpty
            && System.currentTimeMillis() - lastHideTimestamp.get() >= THRESHOLD_FOR_CHANGE_SESSION_FROM_BG_2_FG
        ) {
            sessionIdStore.set(genSession())
        }
    }

    private fun getPageKey(activity: Any): String {
        return "${activity.javaClass.canonicalName}@${System.identityHashCode(activity)}"
    }

    val sessionId: String
        get() {
            initializeIfNot()
            return sessionIdStore.get()!!
        }

    private fun genSession(): String {
        return sessionGenerator.newSession()
    }


    private var sessionGenerator: SessionGenerator = SessionGenerator.DEFAULT
    fun setSessionGenerator(sessionGenerator: SessionGenerator) {
        SessionManager.sessionGenerator = sessionGenerator
    }

    interface SessionGenerator {
        companion object {
            val DEFAULT by lazy {
                object : SessionGenerator {
                    override fun newSession(): String {
                        var cv = Parameters.getInstance()["cv"]
                        if (cv == null) {
                            cv = "cv_fake_test"
                        }
                        val version: String =
                            Md5Utils.encode(
                                cv.toByteArray(StandardCharsets.UTF_8)
                            )?:""

                        val uuid: String = Md5Utils.encode(
                                UUID.randomUUID()
                                    .toString()
                                    .toByteArray()
                        )?:""
                        return version.substring(8, 24) + uuid.substring(8, 24)
                    }
                }
            }
        }

        fun newSession(): String
    }

    private class StringStorage(
        private val key: String
    ) {
        fun get(): String? = JetpackDataStore.DEFAULT.getString(key, null)

        fun set(value: String) = JetpackDataStore.DEFAULT.saveString(key, value)
    }

    private class LongStorage(
        private val key: String
    ) {
        fun get(): Long = JetpackDataStore.DEFAULT.getLong(key, 0L)

        fun set(value: Long) = JetpackDataStore.DEFAULT.saveLong(key, value)
    }
}