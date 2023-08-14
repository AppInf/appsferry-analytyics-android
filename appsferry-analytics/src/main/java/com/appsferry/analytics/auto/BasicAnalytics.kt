package com.appsferry.analytics.auto

import android.util.Base64
import com.appsferry.analytics.reporter.thread.DEFAULT_POOL
import com.appsferry.core.parameters.Parameters
import java.util.concurrent.atomic.AtomicBoolean

/**
 * basic data: heart beat & foreground & background
 *
 * @author Floyd
 * @since  2023/6/6
 */
internal class BasicAnalytics private constructor() {

    companion object {
        const val TAG = "BasicAnalytics"
        val INSTANCE by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BasicAnalytics()
        }
    }

    private lateinit var heartBeat: HeartBeat
    private lateinit var appStatusReport: AppStatusReport
    private val hasInit = AtomicBoolean(false)


    private val eidPrefix by lazy {
        getEidPrefixFromAppid()
    }

    fun init() {
        if (hasInit.compareAndSet(true, true)) {
            return
        }
        heartBeat = HeartBeat(DEFAULT_POOL.provide())
        heartBeat.init(eidPrefix)
        appStatusReport = AppStatusReport()
        appStatusReport.init(eidPrefix)
    }

    private fun getEidPrefixFromAppid(): String {
        val appid = Parameters.getInstance().get("appid") ?: return ""
        val decodedAppId: String = String(Base64.decode(appid, Base64.URL_SAFE))
        val splits = decodedAppId.split(":")
        if (splits.size < 2) {
            return ""
        }
        return splits[1].trim()
    }

}