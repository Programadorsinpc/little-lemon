package com.panick.littlelemon

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    val category: String
)

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_items")
    fun getAllMenuItems(): LiveData<List<MenuItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMenuItems(menuItems: List<MenuItemEntity>)

    @Query("DELETE FROM menu_items")
    fun deleteAllMenuItems()
}

@Database(entities = [MenuItemEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Destruir y recrear la base de datos
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}


/*
class InventoryRepository(context: Context) {
    private val database = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "inventory.db"
    ).createFromAsset("database/inventory.db").build()

    fun getAllMenuItems() = database.menuItemDao().getAllMenuItems()

    fun insertMenuItems(menuItems: List<MenuItemEntity>) =
        database.menuItemDao().insertMenuItems(menuItems)
}*/
