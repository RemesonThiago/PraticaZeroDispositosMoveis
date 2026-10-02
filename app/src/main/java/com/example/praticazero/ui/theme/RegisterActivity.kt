package com.example.praticazero

import com.example.praticazero.ui.theme.PraticaZeroTheme
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praticazero.Greeting
import com.example.praticazero.HomePage
import com.example.praticazero.MainActivity
//import com.example.praticazero.finish

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PraticaZeroTheme {
                //Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterPage()

                }
            }
        }
    }
@Composable
fun RegisterPage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = LocalContext.current as RegisterActivity
    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var repetirSenha by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val modifier = modifier.fillMaxWidth(fraction = 0.9f)
        Text(
            text = "Bem-vindo/a à tela de registro!",
            fontSize = 12.sp
        )
        Spacer(modifier = modifier.size(12.dp))
        OutlinedTextField(
            value = nome,
            label = { Text(text = "Digite seu nome") },
            modifier = modifier,
            onValueChange = {
                nome = it
            }
        )
        Spacer(modifier = modifier.size(12.dp))
        OutlinedTextField(
            value = email,
            label = { Text(text = "Digite seu e-mail") },
            modifier = modifier,
            onValueChange = {
                email = it
            }
        )
        Spacer(modifier = modifier.size(12.dp))
        OutlinedTextField(
            value = senha,
            label = { Text(text = "Digite sua senha") },
            modifier = modifier,
            onValueChange = {
                senha = it
            }
        )
        Spacer(modifier = modifier.size(12.dp))
        OutlinedTextField(
            value = repetirSenha,
            label = { Text(text = "Repita sua senha") },
            modifier = modifier,
            onValueChange = {
                repetirSenha = it
            }
        )
        /*Spacer(
            modifier = Modifier.size(12.dp)
        )*/
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

        }
        Button(
            onClick = {

                if (senha == repetirSenha) {

                    Toast.makeText(
                        context,
                        "Registrado com Sucesso!",
                        Toast.LENGTH_LONG
                    ).show()

                            activity.finish()

                } else {

                    Toast.makeText(
                        context,
                        "As senhas não coincidem!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            enabled = nome.isNotEmpty() &&
                    email.isNotEmpty() &&
                    senha.isNotEmpty() &&
                    repetirSenha.isNotEmpty() && (senha == repetirSenha)
        ) {
            Text("Registrar")
        }

        Button(
            onClick = {
                nome = ""
                email = ""
                senha = ""
                repetirSenha = ""
            }
        ) {
            Text("Limpar")
        }
    }
}