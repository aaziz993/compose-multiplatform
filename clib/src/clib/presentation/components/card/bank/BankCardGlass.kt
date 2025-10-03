package clib.presentation.components.card.bank

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.components.card.bank.model.CardType
import clib.presentation.components.card.bank.model.CreditCard

@Composable
public fun BankCardGlass(
    card: CreditCard,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardGradient = when (card.type) {
        CardType.PLATINUM -> listOf(Color(0xFFE5E4E2), Color(0xFFC0C0C0))
        CardType.GOLD -> listOf(Color(0xFFFFD700), Color(0xFFFFA500))
        CardType.BLACK -> listOf(Color(0xFF2C2C2C), Color(0xFF000000))
        else -> listOf(Color(0xFF1976D2), Color(0xFF1565C0))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = cardGradient.map { it.copy(alpha = 0.9f) },
                    ),
                ),
        ) {
            // Glass overlay effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent,
                            ),
                            center = Offset(0.3f, 0.2f),
                        ),
                    ),
            )

            Column(
                modifier = Modifier.padding(24.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column {
                        Text(
                            text = card.bankName,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = card.cardType,
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    // Contactless payment icon
                    Icon(
                        imageVector = Icons.Filled.Contactless,
                        contentDescription = "Contactless",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(32.dp),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Card number with glassmorphism chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // EMV Chip
                    Box(
                        modifier = Modifier
                            .size(32.dp, 24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.4f),
                                        Color.White.copy(alpha = 0.2f),
                                    ),
                                ),
                            )
                            .border(
                                1.dp,
                                Color.White.copy(alpha = 0.3f),
                                RoundedCornerShape(4.dp),
                            ),
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = card.maskedNumber,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = "VALID THRU",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelSmall,
                        )
                        Text(
                            text = card.expiryDate,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace,
                        )
                    }

                    Text(
                        text = card.holderName,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
