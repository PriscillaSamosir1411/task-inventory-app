package org.ibda.myguessgame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import org.ibda.myguessgame.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private lateinit var vm : DetailsViewModel
    private lateinit var binding : FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val rootView = binding.root

        vm = ViewModelProvider(this).get(DetailsViewModel::class.java)

        this.binding.detailTask = vm
        this.binding.lifecycleOwner = viewLifecycleOwner

        val taskId = arguments?.getInt("taskId") ?: -1
        vm.taskDetail(taskId)

        vm.destination.observe(this.viewLifecycleOwner, { newValue ->
            when (newValue) {
                "Back" -> {

                    // Call ViewModel function to add the new task
                    vm.editStatus(taskId)

                    val actionTextValue = vm.actionText.value // Accessing the value of actionText
                    val titleDetailValue = vm.titleDetail.value // Accessing the value of titleDetail

                    val toastMessage = "$actionTextValue Task $titleDetailValue"
                    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()

                    requireView().findNavController().popBackStack()

                }
            }
        })

        return rootView
    }
}
