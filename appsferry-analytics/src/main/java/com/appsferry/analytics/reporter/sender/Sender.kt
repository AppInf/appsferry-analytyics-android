package com.appsferry.analytics.reporter.sender

import com.appsferry.analytics.reporter.model.Priority
import com.appsferry.analytics.reporter.model.ReportModel
import com.appsferry.core.network.AfResponse
import io.reactivex.rxjava3.core.Observable

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/5
 */
interface Sender {
    fun sendData(dataList: List<ReportModel>, @Priority priority: Int): Observable<AfResponse<Any>>
}