package com.panick.littlelemon

import android.content.Context
import androidx.room.*

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val image: String
)

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_items")
    suspend fun getAllMenuItems(): List<MenuItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItems(menuItems: List<MenuItemEntity>)

    @Query("DELETE FROM menu_items")
    suspend fun deleteAllMenuItems()
}

@Database(entities = [MenuItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}