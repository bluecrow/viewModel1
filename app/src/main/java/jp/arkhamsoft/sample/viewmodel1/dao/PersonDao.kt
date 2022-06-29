package jp.arkhamsoft.sample.viewmodel1.dao

import androidx.room.*
import jp.arkhamsoft.sample.viewmodel1.domain.Person

@Database(entities = [Person::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun personDao(): PersonDao
}

@Dao
interface PersonDao {
    @Query("SELECT * FROM person")
    fun loadAllPerson(): Array<Person>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPerson(person: Person)

    @Query("SELECT * FROM person WHERE id = :id")
    fun getPersonById(id: Int): Person

    @Update
    fun updatePerson(person: Person)

    @Delete
    fun deletePerson(person: Person)
}