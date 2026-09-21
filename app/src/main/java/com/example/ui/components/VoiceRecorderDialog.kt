package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DangerRose
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SuccessEmerald
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRecorderDialog(
    onDismiss: () -> Unit,
    onSaveVoiceMemo: (title: String, transcriptText: String, subject: String?) -> Unit
) {
    var memoTitle by remember { mutableStateOf("Audio Note: Quick Class Memo") }
    var isRecording by remember { mutableStateOf(false) }
    var recordDurationSeconds by remember { mutableIntStateOf(0) }
    var transcript by remember {
        mutableStateOf("Voice memo transcription: 'Prof mentioned that the question on Carnot cycle efficiency and Clausius inequality will be compulsory in the mid-term test.'")
    }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (isRecording) {
                delay(1000)
                recordDurationSeconds++
                if (recordDurationSeconds == 2) {
                    transcript = "Listening..."
                } else if (recordDurationSeconds == 4) {
                    transcript = "Prof mentioned that Carnot cycle efficiency formula eta = 1 - Tc/Th..."
                } else if (recordDurationSeconds >= 6) {
                    transcript = "Prof mentioned that Carnot cycle efficiency formula eta = 1 - Tc/Th and Clausius theorem is high priority for the upcoming exam."
                }
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRecording) 1.25f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("voice_recorder_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Voice-to-Text Memos",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Fast capture with automatic searchable speech transcript",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Animated Mic Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(96.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(if (isRecording) DangerRose.copy(alpha = 0.2f) else PrimaryBlue.copy(alpha = 0.15f))
                    .clickable { isRecording = !isRecording }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(if (isRecording) DangerRose else PrimaryBlue)
                ) {
                    Icon(
                        imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = "Record",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Timer & Status
            val mins = recordDurationSeconds / 60
            val secs = recordDurationSeconds % 60
            Text(
                text = String.format("%02d:%02d", mins, secs),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isRecording) DangerRose else MaterialTheme.colorScheme.onSurface
                )
            )

            Text(
                text = if (isRecording) "Recording audio... Tap to stop" else "Tap microphone to start recording",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Waveform simulation bar
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp)
            ) {
                val heights = listOf(8, 16, 24, 12, 28, 18, 10, 22, 30, 14, 20, 26, 16, 8, 20, 28, 14, 18)
                heights.forEach { h ->
                    val barH = if (isRecording) (h * (0.6f + Math.random().toFloat() * 0.8f)).dp else 4.dp
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(barH)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isRecording) PrimaryBlue else Color.Gray.copy(alpha = 0.4f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Auto-Generated Speech Transcript
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Auto Voice-to-Text Transcript", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryBlue)
                        Text("Search-Ready", fontSize = 11.sp, color = SuccessEmerald, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = transcript,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = memoTitle,
                onValueChange = { memoTitle = it },
                label = { Text("Memo Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onSaveVoiceMemo(memoTitle, transcript, "Physics")
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_voice_memo_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Voice Memo & Index Text", fontWeight = FontWeight.Bold)
            }
        }
    }
}
