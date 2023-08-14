package com.appsferry.analytics.reporter.model

import androidx.annotation.IntDef
import com.appsferry.analytics.reporter.model.Priority.Companion.core
import com.appsferry.analytics.reporter.model.Priority.Companion.important
import com.appsferry.analytics.reporter.model.Priority.Companion.normal


/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/2
 */
@IntDef(core, important, normal)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class Priority {
    companion object {
        const val core = com.appsferry.core.analytics.Priority.core
        const val important = com.appsferry.core.analytics.Priority.important
        const val normal = com.appsferry.core.analytics.Priority.normal
    }
}
