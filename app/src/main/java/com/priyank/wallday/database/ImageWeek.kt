package com.priyank.wallday.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ImageWeek(
    @PrimaryKey val dayOfWeek: Int,
    var isImageSelected: Boolean,
    var imagePath: String?
)