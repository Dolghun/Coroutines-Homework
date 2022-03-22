package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class ImgResource(
    @field:SerializedName("file")
    val fileUrl: String
)
