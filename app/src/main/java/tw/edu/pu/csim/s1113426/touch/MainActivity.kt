package tw.edu.pu.csim.s1113426.touch

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode.Companion.Points
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import tw.edu.pu.csim.s1113426.touch.ui.theme.TouchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TouchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    DrawCircle()
                    DrawPath()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row {
            Text(text = "多指觸控Compose實例",
                fontFamily = FontFamily(Font(R.font.finger)),
                fontSize = 25.sp,
                color = Color.Blue)
            Image(
                painter = painterResource(id = R.drawable.hand),
                contentDescription = "手掌圖片",
                alpha = 0.7f,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Blue)
            )
        }
        Text(text = "作者：林彗靖",
            fontFamily = FontFamily(Font(R.font.finger)),
            fontSize = 25.sp,
            color = Color.Black)
    }

}
@OptIn(ExperimentalComposeUiApi::class)

@Composable
fun DrawCircle() {
    var X = remember { mutableStateListOf(0f) }
    var Y = remember { mutableStateListOf(0f) }
    val handImage = ImageBitmap.imageResource(R.drawable.hand)

    var PaintColor:Color
    val colors = listOf(
        Color.Red, Color(0xFFFFA500), Color.Yellow, Color.Green,
        Color.Blue, Color(0xFF4B0082), Color(0xFF8A2BE2)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                val Fingers = event.getPointerCount()
                X.clear()
                Y.clear()
                for (i in 0..Fingers - 1) {
                    X.add( event.getX(i))
                    Y.add (event.getY(i))
                }

                true
            }

    ){
        Canvas(modifier = Modifier){
            //drawCircle(Color.Yellow, 100f, Offset(X,Y))

            for (i in X.indices) {
                val color = colors[i % colors.size]
                drawCircle(color, 100f, Offset(X[i], Y[i]))
            }

        }

    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawPath() {
    val paths = remember { mutableStateListOf<Offset>() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        paths.clear()
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        paths += Offset(event.x, event.y)
                        true
                    }
                    else -> false
                }
            }
    ){
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path()
            if (paths.isNotEmpty()) {
                path.moveTo(paths.first().x, paths.first().y)
                for (point in paths) {
                    path.lineTo(point.x, point.y)
                }
            }
            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(width = 30f, join = StrokeJoin.Round)
            )
        }

    }
}
