package com.example.s216247lykkehjulet.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.s216247lykkehjulet.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set
    private var isSpinnedValueFallit: Boolean = false

    fun startGameState(){
        // Normal round in the game
        _uiState.update { currentState ->
            currentState.copy(
                isGuessedWordWrong = false,
                currentWord = pickRandomWord(),
                currentCategory = currentCategory,
                lykkehjulValue = pickRandomValue(),
                score = 0
            )
        }

    }
    fun updateGameState(updatedScore: Int, updatedLives: Int){
        _uiState.update {
            currentState->currentState.copy(
                isGuessedWordWrong = false,
                score = updatedScore,
                lives = updatedLives,
                lykkehjulValue = pickRandomValue()
                )
    }
    }
    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }
    fun countOccurrences(s: String, ch: Char): Int {
        return s.count { it == ch }
    }
    fun checkUserGuess() {
        if (currentWord.contains(userGuess, ignoreCase = true)) {
            val occurences: Int = countOccurrences(currentWord, userGuess[0])
            // User's guess is correct, increase the score
            // and call updateGameState() to prepare the game for next round
            if (isSpinnedValueFallit){
                updateGameState(0, _uiState.value.lives)
            }
            else {
                val updatedScore = _uiState.value.score.plus(_uiState.value.lykkehjulValue.toInt() * occurences)
                updateGameState(updatedScore,uiState.value.lives)
            }
        } else {
            // User's guess is wrong, show an error

            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true, lives = _uiState.value.lives.minus(1), score = _uiState.value.score, lykkehjulValue = pickRandomValue())
            }
        }
        // Reset user guess
       // updateUserGuess("")
    }


    // ord brugt i spillet
    lateinit var currentCategory: String
    lateinit var currentWord: String
    lateinit var lykkehjulValue: String


    fun pickRandomWord(): String {
        currentCategory = kategorier.random()
        when (currentCategory){
            "dyr" -> currentWord = dyr.random()
            "bogtitler" -> currentWord = bogtitler.random()
            "sport" -> currentWord = sport.random()
            "film" -> currentWord = film.random()
            "serier" -> currentWord = serier.random()
        }
        return currentWord
        }

    fun pickRandomValue(): String {
        lykkehjulValue = lykkehjulet.random()
        isSpinnedValueFallit = lykkehjulValue.equals("fallit", ignoreCase = true)
        return  lykkehjulValue
    }


}