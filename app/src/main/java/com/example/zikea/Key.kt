package com.example.zikea

import com.orm.SugarRecord

class Key : SugarRecord {
    var id: Int = 0
    var name: String? = null
    var key: Int? = 0

    constructor() : super() {}
    constructor(id: Int, name: String?, key: Int?) : super() {
        this.id = id
        this.name = name
        this.key = key
    }
}