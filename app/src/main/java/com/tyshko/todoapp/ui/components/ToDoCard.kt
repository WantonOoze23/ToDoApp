package com.tyshko.todoapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import com.tyshko.domain.model.ToDoModel
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ToDoSwipeableCard(modifier: Modifier = Modifier) {


}

@Composable
fun ToDoCard(
    modifier: Modifier = Modifier,
    todo: ToDoModel,
    onEdit: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (todo.isCompleted) Color(0xFFD0F0C0) else Color(0xFFD0D0D0)
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = todo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Checkbox(
                    checked = todo.isCompleted,
                    onCheckedChange = onCheckedChange
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    modifier = modifier.clickable {
                        onEdit()
                    },
                    contentDescription = "Edit Todo",
                )
            }
        }
    }
}