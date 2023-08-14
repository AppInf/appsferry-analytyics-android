package com.appsferry.analytics.reporter.net

import com.appsferry.core.network.AfResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/2
 */
interface ReportService {
    @POST("log/upload")
    fun sendData(@Body body: RequestBody, @QueryMap params: Map<String, String>): Observable<AfResponse<Any>>
}