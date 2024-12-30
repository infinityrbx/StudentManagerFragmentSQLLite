package com.example.studentmanagerfragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentmanagerfragment.models.StudentDatabaseHelper
import com.example.studentmanagerfragment.models.StudentModel

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val studentDatabaseHelper = StudentDatabaseHelper(application)
    val students = MutableLiveData<MutableList<StudentModel>?>()

    init {
        students.value = studentDatabaseHelper.getAllStudents().toMutableList()
    }

    fun saveStudent(student: StudentModel, position: Int?) {
        val currentList = students.value ?: mutableListOf()

        if (position != null && position >= 0) {
            val studentToUpdate = currentList[position]
            val updatedStudent = studentDatabaseHelper.updateStudent(student.copy(id = studentToUpdate.id!!), studentToUpdate.id!!)
            if (updatedStudent > 0) {
                currentList[position] = student.copy(id = studentToUpdate.id)
                students.value = currentList
            }
        } else {
            val newId = studentDatabaseHelper.addStudent(student)
            if (newId > 0) {
                currentList.add(student.copy(id = newId.toInt()))
                students.value = currentList
            }
        }
    }


    fun deleteStudent(position: Int) {
        val student = students.value?.get(position)
        if (student != null) {
            studentDatabaseHelper.deleteStudent(position)
            val currentList = students.value?.toMutableList()
            currentList?.removeAt(position)
            students.value = currentList
        }
    }
}


