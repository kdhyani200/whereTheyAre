package kd.dhyani.wheretheyare.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kd.dhyani.wheretheyare.data.model.Faculty
import kd.dhyani.wheretheyare.data.repository.FacultyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: FacultyRepository) : ViewModel() {

    private val _facultyList = MutableStateFlow<List<Faculty>>(emptyList())
    val facultyList: StateFlow<List<Faculty>> get() = _facultyList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        fetchFacultyList()
    }

    private fun fetchFacultyList() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000)
            repository.getFacultyList().collectLatest { list ->
                _facultyList.value = list
                _isLoading.value = false
            }
        }
    }
}
