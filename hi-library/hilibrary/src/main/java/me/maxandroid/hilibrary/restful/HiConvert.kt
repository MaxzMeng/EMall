package me.maxandroid.hilibrary.restful

import java.lang.reflect.Type

interface HiConvert {
    fun <T> convert(rawData: String, dataType: Type):HiResponse<T>
}