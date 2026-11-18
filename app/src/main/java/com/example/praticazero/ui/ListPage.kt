
package com.example.praticazero.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praticazero.MainViewModel
import com.example.praticazero.ui.model.City
import com.example.praticazero.ui.model.Weather

@Composable
fun ListPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val cityList = viewModel.cities
    val activity = LocalActivity.current as Activity

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(
                items = cityList,
                key = { it.name }
            ) { city ->

                CityItem(
                    city = city,
                    weather = viewModel.weather(city.name),

                    onClose = {
                        viewModel.remove(city)

                        Toast.makeText(
                            activity,
                            "Fechar: ${city.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },

                    onClick = {
                        viewModel.selectCity(city.name)

                        Toast.makeText(
                            activity,
                            "Cidade selecionada: ${city.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}

@Composable
fun CityItem(
    city: City,
    weather: Weather,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val desc = if (weather == Weather.LOADING) {
        "Carregando clima..."
    } else {
        weather.desc
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Rounded.FavoriteBorder,
            contentDescription = "Favorito"
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = city.name,
                fontSize = 24.sp
            )

            Text(
                text = desc,
                fontSize = 16.sp
            )
        }

        IconButton(onClick = onClose) {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Remover cidade"
            )
        }
    }
}
