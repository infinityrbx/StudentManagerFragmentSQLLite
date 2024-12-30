package com.example.studentmanagerfragment.fragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.studentmanagerfragment.R
import com.example.studentmanagerfragment.models.StudentModel
import com.example.studentmanagerfragment.StudentViewModel

class AddEditStudentFragment : Fragment() {
    private var studentName: String? = null
    private var studentId: String? = null
    private var position: Int? = null
    private val studentViewModel: StudentViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studentName = it.getString("studentName")
            studentId = it.getString("studentId")
            position = it.getInt("position", -1).takeIf { pos -> pos >= 0 }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_student, container, false)
        val title = view.findViewById<TextView>(R.id.fragment_title)
        val editName = view.findViewById<EditText>(R.id.edit_student_name)
        val editId = view.findViewById<EditText>(R.id.edit_student_id)
        val btnSave = view.findViewById<Button>(R.id.btn_save)

        if (position != null) {
            title.text = "Edit Student"
            btnSave.text = "Save Changes"
            editName.setText(studentName)
            editId.setText(studentId)
        } else {
            title.text = "Add New Student"
            btnSave.text = "Add Student"
        }

        editName.setText(studentName)
        editId.setText(studentId)

        btnSave.setOnClickListener {
            val updatedName = editName.text.toString().trim()
            val updatedId = editId.text.toString().trim()

            if (updatedName.isNotEmpty() && updatedId.isNotEmpty()) {
                val updatedStudent = StudentModel(updatedName, updatedId, position)
                studentViewModel.saveStudent(updatedStudent, position)
                findNavController().navigateUp()
            }
        }

        return view
    }
}
