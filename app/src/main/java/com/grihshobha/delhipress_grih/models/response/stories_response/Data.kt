package com.grihshobha.delhipress_grih.models.response.stories_response

data class Data(
    val catid: Int?,
    val catimage: List<Any?>?,
    val items: List<Item?>?,
    val subcat: List<Any?>?,
    val title: String?
)