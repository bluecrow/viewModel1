package jp.arkhamsoft.sample.viewmodel1.dao

import jp.arkhamsoft.sample.viewmodel1.domain.Person
import javax.inject.Inject

class PersonRepository @Inject constructor(private val dao: PersonDao) {

    fun loadAllPerson(): Array<Person> = dao.loadAllPerson()

    fun insertPerson(person: Person) = dao.insertPerson(person)

    fun getPersonById(id: Int): Person? = dao.getPersonById(id)

    fun updatePerson(person: Person) = dao.updatePerson(person)

    fun deletePerson(person: Person) = dao.deletePerson(person)
}