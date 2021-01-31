package me.sttimort.webkt.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import me.sttimort.webkt.core.compose.Container
import me.sttimort.webkt.core.compose.Page
import me.sttimort.webkt.core.compose.Text
import me.sttimort.webkt.core.compose.observe
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter


private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")

fun ClockPage() = Page(
    styles = listOf("https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css")
) {
    val currentTime = observe(initialValue = now()) {
        emitTimePeriodically(period = Duration.ofSeconds(1))
    }

    Container {
        Text("Server time — ${currentTime.format(formatter)}")
    }
}

private suspend fun emitTimePeriodically(period: Duration) = flow<LocalDateTime> {
    while (true) {
        emit(now())
        delay(period.toMillis())
    }
}