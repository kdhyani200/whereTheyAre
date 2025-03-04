package kd.dhyani.wheretheyare.data.repository

import kd.dhyani.wheretheyare.data.model.Faculty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FacultyRepository {
    private val facultyList = MutableStateFlow<List<Faculty>>(emptyList())

    init {
        facultyList.value = listOf(
            Faculty("1", "Dr. Sharma", "CS", "Room 101"),
            Faculty("2", "Prof. Verma", "IT", "Room 202")
        )
    }

    fun getFacultyList(): Flow<List<Faculty>> = facultyList
}
