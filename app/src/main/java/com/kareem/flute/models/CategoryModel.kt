package com.kareem.flute.models

data class CategoryModel(
    val name: String,
    val coverUrl: String,
    val songs: List<String>
)
{
    constructor() : this(name = "", coverUrl = "",emptyList())
}
