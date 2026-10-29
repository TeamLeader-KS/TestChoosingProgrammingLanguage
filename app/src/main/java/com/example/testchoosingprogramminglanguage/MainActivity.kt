package com.example.testchoosingprogramminglanguage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

sealed class Answer {
    data class Jump(val next: Int) : Answer()
    data class Result(val language: String) : Answer()
}

data class Question(val text: String, val yes: Answer, val no: Answer)

private val questions = listOf(
    Question("Хотите много зарабатывать?", Answer.Jump(1), Answer.Result("1C")),
    Question("Вы тупой?", Answer.Jump(2), Answer.Jump(12)),
    Question("Очень?", Answer.Jump(3), Answer.Jump(7)),
    Question("У вас есть друзья?", Answer.Jump(4), Answer.Result("PHP")),
    Question("Они тоже тупые?", Answer.Result("JavaScript"), Answer.Result("Ruby")),
    Question("У вас технический склад ума?", Answer.Jump(6), Answer.Jump(10)),
    Question("Вам больше 70?", Answer.Result("Assembler"), Answer.Result("Rust")),
    Question("Вы насмотрелись уроков на YouTube?", Answer.Result("Python"), Answer.Jump(8)),
    Question("Вам нравится Windows?", Answer.Result("C#"), Answer.Jump(9)),
    Question("Вы пи...р?", Answer.Result("Swift"), Answer.Result("Kotlin")),
    Question("У вас есть ноги?", Answer.Jump(11), Answer.Result("C")),
    Question("А они вам нужны?", Answer.Result("Java"), Answer.Result("C++")),
    Question("А это точно известно?", Answer.Jump(5), Answer.Result("Go")),
)


private val BgColor = Color(0xFFEBEBEB)
private val TextBlack = Color(0xFF050505)
private val AccentBlue = Color(0xFF006ED2)
private val AccentRed = Color(0xFFD20000)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { QuizScreen() }
    }
}

@Composable
fun QuizScreen() {
    var currentIndex by rememberSaveable { mutableStateOf(0) }
    var result by rememberSaveable { mutableStateOf<String?>(null) }

    fun handleAnswer(yes: Boolean) {
        val q = questions[currentIndex]
        when (val next = if (yes) q.yes else q.no) {
            is Answer.Jump -> currentIndex = next.next
            is Answer.Result -> result = next.language
        }
    }

    fun restart() {
        currentIndex = 0
        result = null
    }

    Surface(modifier = Modifier.fillMaxSize(), color = BgColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Заголовок
            Row(modifier = Modifier.padding(top = 32.dp)) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = AccentBlue)) { append("ВЫБОР ") }
                        withStyle(SpanStyle(color = AccentRed)) { append("ЯЗЫКА ПРОГРАММИРОВАНИЯ") }
                    },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }

            // Центр: вопрос или результат
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (result == null) {
                    Text(
                        text = questions[currentIndex].text,
                        color = TextBlack,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Row {
                        Text("ВАШ ЯЗЫК: ", color = AccentBlue, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(result!!, color = AccentRed, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Кнопки
            if (result == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { handleAnswer(true) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                        modifier = Modifier.size(width = 140.dp, height = 56.dp)
                    ) {
                        Text("ДА", color = Color.White, fontSize = 20.sp)
                    }
                    Button(
                        onClick = { handleAnswer(false) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                        modifier = Modifier.size(width = 140.dp, height = 56.dp)
                    ) {
                        Text("НЕТ", color = Color.White, fontSize = 20.sp)
                    }
                }
            } else {
                Button(
                    onClick = { restart() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .size(width = 200.dp, height = 56.dp)
                ) {
                    Text("Начать заново", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}