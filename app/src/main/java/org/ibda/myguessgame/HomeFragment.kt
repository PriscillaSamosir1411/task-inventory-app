package org.ibda.myguessgame

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import org.ibda.myguessgame.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var vm : HomeViewModel
    private lateinit var binding : FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Temukan tombol "Add" menggunakan findViewById
        val addButton = view.findViewById<Button>(R.id.btnAdd)

        // Atur onClickListener untuk tombol "Add"
        addButton.setOnClickListener {
            // Navigasi ke halaman form data
            view.findNavController().navigate(R.id.action_HomeFragment_to_FormDataFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val rootView = binding.root

        vm = ViewModelProvider(this).get(HomeViewModel::class.java)

        this.binding.home = vm
        this.binding.lifecycleOwner = viewLifecycleOwner


        vm.destination.observe(this.viewLifecycleOwner, {newValue->
            if(newValue != ""){
                val action = HomeFragmentDirections
                    .actionHomeFragmentToBottomNavFragment(this.vm.destination.value!!)
                this.vm.destination.value = ""
                rootView.findNavController().navigate(action)
            }
        })

        // Observe task counts to update UI
        vm.newTaskTotal.observe(viewLifecycleOwner, { count ->
            Log.d("HomeFragment", "New Task Count: $count")
            binding.newTaskTotal.text = count.toString()
        })

        vm.progressTaskTotal.observe(viewLifecycleOwner, { count ->
            Log.d("HomeFragment", "Progress Task Count: $count")
            binding.progressTaskTotal.text = count.toString()
        })

        vm.doneTaskTotal.observe(viewLifecycleOwner, { count ->
            Log.d("HomeFragment", "Done Task Count: $count")
            binding.doneTaskTotal.text = count.toString()
        })


        return rootView
    }

    override fun onResume() {
        super.onResume()
        vm.refreshTasks()
    }

}

