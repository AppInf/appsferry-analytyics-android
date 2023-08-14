package com.appsferry.analytics.reporter

import com.appsferry.analytics.AnalyticsComponent
import com.appsferry.analytics.a.j.a
import com.appsferry.analytics.reporter.cache.InMemoryCache
import com.appsferry.analytics.reporter.cache.ReportCache
import com.appsferry.analytics.reporter.model.EventType
import com.appsferry.analytics.reporter.model.Priority
import com.appsferry.analytics.reporter.model.ReportModel
import com.appsferry.analytics.reporter.sender.CachedDataSender
import com.appsferry.analytics.reporter.sender.RealTimeSender
import com.appsferry.analytics.reporter.thread.DEFAULT_POOL
import com.appsferry.core.analytics.IEventReporter
import com.appsferry.core.logger.ALog
import com.appsferry.core.network.rx.AfNetObserver
import com.appsferry.core.parameters.Parameters
import com.appsferry.core.parameters.appid


/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/2
 */

internal class Reporter : IEventReporter {
    companion object {
        const val TAG = "Reporter"
        val INSTANCE by lazy {
            Reporter()
        }
    }

    private val dataCache: ReportCache = InMemoryCache()
    private val realTimeSender = RealTimeSender()
    private val cachedDataSender = CachedDataSender(dataCache, realTimeSender, DEFAULT_POOL.provide())
    private var enable = false

    internal fun init() {
        cachedDataSender.init()
        val appId = Parameters.getInstance().appid
        if (appId.isNullOrEmpty()) {
            throw IllegalArgumentException("can't get valid app id from parameters!!")
        }
        a.b(AnalyticsComponent.appContext, appId)
    }

    override fun report(
        data: Any,
        md_eid: String,
        @EventType md_etype: String,
        @Priority priority: Int
    ) {
        sendData(ReportModel.obtain(data, md_eid, md_etype), priority)
    }

    fun setEnable(enable: Boolean) {
        this.enable = enable
        cachedDataSender.setEnable(enable)
    }

    private fun sendData(data: ReportModel, @Priority priority: Int) {
        sendData(listOf(data), priority)
    }

    private fun sendData(dataList: List<ReportModel>, @Priority priority: Int) {
        if (!enable) {
            dataCache.cache(dataList)
            return
        }
        realTimeSender.sendData(dataList, priority).subscribe(object : AfNetObserver<Any>() {
            override fun success(data: Any?) {
            }

            override fun failed(code: Int, msg: String, error: Throwable?) {
                ALog.d(TAG, "report data failed, code = $code, msg = $msg")
                dataCache.cache(dataList)
            }
        })
    }
}