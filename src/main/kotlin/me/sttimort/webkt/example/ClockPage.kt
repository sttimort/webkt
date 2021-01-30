package me.sttimort.webkt.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import me.sttimort.webkt.core.compose.Page
import me.sttimort.webkt.core.compose.Text
import me.sttimort.webkt.core.compose.observe
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter


private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")

fun ClockPage() = Page {
    val currentTime = observe(initialValue = now()) {
        emitTimePeriodically(period = Duration.ofMillis(95))
    }

    Text("Server time — ${currentTime.format(formatter)}")
}

private suspend fun emitTimePeriodically(period: Duration) = flow<LocalDateTime> {
    while (true) {
        emit(now())
        delay(period.toMillis())
    }
}