package org.ibda.myguessgame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.ibda.myguessgame.databinding.FragmentFormDataBinding

class FormDataFragment : Fragment() {
    private lateinit var viewModel: FormDataViewModel
    private lateinit var binding: FragmentFormDataBinding
    private var selectedCategory: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFormDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FormDataViewModel::class.java)

        // Set up click listeners for category buttons
        binding.btnNormal.setOnClickListener { selectCategory("Normal") }
        binding.btnImportant.setOnClickListener { selectCategory("Important") }
        binding.btnUrgent.setOnClickListener { selectCategory("Urgent") }

        // Set up click listener for the submit button
        binding.buttonSubmit.setOnClickListener {
            submitData()
        }

        // Observe task submission status
        viewModel.taskSubmissionStatus.observe(viewLifecycleOwner, { status ->
            Log.d("FormDataFragment", "Task submission status: $status")
            Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
        })

        // Mengakses argumen yang dikirimkan
        val args = FormDataFragmentArgs.fromBundle(requireArguments())
        val data = args.data // Mengambil nilai argumen

        // Set up any observers or listeners here
    }

    private fun selectCategory(category: String) {
        // Reset all buttons to default state
        resetButtonsState()

        // Set the selected button to pressed state
        when (category) {
            "Normal" -> binding.btnNormal.isPressed = true
            "Important" -> binding.btnImportant.isPressed = true
            "Urgent" -> binding.btnUrgent.isPressed = true
        }

        // Set the selected category
        selectedCategory = category
    }

    private fun resetButtonsState() {
        // Reset all buttons to default state
        binding.btnNormal.isPressed = false
        binding.btnImportant.isPressed = false
        binding.btnUrgent.isPressed = false
    }

    fun submitData() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()

        if (title.isNotEmpty() && description.isNotEmpty() && selectedCategory.isNotEmpty()) {
            val addTaskInfo = AddTaskInfo(title, description, selectedCategory)
            Log.d("FormDataFragment", "Submitting data: $addTaskInfo")
            viewModel.addTask(addTaskInfo)
        } else {
            Log.d("FormDataFragment", "Form data is incomplete")
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
