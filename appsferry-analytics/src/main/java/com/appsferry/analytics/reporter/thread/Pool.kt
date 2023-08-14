package com.appsferry.analytics.reporter.thread

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/6
 */
internal interface ScheduledServiceProvider {
    fun provide(): ScheduledExecutorService
}

internal val DEFAULT_POOL: ScheduledServiceProvider = object : ScheduledServiceProvider {
    override fun provide(): ScheduledExecutorService {
        return Executors.newSingleThreadScheduledExecutor()
    }
}

