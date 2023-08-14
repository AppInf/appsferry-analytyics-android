package com.appsferry.analytics.reporter.model

import com.appsferry.analytics.a.j.a
import com.appsferry.analytics.reporter.session.SessionManager
import com.appsferry.core.gson.AfGson
import com.appsferry.core.lifecycle.ApplicationMonitor
import com.appsferry.core.parameters.Parameters
import com.google.gson.annotations.SerializedName

class ReportModel private constructor(){
    @SerializedName("md_einfo")
    var data: Any? = null
    var md_eid: String? = null
    @EventType
    var md_etype: String? = null
    var md_etime: String? = null
    var md_userid: String? = null
    var md_mod: String? = null
    var md_path: String? = null
    var md_session: String? = null
    var md_chk: String? = null
    var md_logid: String? = null

    companion object {
        fun obtain(
             data: Any,
             md_eid: String,
             @EventType
             md_etype: String,
        ): ReportModel {

            if (data.javaClass.isPrimitive) {
                throw IllegalArgumentException("data can only be a java data bean")
            }

            val model = ReportModel()
            val currentTime = System.currentTimeMillis()
            val session = SessionManager.sessionId
            val uid = Parameters.getInstance()["uid"] ?: ""
            model.data = data
            model.md_eid = md_eid
            model.md_etype = md_etype
            model.md_mod = if (ApplicationMonitor.instance.isBackground) {
                "0"
            } else {
                "1"
            }
            model.md_etime = currentTime.toString()
            model.md_session = session
            model.md_chk = a.a(currentTime, uid, session)
            model.md_logid = "unset"
            model.md_path = ""
            model.md_userid = uid
            return model
        }
    }

    fun toJson(): String = AfGson.get().toJson(this)

    override fun toString(): String = toJson()
}