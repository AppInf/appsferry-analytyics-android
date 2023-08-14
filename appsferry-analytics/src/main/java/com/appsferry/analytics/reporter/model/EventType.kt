package com.appsferry.analytics.reporter.model

import androidx.annotation.StringDef
import com.appsferry.analytics.reporter.model.EventType.Companion.action
import com.appsferry.analytics.reporter.model.EventType.Companion.basic
import com.appsferry.analytics.reporter.model.EventType.Companion.batch
import com.appsferry.analytics.reporter.model.EventType.Companion.click
import com.appsferry.analytics.reporter.model.EventType.Companion.other
import com.appsferry.analytics.reporter.model.EventType.Companion.quality
import com.appsferry.analytics.reporter.model.EventType.Companion.visit

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/2
 */
@StringDef(click, visit, basic, quality, action, batch, other)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class EventType {
    companion object {
        const val click = com.appsferry.core.analytics.EventType.click
        const val visit = com.appsferry.core.analytics.EventType.visit
        const val basic = com.appsferry.core.analytics.EventType.basic
        const val quality = com.appsferry.core.analytics.EventType.quality
        const val action = com.appsferry.core.analytics.EventType.action
        const val batch = com.appsferry.core.analytics.EventType.batch
        const val other = com.appsferry.core.analytics.EventType.other
    }
}
