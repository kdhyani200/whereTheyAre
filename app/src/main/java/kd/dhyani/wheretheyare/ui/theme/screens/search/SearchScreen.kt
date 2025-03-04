package kd.dhyani.wheretheyare.ui.theme.screens.search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.*
import kd.dhyani.wheretheyare.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SearchScreen(navController: NavController) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var teacherNames by rememberSaveable { mutableStateOf(listOf<String>()) }
    var filteredNames by rememberSaveable { mutableStateOf(listOf<String>()) }
    var selectedTeacher by rememberSaveable { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance().getReference("teachers")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val names = snapshot.children.mapNotNull { it.key }
                teacherNames = names
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data", error.toException())
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.title))
            .padding(16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = {
                searchQuery = it
                filteredNames = if (it.isEmpty()) emptyList() else teacherNames.filter { name ->
                    name.startsWith(it, ignoreCase = true)
                }
            },
            onClearQuery = {
                searchQuery = ""
                filteredNames = emptyList()
            },
            focusManager = focusManager
        )

        SuggestionsList(filteredNames) { selectedName ->
            searchQuery = selectedName
            selectedTeacher = selectedName
            filteredNames = emptyList()
            focusManager.clearFocus()
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedTeacher?.let { teacherName ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp)
            ) {
                TeacherLocationCard(
                    teacherName = teacherName,
                    onClose = {
                        selectedTeacher = null
                        searchQuery = ""
                    }
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    focusManager: FocusManager
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = colorResource(id = R.color.search),
        shadowElevation = 4.dp
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color(colorResource(id = R.color.two).toArgb())
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = {
                        onClearQuery()
                        focusManager.clearFocus()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Search",
                            tint = Color(colorResource(id = R.color.two).toArgb())
                        )
                    }
                }
            },
            placeholder = { Text(text = "Name of faculty") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { focusManager.clearFocus() }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun SuggestionsList(suggestions: List<String>, onItemClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        suggestions.forEach { name ->
            Text(
                text = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(name) }
                    .padding(12.dp))
        }
    }
}

@Composable
fun TeacherLocationCard(teacherName: String, onClose: () -> Unit) {
    val db = FirebaseDatabase.getInstance().getReference("teachers/$teacherName")

    var teacherData by remember { mutableStateOf<Teacher?>(null) }

    LaunchedEffect(teacherName) {
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                teacherData = snapshot.getValue(Teacher::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching teacher data", error.toException())
            }
        })
    }

    teacherData?.let { teacher ->
        val currentClass =
            getCurrentClass(teacher.schedule, getCurrentTime(), teacherName, teacher.sex)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp)
                .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.search))
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(colorResource(id = R.color.two).toArgb())

                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentClass,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.two)
                    )
                }
            }
        }
    }
}

data class Teacher(
    val sex: String = "",
    val schedule: Map<String, String> = emptyMap()
)

fun getCurrentTime(): String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

fun getCurrentClass(
    schedule: Map<String, String>,
    currentTime: String,
    teacherName: String,
    sex: String
): String {
    val currentClass =
        schedule.entries.find { isTimeInRange(it.key, currentTime) }?.value ?: "Unavailable"

    val isNonTeachingTime =
        isTimeInRange("00:00-09:30", currentTime) ||
                isTimeInRange("12:30-13:30", currentTime) ||
                isTimeInRange("17:30-23:59", currentTime)

    return if (isNonTeachingTime) {
        currentClass
    } else {
        val title = if (sex == "male") "Sir" else "Mam"
        "$teacherName $title \n\n is scheduled to be\n" +
                "\n in\n\n $currentClass"
    }
}


fun isTimeInRange(range: String, currentTime: String): Boolean {
    try {
        val (start, end) = range.split("-")

        val current = currentTime.replace(":", "").toIntOrNull() ?: return false
        val startTime = start.replace(":", "").toIntOrNull() ?: return false
        val endTime = end.replace(":", "").toIntOrNull() ?: return false

        return if (startTime <= endTime) {
            current in startTime..endTime
        } else {
            current >= startTime || current <= endTime
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}
