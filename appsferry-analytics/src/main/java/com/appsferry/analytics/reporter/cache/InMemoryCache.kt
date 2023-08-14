package com.appsferry.analytics.reporter.cache

import com.appsferry.analytics.reporter.model.ReportModel
import java.util.*
import kotlin.math.min

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/5
 */
class InMemoryCache: ReportCache {
    private val maxSize = 1000
    private val segmentSize = 100
    private val storage = LinkedList<ReportModel>()

    @Synchronized
    override fun cache(data: List<ReportModel>) {
        storage.addAll(data)
        if (storage.size > maxSize) {
            // abandon queue head
            val dataToAbandon = mutableListOf<ReportModel>()
            repeat(storage.size - maxSize) {
                dataToAbandon.add(storage[it])
            }
            storage.removeAll(dataToAbandon.toSet())
        }
    }

    @Synchronized
    override fun nextSegment(): List<ReportModel> {
        val size = min(segmentSize, storage.size)
        val segment = LinkedList<ReportModel>()
        repeat(size) {
            segment.add(storage[it])
        }
        // use set to improve
        storage.removeAll(segment.toSet())
        return segment
    }

    @Synchronized
    override fun hasMore(): Boolean = storage.isNotEmpty()

}