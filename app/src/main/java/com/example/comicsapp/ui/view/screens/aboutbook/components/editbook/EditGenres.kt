package com.example.comicsapp.ui.view.screens.aboutbook.components.editbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsapp.ui.viewmodel.AppViewModel
import com.example.comicsapp.ui.components.button.CustomButton
import com.example.comicsapp.ui.components.button.CustomButtonStyle
import com.example.comicsapp.ui.components.checkbox.CustomCheckBox
import com.example.comicsapp.ui.components.checkbox.CustomCheckBoxStyle
import com.example.comicsapp.ui.components.textfield.ThemedTextField
import com.example.comicsapp.ui.components.textfield.ThemedTextFieldStyle


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun editBookGenres(navController: NavHostController, appViewModel: AppViewModel){

    val allGenres = appViewModel.editBook.allGenres.collectAsState()
    val editableBookGenres = appViewModel.editBook.editableBookGenres.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        val textFieldPrimary = remember { mutableStateOf(TextFieldValue("")) }

        ThemedTextField(
            value = "",
            onValueChange = { textFieldPrimary.value = it },
            style = ThemedTextFieldStyle.primary().copy(),
            placeholder = "Введите название жанра"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Justify аналог
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            for (item in allGenres.value.filter { textFieldPrimary.value.text.lowercase() in it.genreName.lowercase() }) {
                // val isChecked = remember { mutableStateOf( false ) }
                Column(
                    modifier = Modifier
                        .padding(2.dp)
                        .wrapContentSize(),
                ) {
                    CustomCheckBox(
                        text = item.genreName,
                        style = CustomCheckBoxStyle.primary().copy(
                            textStyle = MaterialTheme.typography.bodyMedium,
                        ),
                        onItemSelected = {
                            appViewModel.editBook.moveGenre(item)
                        },
                        checkBoxValue = item in editableBookGenres.value

                    )
                }
                if (item in editableBookGenres.value)
                    Text(text = item.genreName)
            }



        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            CustomButton(
                text = "Сбросить",
                onClick = {
                    appViewModel.editBook.rollback()
                },
                buttonStyle = CustomButtonStyle.error()
            )
            CustomButton(
                text = "Сохранить",
                onClick = { },
                buttonStyle = CustomButtonStyle.success()
            )
        }
    }


    /*
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val editableBookGenres = appViewModel.editBook.editableBookGenres.collectAsState()
        val editableAllGenres = appViewModel.editBook.allGenres.collectAsState()
        val selectedBookGenres = appViewModel.editBook.selectedBookGenres.collectAsState()
        var selectedGenre by remember { mutableStateOf<Genre?>(null) }

        FilterableDropdownMenu(
            items = editableAllGenres.value,
            onItemSelected =
            { genre ->
                selectedGenre = genre // Сохраняем выбранный жанр
                appViewModel.editBook.setGenre(genre)
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        FlowRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Absolute.SpaceAround // Justify аналог
        ) {
            for (item in selectedBookGenres.value){
                Box (
                    modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                ){
                    GenreBox(
                        genre = item,
                        selectedItems = editableBookGenres.value,
                        onItemSelected = {
                            appViewModel.editBook.moveGenre(item)
                        }
                    )
                }

            }
        }
        Row(){
            CustomButton("Save", {})
        }

    }

     */
}