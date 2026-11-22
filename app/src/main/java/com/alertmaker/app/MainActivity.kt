package com.alertmaker.app

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlertMakerScreen()
        }
    }
}

@Composable
fun AlertMakerScreen() {
    var selectedVideo by remember { mutableStateOf<Uri?>(null) }
    var selectedFormat by remember { mutableStateOf("WebM") }
    var similarity by remember { mutableFloatStateOf(0.15f) }
    var softness by remember { mutableFloatStateOf(0.08f) }
    var crf by remember { mutableFloatStateOf(32f) }
    var cpuUsed by remember { mutableFloatStateOf(4f) }
    var showBefore byتمام. نبدأ بواجهة التطبيق. **الكود ده يضيف اختيار فيديو، إعدادات المعالجة، خيارات الصيغة، ومعاينة بخلفية مربعات. لكنه لا يزيل الخلفية ولا يصدر ملفًا بعد**؛ ده يحتاج إضافة FFmpeg وربطه لاحقًا. زر التصدير هيعرض رسالة واضحة بدل ما يوهمك إن المعالجة اشتغلت.

افتح `app/src/main/java/com/alertmaker/app/MainActivity.kt` في GitHub، اضغط **Edit**، امسح محتوى الملف كله، والصق الكود التالي:

```kotlin
package com.alertmaker.app

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AlertMakerScreen()
            }
        }
    }
}

@Composable
fun AlertMakerScreen() {
    var selectedVideo by remember { mutableStateOf<Uri?>(null) }
    var selectedFormat by remember { mutableStateOf("WebM") }
    var similarity by remember { mutableFloatStateOf(0.15f) }
    var softness by remember { mutableFloatStateOf(0.08f) }
    var crf by remember { mutableFloatStateOf(32f) }
    var cpuUsed by remember { mutableFloatStateOf(4f) }
    var showBefore by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("اختر فيديو للبدء") }

    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedVideo = uri
        status = if (uri != null) "تم اختيار الفيديو. معالجة FFmpeg غير مضافة بعد."
        else "لم يتم اختيار فيديو"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF10121C), Color(0xFF24213A))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "AlertMaker",
            color = Color.White,
            fontSize = 28.sp
        )
        Text(
            text = "إزالة خلفية الفيديو وتصديره بخلفية شفافة",
            color = Color.LightGray
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF171A28)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("1. اختيار الفيديو", color = Color.White)

                Button(
                    onClick = { videoPicker.launch("video/\*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (selectedVideo == null) "اختيار فيديو" else "اختيار فيديو آخر")
                }

                Text(
                    text = if (selectedVideo == null) "لم يتم اختيار فيديو"
                    else "تم اختيار فيديو",
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF171A28)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("2. معاينة الشفافية", color = Color.White)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .background(Color(0xFF282A35), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Checkerboard(
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = if (selectedVideo == null) {
                            "اختر فيديو لعرضه هنا"
                        } else if (showBefore) {
                            "عرض قبل إزالة الخلفية"
                        } else {
                            "معاينة الشفافية\n(ستظهر بعد إضافة معالجة الفيديو)"
                        },
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = { showBefore = !showBefore },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedVideo != null
                ) {
                    Text(if (showBefore) "عرض بعد المعالجة" else "مقارنة قبل / بعد")
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF171A28)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("3. إعدادات إزالة الخلفية", color = Color.White)

                SettingSlider(
                    label = "Similarity",
                    value = similarity,
                    valueText = "%.2f".format(similarity),
                    range = 0f..1f,
                    onValueChange = { similarity = it }
                )

                SettingSlider(
                    label = "نعومة الحواف",
                    value = softness,
                    valueText = "%.2f".format(softness),
                    range = 0f..1f,
                    onValueChange = { softness = it }
                )

                SettingSlider(
                    label = "جودة CRF",
                    value = crf,
                    valueText = crf.toInt().toString(),
                    range = 0f..63f,
                    onValueChange = { crf = it }
                )

                SettingSlider(
                    label = "سرعة المعالجة cpu-used",
                    value = cpuUsed,
                    valueText = cpuUsed.toInt().toString(),
                    range = 0f..8f,
                    onValueChange = { cpuUsed = it }
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF171A28)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("4. صيغة التصدير", color = Color.White)

                listOf("WebM", "MOV", "PNG Sequence").forEach { format ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedFormat == format,
                            onClick = { selectedFormat = format }
                        )
                        Text(format, color = Color.White)
                    }
                }

                Text("التقدم: 0%", color = Color.LightGray)
                LinearProgressIndicator(
                    progress = 0f,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("الوقت المنقضي: --     الوقت المتبقي: --", color = Color.LightGray)

                Button(
                    onClick = {
                        status = "التصدير غير متاح بعد: يلزم إضافة وربط FFmpeg."
                    },
                    enabled = selectedVideo != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("تصدير \$selectedFormat")
                }

                Text(status, color = Color(0xFFFFD180), fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun SettingSlider(
    label: String,
    value: Float,
    valueText: String,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Text("\$label: \$valueText", color = Color.White)
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = range
    )
}

@Composable
fun Checkerboard(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val cellSize = 24.dp.toPx()
        val light = Color(0xFFD7D7D7)
        val dark = Color(0xFF888888)

        var row = 0
        var y = 0f
        while (y < size.height) {
            var x = 0f
            var column = 0
            while (x < size.width) {
                drawRect(
                    color = if ((row + column) % 2 == 0) light else dark,
                    topLeft = androidx.compose.ui.geometry.Offset(x, y),
                    size = androidx.compose.ui.geometry.Size(cellSize, cellSize)
                )
                x += cellSize
                column++
            }
            y += cellSize
            row++
        }
    }
}
