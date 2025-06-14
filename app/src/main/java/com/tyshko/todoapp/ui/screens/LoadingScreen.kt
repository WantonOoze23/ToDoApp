package com.tyshko.todoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random


@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    onFinish: () -> Unit
) {
    val totalSteps = 6
    val delayMillis = 1000L

    var activeBoxIndex by remember { mutableIntStateOf(Random.nextInt(0, 4)) }


    LaunchedEffect(Unit) {
        repeat(totalSteps) {
            activeBoxIndex = Random.nextInt(0, 4)
            delay(delayMillis)
        }
        onFinish()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        for (row in 0..1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (col in 0..1) {
                    val index = row * 2 + col
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(8.dp)
                            .background(
                                if (index == activeBoxIndex) Color.Red else Color.Green,
                                shape = RoundedCornerShape(16.dp) 
                            )
                    )
                }
            }
        }
    }
}