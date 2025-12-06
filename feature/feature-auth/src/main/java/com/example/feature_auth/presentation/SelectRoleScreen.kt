package com.example.feature_auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core_ui.R
import com.example.core_ui.components.buttons.GurukulPrimaryButton
import com.example.core_ui.ui.theme.BlueGradientEnd
import com.example.core_ui.ui.theme.BlueGradientStart
import com.example.core_ui.ui.theme.CardBackground
import com.example.core_ui.ui.theme.GurukulTheme
import com.example.core_ui.ui.theme.TextInverse
import com.example.core_ui.ui.theme.TextPrimary
import com.example.core_ui.ui.theme.TextSecondary


@Preview(showBackground = true)
@Composable
fun SelectRolePreview() {
    GurukulTheme {
        SelectRoleScreen(
            onTeacherClick = {},
            onStudentClick = {}
        )
    }
}

@Composable
fun SelectRoleScreen(
    onTeacherClick: () -> Unit,
    onStudentClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        BlueGradientStart,
                        BlueGradientEnd
                    ),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            ),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "GuruKul",
                style = MaterialTheme.typography.headlineLarge,
                color = TextInverse
            )

            Text(
                text = "Tag-Line",
                style = MaterialTheme.typography.bodySmall,
                color = TextInverse
            )

            Spacer(Modifier.height(24.dp))




            RoleCard(
                title = "I’m a Teacher",
                subtitle = "Manage classes and students",
                iconRes = R.drawable.students_icon,
                onClick = onTeacherClick
            )

            Spacer(Modifier.height(12.dp))


            RoleCard(
                title = "I’m a Student",
                subtitle = "View classes and progress",
                iconRes = R.drawable.students_icon,
                onClick = onStudentClick
            )
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}