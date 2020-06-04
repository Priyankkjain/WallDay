package com.priyank.wallday.database

import androidx.room.*
import io.reactivex.Single

@Dao
interface ImageWeekDao {
    @Query("SELECT * FROM imageweek")
    fun getAllWeekImage(): Single<List<ImageWeek>>

    @Query("SELECT * FROM imageweek WHERE dayOfWeek = :selectedDayOfTheWeek")
    fun getDayImage(selectedDayOfTheWeek: Int): Single<ImageWeek>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imageWeek: ImageWeek): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTheImageWeek(vararg imageWeek: ImageWeek): Single<List<Long>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(imageWeek: ImageWeek): Single<Int>
}