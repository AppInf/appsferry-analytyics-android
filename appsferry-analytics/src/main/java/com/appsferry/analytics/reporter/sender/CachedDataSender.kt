package com.appsferry.analytics.reporter.sender

import com.appsferry.analytics.reporter.Reporter
import com.appsferry.analytics.reporter.cache.ReportCache
import com.appsferry.analytics.reporter.model.Priority
import com.appsferry.analytics.reporter.model.ReportModel
import com.appsferry.core.lifecycle.ApplicationMonitor
import com.appsferry.core.lifecycle.ForeBackgroundListener
import com.appsferry.core.logger.ALog
import com.appsferry.core.network.AfResponse
import com.appsferry.core.network.rx.AfNetObserver
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/5
 */
internal class CachedDataSender(
    private val dataSource: ReportCache,
    private val sender: Sender,
    private val executor: ScheduledExecutorService
) : Sender {

    private val intervalMs = 30 * 1000L
    private var future: Future<*>? = null
    private var isEnable = false

    fun init() {
        start()
        ApplicationMonitor.instance.registerForeBackgroundListener(object : ForeBackgroundListener {
            override fun onForeground() {
                start()
            }

            override fun onBackground() {
                stop()
            }

        })
    }

    override fun sendData(dataList: List<ReportModel>, priority: Int): Observable<AfResponse<Any>> {
        return sender.sendData(dataList, Priority.normal)
    }

    fun setEnable(enable: Boolean) {
        isEnable = enable
    }

    fun start() {
        if (future != null) {
            return
        }
        future = executor.scheduleAtFixedRate({
            if (isEnable && dataSource.hasMore()) {
                val segment = dataSource.nextSegment()
                sendData(segment, Priority.normal).subscribe(object : AfNetObserver<Any>() {
                    override fun success(data: Any?) {
                    }

                    override fun failed(code: Int, msg: String, error: Throwable?) {
                        ALog.d(Reporter.TAG, "CachedData failed, code = $code, msg = $msg")
                        dataSource.cache(segment)
                    }
                })
            }
        }, 2000, intervalMs, TimeUnit.MILLISECONDS)
    }

    private fun stop() {
        future?.cancel(false)
        future = null
    }


}