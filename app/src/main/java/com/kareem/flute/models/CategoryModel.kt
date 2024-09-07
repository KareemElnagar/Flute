package com.kareem.flute.models

data class CategoryModel(
    val name: String,
    val coverUrl: String,
    var songs: List<String>
)
{
    constructor() : this(name = "", coverUrl = "",emptyList())
}
