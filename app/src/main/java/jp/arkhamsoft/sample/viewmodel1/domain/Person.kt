package jp.arkhamsoft.sample.viewmodel1.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Person(var name: String, var mail: String, var age: Int) {
    companion object {
        fun label(argId: Int): String = if (argId == 0) "Add Person" else "Update Person"
    }
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    fun id_s() = if (id == 0) { "" } else { id.toString() }
    fun age_s() = age.toString()
    fun to_s() = "$id: $name ($mail, $age)"
    fun label() = label(id)
}