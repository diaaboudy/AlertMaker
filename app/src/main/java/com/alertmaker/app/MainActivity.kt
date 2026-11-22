package com.alertmaker.app

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    var similarity by remember { mutableFloatStateOf(0.15f) }
    var softness by remember { mutableFloatStateOf(0.08f) }

    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedVideo = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0D16))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "ALERT MAKER",
            color = Color.White,
            fontSize = 25.sp
        )

        Text(
            text = "جهّز تنبيهات شفافة للبث المباشر",
            color = Color.LightGray
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF171A28)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. اختار الفيديو", color = Color.White)

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { videoPicker.launch("video/*") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B72FF)
                    )
                ) {
                    Text("اختيار فيديو")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (selectedVideo == null) {
                        "لم يتم اختيار فيديو"
                    } else {
                        "تم اختيار فيديو ✅"
                    },
                    color = Color.LightGray
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF171A28)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. إزالة الخلفية", color = Color.White)

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "معاينة الفيديو والشفافية ستظهر هنا",
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Similarity: ${"%.2f".format(similarity)}",
                    color = Color.White
                )
                Slider(
                    value = similarity,
                    onValueChange = { similarity = it },
                    valueRange = 0f..1f
                )

                Text(
                    text = "نعومة الحواف: ${"%.2f".format(softness)}",
                    color = Color.White
                )
                Slider(
                    value = softness,
                    onValueChange = { softness = it },
                    valueRange = 0f..1f
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF171A28)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. تصدير WebM شفاف", color = Color.White)

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { },
                    enabled = selectedVideo != null,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B72FF)
                    )
                ) {
                    Text("تصدير WebM")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "هنضيف معالجة الفيديو والتصدير في الخطوة التالية.",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}
