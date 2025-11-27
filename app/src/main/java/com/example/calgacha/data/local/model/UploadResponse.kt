package com.example.calgacha.data.local.model

data class UploadData(
    val url:String
)

data class UploadResponse (
    val status: String,
    val data: UploadData
)