package com.yuri.poker

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yuri.poker.Model.LoginViewModel
import com.yuri.poker.ui.theme.branco
import com.yuri.poker.ui.theme.cinzaObjetos
import com.yuri.poker.ui.theme.preto

@Composable
fun LoginActivity(navController: NavController, viewModel: LoginViewModel){
    Scaffold(
        Modifier.fillMaxSize(),
        containerColor = preto
    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logopokerbranca),
                contentDescription = null,
                modifier = Modifier.width(80.dp)
                    .padding(0.dp, 30.dp),
            )
        }
        popUpNickname(onOkClick = { apelido ->
            if(apelido.isNullOrEmpty()){
            }else{
                viewModel.setarApelidoUsuario(apelido)
                navController.navigate("mesa")
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun popUpNickname(onOkClick: (String) -> Unit){
    val scale = remember { androidx.compose.animation.core.Animatable(0f) }
    val apelido = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(durationMillis = 400, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column (
            modifier = Modifier
                .scale(scale.value)
                .background(cinzaObjetos, shape = RoundedCornerShape(15.dp))
                .padding(15.dp)
        ){
            Text(text = "Informações do Jogador", color = branco, modifier = Modifier.padding(0.dp, 15.dp))
            TextField(
                value = apelido.value,
                onValueChange = { apelido.value = it },
                textStyle = TextStyle(color = branco),
                label = { Text(text = "Seu apelido", color = branco) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = cinzaObjetos,
                    cursorColor = branco,
                    focusedIndicatorColor = branco,
                    unfocusedIndicatorColor = branco,
                    focusedLabelColor = branco,
                    unfocusedLabelColor = branco
                )
            )

            Button(onClick = {
                onOkClick(apelido.value)
            }, Modifier
                .align(alignment = Alignment.End)
                .padding(0.dp, 10.dp, 0.dp, 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = cinzaObjetos, contentColor = branco)
            ) {
                Text(text = "Continuar", color = branco)

            }
        }
    }
}

@Composable
@Preview
fun LoginPreview(){
    LoginActivity(NavController(LocalContext.current), viewModel())
}