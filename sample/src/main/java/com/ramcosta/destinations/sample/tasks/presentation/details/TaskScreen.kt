package com.ramcosta.destinations.sample.tasks.presentation.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.boodschappenlijst.toast
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.destinations.sample.core.viewmodel.viewModel
import com.ramcosta.destinations.sample.destinations.AddStepDialogDestination
import com.ramcosta.destinations.sample.destinations.StepScreenDestination
import com.ramcosta.destinations.sample.tasks.domain.Step
import com.ramcosta.destinations.sample.tasks.presentation.list.TaskUiItem

@Destination(navArgsDelegate = TaskScreenNavArgs::class)
@Composable
fun TaskScreen(
    navArgs: TaskScreenNavArgs,
    navigator: DestinationsNavigator,
    viewModel: TaskDetailsViewModel = viewModel()
) {
    val task = viewModel.task.collectAsState().value
    val ctx = LocalContext.current
    if (task != null) {
        //toast(ctx, "taskvalue = ${task.title}" )
    }
    if (task == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
            //CircularProgressIndicator(
                //strokeWidth = 2.dp,
               // modifier = Modifier.fillMaxSize()
            //)
            //LinearProgressIndicator()
        }
        return
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigator.navigate(AddStepDialogDestination(navArgs.taskId)) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add step button",
                    tint = Color.White
                )
            }
        }
    ) {
        val dd = it //dit is om it te gebruiken, moet niet
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Completed:")

                Checkbox(
                    checked = task.completed,
                    onCheckedChange = viewModel::onTaskCheckedChange
                )
            }

            Spacer(Modifier.height(16.dp))

            val steps = viewModel.steps.collectAsState().value
            Text("Steps: (aantal steps = ${steps.size})")
            //Text("aantal task.steps = ${TaskUiItem}") //nok

            LazyColumn {
                items(steps) { step ->
                    Text("title = ${step.title}" + ", taskId = ${step.taskId}")
                    Text("id = ${step.id}")
                }
            }
            LazyColumn {
                items(steps) { step ->
                    StepItem(
                        step = step,
                        onStepClicked = {
                            navigator.navigate(StepScreenDestination(step.id))
                        },
                        onCheckedChange = { viewModel.onStepCheckedChanged(step, it) }
                    )
                }
            }
        }
    }
}

@Composable
fun StepItem(
    step: Step,
    onStepClicked: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStepClicked() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = step.completed,
            onCheckedChange = onCheckedChange
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(step.title)
    }
}
