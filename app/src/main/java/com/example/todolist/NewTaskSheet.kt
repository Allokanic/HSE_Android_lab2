package com.example.todolist

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime

class NewTaskSheet(private var taskItem: TaskItem?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if (taskItem != null) {
            binding.taskTitle.text = "Edit task"
            val editable = Editable.Factory.getInstance()
            binding.name.text = editable.newEditable(taskItem!!.name)
            binding.desc.text = editable.newEditable(taskItem!!.desc)
            if (taskItem!!.dueTime != null) {
                dueTime = taskItem!!.dueTime!!
                updateTimeButtonText()
            }
        } else {
            binding.taskTitle.text = "New task"
        }

        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
        binding.saveButton.setOnClickListener {
            saveAction()
        }
        binding.timePickerButton.setOnClickListener{
            openTimePicker()
        }
        binding.dropTimeButton.setOnClickListener{
            flushDueTime()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun flushDueTime() {
        if (dueTime == null) {
            Toast.makeText(context, "nothing to drop", Toast.LENGTH_SHORT).show()
        } else {
            dueTime = null
            binding.timePickerButton.text = "select time"
        }
    }

    private fun openTimePicker() {
        if (dueTime == null) {
            dueTime = LocalTime.now()
        }
        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Due time")
        dialog.show()
    }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d", dueTime!!.hour, dueTime!!.minute)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveAction() {
        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()
        if (taskItem == null && "" == name) {
            Toast.makeText(context, "Can't create empty task", Toast.LENGTH_SHORT).show()
        } else {
            val newTask = TaskItem(name, desc, dueTime, null)
            taskViewModel.addTaskItem(newTask)
            binding.name.setText("")
            binding.desc.setText("")
            dismiss()
        }
    }

}








