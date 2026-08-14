package com.example.ashrut.reposcout.data.local.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ashrut.reposcout.data.local.entity.RepositoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {

    @Query("SELECT * FROM saved_repositories ORDER BY name ASC")
    fun getSavedRepositories(): Flow<List<RepositoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRepository(
        repository: RepositoryEntity
    )

    @Delete
    suspend fun deleteRepository(
        repository: RepositoryEntity
    )

    @Query(
        "SELECT EXISTS(SELECT 1 FROM saved_repositories WHERE id = :id)"
    )
    suspend fun isRepositorySaved(
        id: Long
    ): Boolean

    @Query(
        "SELECT * FROM saved_repositories WHERE id = :id LIMIT 1"
    )
    suspend fun getSavedRepository(
        id: Long
    ): RepositoryEntity?
}