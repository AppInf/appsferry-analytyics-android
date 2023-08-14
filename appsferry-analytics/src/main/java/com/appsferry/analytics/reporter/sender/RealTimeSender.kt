package com.appsferry.analytics.reporter.sender

import com.appsferry.analytics.reporter.model.ReportModel
import com.appsferry.analytics.reporter.net.ReportService
import com.appsferry.core.logger.ALog
import com.appsferry.core.network.AfResponse
import com.appsferry.core.network.RetrofitAgent
import com.appsferry.core.parameters.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPOutputStream

/**
 * description:
 *
 * @author Floyd
 * @since  2023/6/5
 */
internal class RealTimeSender: Sender {
    companion object {
        private var seq = 0
        private var lastUid: String? = null
        @Synchronized
        private fun getSeq(uid: String): Int {
            if (lastUid != uid) {
                seq = 0
                lastUid = uid
            }
            return seq ++
        }
    }
    override fun sendData(dataList: List<ReportModel>, priority: Int): Observable<AfResponse<Any>> {
        return Observable.just(dataList).subscribeOn(Schedulers.computation()).map {
            zipTrackData(dataList.toList())
        }.flatMap {
            val body = RequestBody.create(MediaType.parse("application/octet-stream"), it)
            val params = getQueryParams().also { it["md_priority"] = priority.toString() }
            RetrofitAgent.INSTANCE.analyticsService<ReportService>().sendData(body, params)
        }
    }

    private fun zipTrackData(datas: List<ReportModel>): ByteArray {
        val multiJson = trackData2MultiJson(datas)
        return gzip(multiJson.toByteArray())
    }

    private fun trackData2MultiJson(data: List<ReportModel>): String {
        if (data.size == 1) {
            return data[0].toJson()
        }
        val builder = StringBuilder()
        for (item in data) {
            builder.append(item.toJson()).append("\n")
        }
        return builder.toString()
    }

    private fun gzip(foo: ByteArray): ByteArray {
        val baos = ByteArrayOutputStream()
        var gzos: GZIPOutputStream? = null
        try {
            gzos = GZIPOutputStream(baos)
            gzos.write(foo)
            gzos.flush()
        } catch (var12: IOException) {
            ALog.w("Track", "gzip error: $var12")
        } finally {
            if (gzos != null) {
                try {
                    gzos.close()
                } catch (ignored: IOException) {
                }
            }
        }
        return baos.toByteArray()
    }

    private fun getQueryParams(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        val uid = Parameters.getInstance().uid?:""
        map["cv"] = Parameters.getInstance().cv?:""
        map["uid"] = uid
        map["smid"] = Parameters.getInstance().smid?:""
        map[appIdKey()] = Parameters.getInstance().appid?:""
        map["seq"] = getSeq(uid).toString()
        Parameters.getInstance().uuid?.let {
            map["uuid"] = it
        }
        return map
    }

    private fun appIdKey(): String {
        val bytes = byteArrayOf(104, 106, 94, 96, 111, 111, 104, 99)
        val tmp = ByteArray(bytes.size)
        for (i in bytes.indices) {
            val b = bytes[i]
            if (b < 127) {
                tmp[i] = (b + 1).toByte()
            } else {
                tmp[i] = -128
            }
        }
        return String(tmp, StandardCharsets.UTF_8)
    }
}