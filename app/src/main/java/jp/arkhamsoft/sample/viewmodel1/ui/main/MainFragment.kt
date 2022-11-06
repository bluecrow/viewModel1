package jp.arkhamsoft.sample.viewmodel1.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jp.arkhamsoft.sample.viewmodel1.databinding.FragmentMainBinding
import jp.arkhamsoft.sample.viewmodel1.domain.Person
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment @Inject constructor() : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.viewmodel = viewModel

        binding.textView1.text = viewModel.allByText()

        binding.myId.apply {
            setOnFocusChangeListener { _, b ->
                if (!b) {
                    try {
                        val id = text.toString().toInt()
                        val person = viewModel.getById(id) ?: Person("", "", 0)
                        viewModel.person.postValue(person)
                        binding.person = person
                    } catch (e: Exception) {
                        Log.d("MainFragment", "${e.message}")
                    }
                }
            }
        }

        binding.addButton.apply {
            setOnClickListener {
                try {
                    val myId = binding.myId.text.toString()
                    val newName = binding.name.text.toString()
                    val newMail = binding.mail.text.toString()
                    val newAge = binding.age.text.toString().toInt()
                    if (myId.isEmpty()) {
                        //add
                        viewModel.add(newName, newMail, newAge)
                    } else {
                        //update
                        val person = viewModel.getById(if (myId.isEmpty()) 0 else myId.toInt())
                        if (person != null) {
                            person.name = newName
                            person.mail = newMail
                            person.age = newAge
                            viewModel.update(person)
                        } else {
                            //not found
                            Toast.makeText(requireContext(), "Id not found", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("MainFragment", "${e.message}")
                } finally {
                    updateView()
                }
            }
        }

        binding.deleteButton.apply {
            setOnClickListener {
                try {
                    val myId = binding.myId.text.toString()
                    if (myId.isNotEmpty()) {
                        val person = viewModel.getById(myId.toInt())
                        if (person != null) {
                            viewModel.delete(person)
                        }
                    }
                } catch (e: Exception) {
                    Log.d("MainFragment", "${e.message}")
                } finally {
                    updateView()
                }
            }
        }

        viewModel.person.observe(viewLifecycleOwner) {
            binding.addButton.text = it.label()
        }

        return binding.root
    }

    private fun updateView() {
        binding.textView1.text = viewModel.allByText()
        initInputField()
        viewModel.person.value = Person("", "", 0)
    }

    private fun initInputField() {
        binding.myId.setText("")
        binding.name.setText("")
        binding.mail.setText("")
        binding.age.setText("0")
    }
}