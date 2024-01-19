package com.example.myapplication.addtasks.ui



import com.example.myapplication.addtasks.ui.TaskUiState.*
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.addtasks.domain.AddTaskUseCase
import com.example.myapplication.addtasks.domain.DeleteTaskUseCase
import com.example.myapplication.addtasks.domain.GetTasksUseCase
import com.example.myapplication.addtasks.domain.UpdateTaskUseCase
import com.example.myapplication.addtasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    getTasksUseCase: GetTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
): ViewModel() {
    val uiState: StateFlow<TaskUiState> = getTasksUseCase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _myTaskText = MutableLiveData<String>()
    val myTaskText: LiveData<String> = _myTaskText

    fun onDialogClose() {
        _showDialog.value = false
    }

    //Actualizamos la función para crear la tarea en la lista anterior
    //Comentamos el mensaje al log que hemos realizado inicialmente y añadimos una nueva tarea a la lista _tasks
    fun onTaskCreated() {
        onDialogClose()
        //Un viewModelScope es una corutina.
        viewModelScope.launch {
            addTaskUseCase(TaskModel(task = _myTaskText.value ?: ""))
        }

        _myTaskText.value = ""
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onTaskTextChanged(taskText: String) {
        _myTaskText.value = taskText
    }
    fun onItemRemove() {
        viewModelScope.launch {
            deleteTaskUseCase(TaskModel(task = _myTaskText.value ?: ""))
        }

    }
    fun onCheckBoxSelected() {
        viewModelScope.launch {
            updateTaskUseCase(TaskModel(task = _myTaskText.value ?: ""))
        }
    }
}