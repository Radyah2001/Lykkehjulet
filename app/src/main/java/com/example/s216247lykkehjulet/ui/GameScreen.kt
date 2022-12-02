package com.example.s216247lykkehjulet.ui

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.s216247lykkehjulet.ui.theme.S216247LykkehjuletTheme
import com.example.s216247lykkehjulet.R

// the place where all ui elements are put
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
){
    val gameUiState by gameViewModel.uiState.collectAsState()
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        GameStatus(
            score = gameUiState.score,
            category = gameUiState.currentCategory,
            lykkehjulet = gameUiState.lykkehjulValue,
            lives = gameUiState.lives
        )
        GameLayout(
            onUserGuessChanged = { gameViewModel.updateUserGuess(it) },
            userGuess = gameViewModel.userGuess,
            onKeyboardDone = { gameViewModel.checkUserGuess() },
            currentWord = gameUiState.concealedWord,
            isGuessWrong = gameUiState.isGuessedLetterWrong
        )
        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            onClick = {gameViewModel.startGameState()
            }
        ) {
            Text(stringResource(R.string.start))
        }
        Row(modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(30.dp)) {
            Text(text = "Used letters: " + gameUiState.usedLetters, fontSize = 18.sp)
            
        }
        if (gameUiState.isGameOver){
            FinalScoreDialog(score = gameUiState.score, onPlayAgain = { gameViewModel.resetGame() })
            
        }
         else if (gameUiState.isGameLost){
            LossDialog(onPlayAgain = { gameViewModel.resetGame()})

        }

    }


}


@Composable
fun GameStatus(score: Int,category: String, lykkehjulet: String,lives: Int,   modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(48.dp),
    ) {Text(
        text = stringResource(R.string.currentCategory, category),
        fontSize = 18.sp,
    )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            text = stringResource(R.string.score,score),
            fontSize = 18.sp,
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(26.dp),
    ) {Text(
        text = stringResource(R.string.currentLives, lives),
        fontSize = 18.sp,
    )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            text = stringResource(R.string.lykkehjul,lykkehjulet),
            fontSize = 14.sp,
        )
    }

}
@Composable
fun GameLayout(
    currentWord: String,
    userGuess: String,
    isGuessWrong: Boolean,
    onUserGuessChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = currentWord,
            fontSize = 45.sp,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .alpha(1f),

        )
        OutlinedTextField(
            value = userGuess,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onUserGuessChanged,
            label = {
                if (isGuessWrong) {
                    Text(stringResource(R.string.wrong_guess))
                } else {
                    Text(stringResource(R.string.enter_your_guess))
                }
            },
            isError = isGuessWrong,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardDone() }
            ),
        )
    }

}

/*
 * Creates and shows an AlertDialog with final score.
 */
@Composable
private fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.

        },
        title = { Text(stringResource(R.string.congratulations)) },
        text = { Text(stringResource(R.string.you_scored, score)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(R.string.play_again))
            }
        }
    )
}

/*
 * Creates and shows an AlertDialog when you lose.
 */
@Composable
private fun LossDialog(
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = { Text(stringResource(R.string.you_lost)) },
        text = { Text(stringResource(R.string.try_again)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(R.string.play_again))
            }
        }
    )
}








@Preview(showBackground = true)
@Composable
    fun GameScreenPreview() {
         S216247LykkehjuletTheme{
            GameScreen()
        }
    }




