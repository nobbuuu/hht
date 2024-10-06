package com.booyue.poetry.bean

data class MainVideoBean(
        var videoName: String? = "",
        var downloadStatus: Int = 0,
        var downloadUrl: String? = null,
        var type: Int = 0,
        var subImg: Int = 0
)
