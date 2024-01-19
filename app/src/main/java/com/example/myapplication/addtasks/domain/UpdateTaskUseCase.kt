package com.example.myapplication.addtasks.domain

import com.example.myapplication.addtasks.data.TaskRepository
import com.example.myapplication.addtasks.ui.model.TaskModel
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.update(taskModel)
    }
}