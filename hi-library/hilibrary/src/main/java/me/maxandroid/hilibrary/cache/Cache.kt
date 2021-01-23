package me.maxandroid.hilibrary.cache

import androidx.room.Entity

@Entity(tableName = "cache")
class Cache {
    var key: String = ""

    var data: ByteArray? = null
}