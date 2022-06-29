package jp.arkhamsoft.sample.viewmodel1.ui.main

import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.arkhamsoft.sample.viewmodel1.dao.PersonDao
import jp.arkhamsoft.sample.viewmodel1.domain.Person
import java.lang.IllegalArgumentException

class MainViewModel(val dao: PersonDao) : ViewModel() {
    var data: Array<Person> = dao.loadAllPerson()
    val person: MutableLiveData<Person> by lazy {
        MutableLiveData<Person>()
    }

    fun allByText(): String {
        data = dao.loadAllPerson()
        var result = ""
        for (item in data) {
            result += item.to_s()
            result += "\n"
        }
        return result
    }

    fun getById(id: Int): Person? = dao.getPersonById(id)

    fun add(name: String, mail: String, age: Int) {
        val person = Person(name, mail, age)
        dao.insertPerson(person)
    }

    fun update(person: Person) {
        dao.updatePerson(person)
    }

    fun delete(person: Person) {
        dao.deletePerson(person)
    }
}

class MainViewModelFactory(private val dataSource: PersonDao): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(model: Class<T>): T {
        if (model.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Can not get viewModel")
    }
}