package com.appsferry.analytics.auto

import android.text.TextUtils
import com.appsferry.analytics.auto.model.HeartBeatModel
import com.appsferry.analytics.reporter.Reporter
import com.appsferry.analytics.reporter.model.EventType
import com.appsferry.analytics.reporter.model.Priority
import com.appsferry.core.lifecycle.ApplicationMonitor
import com.appsferry.core.lifecycle.ForeBackgroundListener
import com.appsferry.core.logger.ALog
import com.appsferry.core.parameters.Parameters
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/6
 */
class HeartBeat(
    private val scheduler: ScheduledExecutorService
) {
    private val eid = "app_heartbeat"
    private val intervalMs = 30 * 1000L
    private var lastSendTime = 0L
    private var future: ScheduledFuture<*>? = null
    private var eidPrefix: String? = null

    fun init(prefix: String) {
        eidPrefix = prefix
        startHeartBeat()
        ApplicationMonitor.instance.registerForeBackgroundListener(object : ForeBackgroundListener {
            override fun onForeground() {
                startHeartBeat()
            }

            override fun onBackground() {
                sendHeartBeat()
                stopHeartBeat()
            }
        })
        Parameters.getInstance().subscribe("uid") { isExisted, newValue, preValue ->
            sendHeartBeat()
        }
    }

    private fun sendHeartBeat() {
        if (System.currentTimeMillis() - lastSendTime < 5000) {
            return
        }
        val model = HeartBeatModel().also {
            it.time = System.currentTimeMillis().toString()
        }
        val eidFix = if (!TextUtils.isEmpty(eidPrefix)) "${eidPrefix}_$eid" else eid
        Reporter.INSTANCE.report(model, eidFix, EventType.batch, Priority.normal)
        ALog.i(BasicAnalytics.TAG, "send heartbeat")
        lastSendTime = System.currentTimeMillis()
    }

    private fun startHeartBeat() {
        if (future != null) {
            return
        }
        ALog.i(BasicAnalytics.TAG, "schedule heartbeat")
        future = scheduler.scheduleAtFixedRate(this::sendHeartBeat, 0, intervalMs, TimeUnit.MILLISECONDS)
    }

    private fun stopHeartBeat() {
        ALog.i(BasicAnalytics.TAG, "cancel heartbeat")
        future?.cancel(true)
        future = null
    }

}