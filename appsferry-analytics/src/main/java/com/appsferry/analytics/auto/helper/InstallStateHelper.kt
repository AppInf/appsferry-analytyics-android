package com.appsferry.analytics.auto.helper

import com.appsferry.core.storage.JetpackDataStore

/**
 * description:
 *
 * @author Floyd
 * @since 2022/9/19
 */
object InstallStateHelper {
    private val KEY = "data_analytics_first_install"
    fun isFirstInstall(): Boolean {
        return JetpackDataStore.DEFAULT.getBoolean(KEY, true)
    }
    fun markInstalled() {
        JetpackDataStore.DEFAULT.saveBoolean(KEY, false)
    }
}