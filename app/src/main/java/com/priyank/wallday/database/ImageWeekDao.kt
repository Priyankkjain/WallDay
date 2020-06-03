package com.priyank.wallday.database

import androidx.room.*
import io.reactivex.Single

@Dao
interface ImageWeekDao {
    @Query("SELECT * from imageweek")
    fun getAllWeekImage(): Single<List<ImageWeek>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imageWeek: ImageWeek): Single<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(imageWeek: ImageWeek): Single<Int>
}