package com.example.studentmanagerfragment.fragments

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.studentmanagerfragment.R
import com.example.studentmanagerfragment.adapters.StudentListAdapter
import com.example.studentmanagerfragment.StudentViewModel

class StudentListFragment : Fragment() {
    private lateinit var adapter: StudentListAdapter
    private val studentViewModel: StudentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_student_list, container, false)
        val listView = view.findViewById<ListView>(R.id.list_view_students)

        adapter = StudentListAdapter(requireContext(), studentViewModel.students.value ?: mutableListOf())
        listView.adapter = adapter

        registerForContextMenu(listView)

        studentViewModel.students.observe(viewLifecycleOwner) { updatedList ->
            if (updatedList != null) {
                adapter.updateData(updatedList)
            }
        }

        listView.onItemClickListener = null
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position // Get the list position
        val students = studentViewModel.students.value // Current list of students

        val selectedStudent = students?.get(position) // Retrieve the student from the list

        return when (item.itemId) {
            R.id.context_edit -> {
                if (selectedStudent != null) {
                    val action = StudentListFragmentDirections.actionStudentListToAddEditStudent(
                        studentName = selectedStudent.studentName,
                        studentId = selectedStudent.studentId,
                        position = position // Pass the list position
                    )
                    findNavController().navigate(action)
                }
                true
            }
            R.id.context_remove -> {
                if (selectedStudent != null) {
                    studentViewModel.deleteStudent(position)
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val selectedStudent = studentViewModel.students.value?.get(info.position)

        return when (item.itemId) {
            R.id.context_edit -> {
                if (selectedStudent != null) {
                    val action = StudentListFragmentDirections.actionStudentListToAddEditStudent(
                        studentName = selectedStudent.studentName,
                        studentId = selectedStudent.studentId,
                        position = info.position
                    )
                    findNavController().navigate(action)
                }
                true
            }
            R.id.context_remove -> {
                selectedStudent?.let {
                    studentViewModel.deleteStudent(info.position)
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}
