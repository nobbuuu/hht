package com.booyue.poetry.bean

data class NatureVideoBean(
        val code: Int,
        val groupList: List<Group>,
        val info: String,
        val success: Boolean
)

data class Group(
        val groupId: Int,
        val groupName: String,
        val videoVoList: List<VideoVo>
)

data class VideoVo(
        val fileSize: Int = 0,
        var id: Long = 0,
        var image: String? = null,
        var name: String? = null,
        val sort: Int = 0,
        var url: String? = null,

        var status: Int = 0,
        var type: Int = 0,
        var subImg: Int = 0,
        var percent: Int = 0,
        var groupName: String? = null,
        var groupId: Int = 0
)