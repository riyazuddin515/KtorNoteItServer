package com.riyazuddin.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Note(
    val title: String,
    val content: String,
    val date: Long,
    val owner: String,
    val color: String,
    @BsonId
    val id: String = ObjectId().toString()
)