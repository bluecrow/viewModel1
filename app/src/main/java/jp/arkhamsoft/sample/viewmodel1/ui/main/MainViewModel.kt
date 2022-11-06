package jp.arkhamsoft.sample.viewmodel1.ui.main


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.arkhamsoft.sample.viewmodel1.dao.PersonRepository
import jp.arkhamsoft.sample.viewmodel1.domain.Person
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {

    var data: Array<Person> = repository.loadAllPerson()
    val person: MutableLiveData<Person> by lazy {
        MutableLiveData<Person>()
    }

    fun allByText(): String {
        data = repository.loadAllPerson()
        return data.joinToString(separator = "\n") { item -> item.to_s() }
    }

    fun getById(id: Int): Person? = repository.getPersonById(id)

    fun add(name: String, mail: String, age: Int) {
        val person = makePerson(name, mail, age)
        repository.insertPerson(person)
    }

    private fun makePerson(
        name: String,
        mail: String,
        age: Int
    ) = Person(name, mail, age)

    fun update(person: Person) {
        repository.updatePerson(person)
    }

    fun delete(person: Person) {
        repository.deletePerson(person)
    }
}
