package com.appsferry.analytics.auto

import android.text.TextUtils
import com.appsferry.analytics.auto.helper.InstallStateHelper
import com.appsferry.analytics.auto.model.AppEndModel
import com.appsferry.analytics.auto.model.AppStartModel
import com.appsferry.analytics.reporter.Reporter
import com.appsferry.analytics.reporter.model.EventType
import com.appsferry.core.lifecycle.ApplicationMonitor
import com.appsferry.core.lifecycle.ForeBackgroundListener
import com.appsferry.core.logger.ALog

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/6
 */
class AppStatusReport {
    companion object {
        private const val STATUS_START = "app_start"
        private const val STATUS_END = "app_end"
    }

    private var isFirstInstall = true
    private var isColdLauncher = true

    private var eidPrefix: String? = null

    fun init(prefix: String) {
        this.eidPrefix = prefix
        isFirstInstall = InstallStateHelper.isFirstInstall()
        if (!ApplicationMonitor.instance.isBackground) {
            reportAppStart()
        }
        ApplicationMonitor.instance.registerForeBackgroundListener(object : ForeBackgroundListener {
            override fun onForeground() {
                reportAppStart()
            }

            override fun onBackground() {
                reportAppEnd()
            }
        })
    }


    private fun reportAppStart() {
        val startModel = AppStartModel()
        startModel.start_time = System.currentTimeMillis().toString()
        if (isFirstInstall) {
            startModel.first_start = "1"
            isFirstInstall = false
            InstallStateHelper.markInstalled()
        } else {
            startModel.first_start = "0"
        }
        if (isColdLauncher) {
            startModel.mod = "0"
            isColdLauncher = false
        } else {
            startModel.mod = "1"
        }
        val eidFix = if (TextUtils.isEmpty(eidPrefix)) STATUS_START else "${eidPrefix}_${STATUS_START}"
        Reporter.INSTANCE.report(startModel, eidFix, EventType.batch)
        ALog.i(BasicAnalytics.TAG, "send app start")
    }

    private fun reportAppEnd() {
        val startModel = AppEndModel()
        startModel.end_time = System.currentTimeMillis().toString()
        val eidFix = if (TextUtils.isEmpty(eidPrefix)) STATUS_END else "${eidPrefix}_$STATUS_END"
        Reporter.INSTANCE.report(startModel, eidFix, EventType.batch)
        ALog.i(BasicAnalytics.TAG, "send app end")
    }
}