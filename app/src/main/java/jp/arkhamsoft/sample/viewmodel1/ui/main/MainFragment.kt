package jp.arkhamsoft.sample.viewmodel1.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.arkhamsoft.sample.viewmodel1.dao.AppDatabase
import jp.arkhamsoft.sample.viewmodel1.databinding.FragmentMainBinding
import jp.arkhamsoft.sample.viewmodel1.domain.Person

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        val db = AppDatabase.getInstance(requireContext())

        viewModel = MainViewModelFactory(db.personDao())
            .create(MainViewModel::class.java)

        binding.viewmodel = viewModel

        binding.textView1.text = viewModel.allByText()
        binding.addButton.text = Person.label(0)

        binding.myId.setOnFocusChangeListener { view, b ->
            if (!b) {
                try {
                    val id = binding.myId.text.toString().toInt()
                    binding.addButton.text = Person.label(id)
                    val person = viewModel.getById(id)
                    binding.name.setText(person?.name ?: "")
                    binding.mail.setText(person?.mail ?: "")
                    binding.age.setText(person?.age_s() ?: "0")
                } catch (e: Exception) {
                    Log.d("MainFragment", "catch Exception")
                }
            }
        }

        binding.addButton.setOnClickListener {
            val myId = binding.myId.text
            val newName = binding.name.text
            val newMail = binding.mail.text
            val newAge = binding.age.text
            if (myId.toString().isEmpty()) {
                viewModel.add(newName.toString(), newMail.toString(), newAge.toString().toInt())
            } else {
                val person = viewModel.getById(
                    if (myId.toString().isEmpty()) 0 else myId.toString()?.toInt()
                )
                if (person != null) {
                    person.name = newName.toString()
                    person.mail = newMail.toString()
                    person.age = newAge.toString().toInt()
                    viewModel.update(person)
                }
            }
            binding.textView1.text = viewModel.allByText()
            initInputField()
            viewModel.person.value = Person("", "", 0)
        }
        binding.deleteButton.setOnClickListener {
            val myId = binding.myId.text
            if (!myId.toString().isEmpty()) {
                val person = viewModel.getById(
                    if (myId.toString().isEmpty()) 0 else myId.toString()?.toInt()
                )
                if (person != null) {
                    viewModel.delete(person)
                    initInputField()
                    viewModel.person.value = Person("", "", 0)
                    binding.textView1.text = viewModel.allByText()
                }
            }
        }

        return binding.root
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