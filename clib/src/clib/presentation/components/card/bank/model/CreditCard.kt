package clib.presentation.components.card.bank.model

import kotlinx.serialization.Serializable

@Serializable
public data class CreditCard(
    val bankName: String,
    val cardType: String,
    val type: CardType,
    val maskedNumber: String,
    val expiryDate: String,
    val holderName: String
)
