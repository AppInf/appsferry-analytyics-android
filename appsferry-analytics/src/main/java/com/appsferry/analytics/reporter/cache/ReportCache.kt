package com.appsferry.analytics.reporter.cache

import com.appsferry.analytics.reporter.model.ReportModel

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/5
 */
interface ReportCache {
    fun cache(data: List<ReportModel>)
    fun nextSegment(): List<ReportModel>
    fun hasMore(): Boolean
}