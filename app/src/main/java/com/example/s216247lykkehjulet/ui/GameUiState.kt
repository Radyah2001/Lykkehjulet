package com.example.s216247lykkehjulet.ui


/**
 * Data class that represents the game UI state
 */
data class GameUiState(
    val currentWord: String = "",
    val currentCategory: String = "",
    val score: Int = 0,
    val lives: Int = 5,
    val lykkehjulValue: String = "",
    val isGuessedWordWrong: Boolean = false,
    val isGuessedLetterWrong: Boolean = false,
    val isGameOver: Boolean = false
)