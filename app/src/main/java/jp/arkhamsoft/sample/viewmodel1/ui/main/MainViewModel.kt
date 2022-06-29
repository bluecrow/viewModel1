package jp.arkhamsoft.sample.viewmodel1.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.arkhamsoft.sample.viewmodel1.domain.Person

class MainViewModel : ViewModel() {
    val data: MutableLiveData<MutableList<Person>> by lazy {
        MutableLiveData<MutableList<Person>>()
    }
    val person: MutableLiveData<Person> by lazy {
        MutableLiveData<Person>()
    }
    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val amil: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val age: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val allText: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun allByText(): String {
        var result = ""
        for (item in data.value!!) {
            result += item.to_s()
            result += "\n"
        }
        return result
    }

    fun getById(id: Int): Person = data.value!!.get(id)

    fun add(name: String, mail: String, age: Int) = data.value?.add(Person(name, mail, age))

    init {
        data.value = mutableListOf(
            Person("Taro", "taro@yamada", 36),
            Person("Hanako", "hanako@flower", 25),
            Person("Sachiko", "sachiko@happy", 14)
        )
        //allText.value = allByText()
    }
}

