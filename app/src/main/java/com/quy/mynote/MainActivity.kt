package com.quy.mynote

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quy.mynote.database.MyViewModelFactory
import com.quy.mynote.database.Note
import com.quy.mynote.database.NoteViewModel
import com.quy.mynote.ui.theme.MyNoteTheme
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : ComponentActivity(),LifecycleOwner {
    @SuppressLint("UnrememberedMutableState", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNoteTheme {
                val titleState = remember {
                    mutableStateOf(value = "")
                }
                val noteState = remember {
                    mutableStateOf(value = "")
                }

                val noteViewModel = viewModel<NoteViewModel>(
                    factory = MyViewModelFactory(applicationContext as Application)
                )
                var listNote by mutableStateOf(listOf<Note>())

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBar()
                    }
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(20.dp),
                    ) {

                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White
                            ),
                            value = titleState.value,
                            onValueChange = {
                                titleState.value = it
                            },
                            label = {
                                Text(text = "Title", fontWeight = FontWeight.Bold)
                            }
                        )

                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White
                            ),
                            value = noteState.value,
                            onValueChange = {
                                noteState.value = it
                            },
                            label = {
                                Text(text = "Add a note", fontWeight = FontWeight.Bold)
                            }
                        )
                        Button(
                            onClick = {
                                val sdf = SimpleDateFormat("EEE, dd MMMM hh:mm a")
                                val currentDate = sdf.format(Date())
                                noteViewModel.addNote(
                                    Note(
                                        title = titleState.value,
                                        content = noteState.value,
                                        time = currentDate
                                    )
                                )
                                titleState.value = ""
                                noteState.value = ""
                            },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("Save", style = MaterialTheme.typography.button)
                        }
                        Divider(modifier = Modifier.padding(vertical = 15.dp))

                        noteViewModel.readAllData.observeForever(androidx.lifecycle.Observer { notes ->
                            listNote = notes
                        })
                        ListNote(data = listNote) { note ->
                            noteViewModel.deleteNote(note)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = "JetNote",
                fontSize = 22.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
        },
        contentColor = Color(0xFF2F3237),
        modifier = Modifier
            .padding(8.dp),
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_notifications_24),
                contentDescription = "notifications image",
                modifier = Modifier
                    .size(30.dp)
                    .fillMaxWidth(),
                tint = Color(0xFF2F3237)
            )
        },
        backgroundColor = Color(0xFFDADDE2)
    )
}


@Composable
fun ListNote(data: List<Note>, deleteValue: (Note) -> Unit) {

    LazyColumn {
        items(data) { note ->

            Surface(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp))
                    .fillMaxWidth(),
                color = Color(0xFFDFE6EB),
                elevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.subtitle2
                    )
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = note.time,
                        style = MaterialTheme.typography.caption
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.End,
                ) {
                    IconButton(onClick = {
                        deleteValue(note)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                            contentDescription = "delete image",
                            modifier = Modifier
                                .size(30.dp)
                                .fillMaxWidth(),
                            tint = Color(0xFF2F3237)
                        )
                    }
                }
            }
        }
    }
}
