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
    init {
        resetGame()
    }
    fun resetGame(){
        _uiState.value = GameUiState(
            currentWord = pickRandomWord(),
            currentCategory = currentCategory,
            usedLetters = "",
            score = 0,
            lives = 5,
            lykkehjulValue = pickRandomValue(),
            isGuessedWordWrong = false,
            isGameOver = false,
            isGameLost = false,
            concealedWord = concealWord(currentWord)
        )
    }
    private fun concealWord(currentWord: String): String {
        var concealedWord = ""
        val length: Int = currentWord.length
        for (i in 1..length){
            concealedWord += "_"
        }

        return concealedWord
    }

    fun startGameState() {
        // Normal round in the game
        _uiState.update { currentState ->
            currentState.copy(
                isGuessedWordWrong = false,
                currentWord = pickRandomWord(),
                currentCategory = currentCategory,
                lykkehjulValue = pickRandomValue(),
                score = 0,
                lives = 5,
                usedLetters = "",
                isGameOver = false,
            )
        }
        _uiState.update { currentState ->
            currentState.copy(concealedWord = concealWord(currentWord))

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
        if (currentWord.equals(userGuess,ignoreCase = true)){
            _uiState.update { currentState -> currentState.copy(isGameOver = true, score = uiState.value.lykkehjulValue.toInt()*uiState.value.currentWord.length)}
        }
        else if (currentWord.contains(userGuess, ignoreCase = true)) {
            val occurences: Int = countOccurrences(currentWord, userGuess[0])
            val temp = uiState.value.currentWord.replace(userGuess[0],'_', ignoreCase = true)
            var temp2 = uiState.value.concealedWord
            val chars = temp2.toCharArray()

            for (i in currentWord.indices){
                if (temp[i]=='_'){
                    chars[i] = userGuess[0]
                }

            }
            temp2 = String(chars)



            _uiState.update { currentState -> currentState.copy(concealedWord = temp2)      }

            if (_uiState.value.usedLetters.contains(userGuess, ignoreCase = true)){
                updateGameState(uiState.value.score,uiState.value.lives.minus(1))

            }
            if (_uiState.value.lives == 0){
                _uiState.update { currentState -> currentState.copy(isGameLost = true) }
            }
            if (String(chars)==currentWord){
                _uiState.update { currentState -> currentState.copy(isGameOver = true) }
            }






            // User's guess is correct, increase the score
            // and call updateGameState() to prepare the game for next round
            if (isSpinnedValueFallit){
                updateGameState(0, _uiState.value.lives)
            }
            else {
                val updatedScore = _uiState.value.score.plus(_uiState.value.lykkehjulValue.toInt() * occurences)
                updateGameState(updatedScore,uiState.value.lives)
                if (uiState.value.usedLetters.contains(currentWord, ignoreCase = true))
                {
                    _uiState.update { currentState -> currentState.copy(usedLetters = uiState.value.usedLetters) }
                }
                else
                _uiState.update { currentState -> currentState.copy(usedLetters = _uiState.value.usedLetters.plus(userGuess)) }
            }
        } else {
            // User's guess is wrong, show an error

            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true, lives = _uiState.value.lives.minus(1), score = _uiState.value.score, lykkehjulValue = pickRandomValue())

            }
            if (uiState.value.usedLetters.contains(uiState.value.currentWord, ignoreCase = true))
            {
                _uiState.update { currentState -> currentState.copy(usedLetters = _uiState.value.usedLetters) }
            }
            else
                _uiState.update { currentState ->
                    currentState.copy(usedLetters = _uiState.value.usedLetters.plus(userGuess))
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