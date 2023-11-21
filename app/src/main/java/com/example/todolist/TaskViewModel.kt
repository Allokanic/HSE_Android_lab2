package com.example.todolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalTime
import java.util.Collections

class TaskViewModel: ViewModel()
{
    var taskItems = MutableLiveData<MutableList<TaskItem>>()

    init {
        taskItems.value = mutableListOf()
    }

    fun addTaskItem(newTaskItem: TaskItem) {
        val list = taskItems.value
        list!!.add(newTaskItem)
        sortTasks(list)
        taskItems.postValue(list)
    }

    fun completeTaskItem(updateTaskItem: TaskItem) {
        val list = taskItems.value
        val task = list!!.find{ taskItem -> taskItem.id == updateTaskItem.id }
        if (task != null) {
            completeTask(task)
            sortTasks(list)
        }
        taskItems.postValue(list)
    }

    private fun completeTask(original: TaskItem) {
        if (original.completedDate != null) {
            original.completedDate = null
        } else {
            original.completedDate = LocalTime.now()
        }
    }

    private fun sortTasks(tasks: MutableList<TaskItem>) {
        tasks.sortWith { a, b ->
            if (a.isCompleted() && b.isCompleted()) {
                a.completedDate!!.compareTo(b.completedDate)
            } else if (!a.isCompleted() && !b.isCompleted()) {
                if (a.dueTime != null && b.dueTime != null) {
                    a.dueTime!!.compareTo(b.dueTime)
                } else {
                    a.name.compareTo(b.name)
                }
            } else if (a.isCompleted() && !b.isCompleted()) {
                1
            } else {
                -1
            }
        }
    }
}