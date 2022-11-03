package jp.arkhamsoft.sample.viewmodel1.dao

import android.content.Context
import androidx.room.*
import jp.arkhamsoft.sample.viewmodel1.domain.Person

@Database(entities = [Person::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun personDao(): PersonDao

    companion object {
        @Volatile private var singleton: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            singleton ?: synchronized(this) {
                singleton ?: buildDatabase(context)
                    .also { singleton = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "mydata.db")
                .allowMainThreadQueries()
                .build()
    }
}

@Dao
interface PersonDao {
    @Query("SELECT * FROM person")
    fun loadAllPerson(): Array<Person>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPerson(person: Person)

    @Query("SELECT * FROM person WHERE id = :id")
    fun getPersonById(id: Int): Person?

    @Update
    fun updatePerson(person: Person)

    @Delete
    fun deletePerson(person: Person)
}