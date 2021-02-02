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
    styles = listOf("https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css",
    "https://github.com/CreateLab/css-demo/blob/main/css.css")
) {
    /*val currentTime = observe(initialValue = now()) {
        emitTimePeriodically(period = Duration.ofSeconds(1))
    }*/

    Container(classes = listOf("container-fluid")) {
        // Text("Server time — ${currentTime.format(formatter)}")

        Container(classes = listOf("row")) {
            Container(classes = listOf("col-1","px-1","bg-dark","position-fixed")) {
                Container(classes = listOf("nav","flex-column", "flex-nowrap", "vh-100", "overflow-auto", "text-white", "p-2")) {
                    Container(classes = listOf("sidebar")) {
                        Text("ИТМОВТ")
                    }
                }
            }
            Container(classes = listOf("col-9","offset-3")) {
                Container(classes = listOf("h1")) {
                    Text("Задача 1")
                }
                Container{
                    Text("Написать программу на языке Java, выполняющую соответствующие варианту действия. Программа должна соответствовать следующим требованиям:")
                }
                Container(classes = listOf("ml-1")){
                    Text("1 Она должна быть упакована в исполняемый jar-архив.")
                }
                Container(classes = listOf("ml-1")){
                    Text("2 Выражение должно вычисляться в соответствии с правилами вычисления математических выражений (должен соблюдаться порядок выполнения действий и т.д.).")
                }
                Container(classes = listOf("d-flex")){
                    Container{
                        Text("Выполнение программы необходимо продемонстрировать на сервере")
                    }
                    Container(listOf("text-danger")){
                        Text("helios.")
                    }
                }
                Container(classes = listOf("d-flex")){
                    Container{
                        Text("Вариант:")
                    }
                    Container{
                        Text("поле ввода.")
                    }
                }
                Container(classes = listOf("h1")) {
                    Text("Текст задания:")
                }
                Container{
                    Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas rhoncus, magna ut interdum sollicitudin, lorem felis congue nulla, ac efficitur metus sapien vel ipsum. Suspendisse facilisis luctus pellentesque. Praesent rhoncus nisl in lacus imperdiet, ullamcorper ornare ligula ultrices. Donec et lectus in nulla aliquet pellentesque imperdiet at odio. Etiam dolor quam, viverra vel lacus sed, ullamcorper efficitur justo. Cras ultrices velit id ullamcorper suscipit. Maecenas dictum tellus et tempor pharetra. Phasellus vel pharetra enim. Donec nisl nunc, gravida in magna et, hendrerit aliquam turpis.")
                }
            }
        }
    }
}

private suspend fun emitTimePeriodically(period: Duration) = flow<LocalDateTime> {
    while (true) {
        emit(now())
        delay(period.toMillis())
    }
}