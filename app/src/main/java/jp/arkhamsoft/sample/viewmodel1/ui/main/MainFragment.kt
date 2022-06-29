package jp.arkhamsoft.sample.viewmodel1.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.room.Room
import jp.arkhamsoft.sample.viewmodel1.databinding.FragmentMainBinding
import jp.arkhamsoft.sample.viewmodel1.R
import jp.arkhamsoft.sample.viewmodel1.dao.AppDatabase
import jp.arkhamsoft.sample.viewmodel1.domain.Person

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.setContentView(
            requireActivity(), R.layout.fragment_main
        )

        val db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,
            "mydata.db"
        ).allowMainThreadQueries().build()
        binding.textView1.text = getByText(db.personDao().loadAllPerson())
        binding.addButton.text = Person.label(0)

        binding.myId.setOnFocusChangeListener { view, b ->
            if (!b) {
                try {
                    val id = binding.myId.text.toString().toInt()
                    binding.addButton.text = Person.label(id)
                    val person = db.personDao().getPersonById(id)
                    binding.name.setText(person?.name ?: "")
                    binding.mail.setText(person?.mail ?: "")
                    binding.age.setText(person?.age_s() ?: "0")
                } catch (e: Exception) {

                }
            }
        }

        binding.addButton.setOnClickListener {
            val myId = binding.myId.text
            val newName = binding.name.text
            val newMail = binding.mail.text
            val newAge = binding.age.text
            val person = db.personDao().getPersonById(if (myId.toString().isEmpty()) 0 else myId.toString()?.toInt() ?: 0)
            if (myId.toString().isEmpty()) {
                val person = Person(newName.toString(), newMail.toString(), newAge.toString().toInt())
                db.personDao().insertPerson(person)
            } else {
                person.name = newName.toString()
                person.mail = newMail.toString()
                person.age = newAge.toString().toInt()
                db.personDao().updatePerson(person)
            }
            initInputField()
            viewModel.person.value = Person("", "", 0)
            binding.textView1.text = getByText(db.personDao().loadAllPerson())
        }
        binding.deleteButton.setOnClickListener {
            val myId = binding.myId.text
            val person = db.personDao().getPersonById(if (myId.toString().isEmpty()) 0 else myId.toString()?.toInt() ?: 0)
            if (!myId.toString().isEmpty()) {
                db.personDao().deletePerson(person)
            }
            initInputField()
            viewModel.person.value = Person("", "", 0)
            binding.textView1.text = getByText(db.personDao().loadAllPerson())
        }
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    private fun initInputField() {
        binding.myId.setText("")
        binding.name.setText("")
        binding.mail.setText("")
        binding.age.setText("0")
        binding.addButton.text = Person.label(0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val allObserver = Observer<String> { newAll ->
            requireActivity().findViewById<TextView>(R.id.textView1).text = newAll
        }
        binding.viewmodel = viewModel
    }

    fun getByText(data: Array<Person>): String {
        var result = ""
        for (item in data) {
            result += item.to_s()
            result += "\n"
        }
        return result
    }
}