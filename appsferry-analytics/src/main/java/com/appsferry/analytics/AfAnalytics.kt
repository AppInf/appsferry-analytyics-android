package com.appsferry.analytics

import com.appsferry.analytics.auto.BasicAnalytics
import com.appsferry.analytics.reporter.Reporter
import com.appsferry.analytics.reporter.model.EventType
import com.appsferry.analytics.reporter.model.Priority

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/6
 */
object AfAnalytics {
    @JvmStatic
    fun init() {
        Reporter.INSTANCE.init()
        BasicAnalytics.INSTANCE.init()
    }

    @JvmStatic
    fun setPrivacyAgreement(agree: Boolean) {
        Reporter.INSTANCE.setEnable(agree)
    }

    @JvmStatic
    @JvmOverloads
    fun report(
        data: Any,
        md_eid: String,
        @EventType md_etype: String = EventType.other,
        @Priority priority: Int = Priority.normal
    ) {
        Reporter.INSTANCE.report(data, md_eid, md_etype, priority)
    }

}