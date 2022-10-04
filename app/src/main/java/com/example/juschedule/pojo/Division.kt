package com.example.juschedule.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Division(
    var Division:String?="",
    var teacher:String?="",
    var time:String?="", var days:String?="",
    var coursesName:String?=""): Parcelable