package com.yuri.poker

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yuri.poker.Model.Bot
import com.yuri.poker.Model.Carta
import com.yuri.poker.Model.LoginViewModel
import com.yuri.poker.Model.Naipe
import com.yuri.poker.Model.Player
import com.yuri.poker.Model.nomesBot
import com.yuri.poker.ui.theme.PokerTheme
import com.yuri.poker.ui.theme.branco
import com.yuri.poker.ui.theme.cinzaObjetos
import com.yuri.poker.ui.theme.cinzaTexto
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mesa(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    val usuario = viewModel.apelido;
    val pokerStages = listOf("pre_flop", "flop", "turn", "river", "showdown")
    var currentStage by remember { mutableStateOf(pokerStages[0]) }

    val cartasDaMesa = remember { List(5) { Carta.getCartaAleatoria() } }
    val cartasDoJogador = remember { List(2) { Carta.getCartaAleatoria() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("login")
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(Modifier.padding(paddingValues).background(Color.Black)) {
            Column(
                Modifier
                    .weight(0.2f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(82.dp)
                )
                var bot: Bot = configBot()
                Text(text = bot.nome.toString(), fontSize = 18.sp, color = cinzaTexto)
                Text(text = bot.saldo.toString(), fontSize = 20.sp, color = Color.White)
            }

            Row(
                Modifier
                    .weight(0.4f)
                    .fillMaxWidth()
            ) {
                cartasDaMesa(cartas = cartasDaMesa, mostrar = true, currentStage = currentStage)
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .padding(0.dp, 0.dp)
            ) {
                Row(
                    Modifier.padding(20.dp, 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(cinzaObjetos)
                    ) {
                        Text(text = "Call", fontSize = 18.sp)
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(cinzaObjetos)
                    ) {
                        Text(text = "Raise", fontSize = 18.sp)
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(0.4f),
                        colors = ButtonDefaults.buttonColors(cinzaObjetos)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp).rotate(90f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Row(
                        Modifier.weight(0.4f)
                    ) {
                        cartasDoJogador(cartas = cartasDoJogador, mostrar = true)
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.3f)
                            .height(125.dp)
                            .padding(end = 20.dp)
                            .background(cinzaObjetos, shape = MaterialTheme.shapes.medium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        var player: Player = configPlayer(usuario)
                        Text(text = player.nome, Modifier.padding(top = 5.dp), color = cinzaTexto)
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(54.dp)
                        )
                        Text(
                            text = player.saldo.toString(),
                            color = branco,
                            fontSize = 16.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun cartasDaMesa(cartas: List<Carta>, mostrar: Boolean, currentStage: String) {
    val scale = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(durationMillis = 400, easing = FastOutSlowInEasing)
        )
    }

    val cartasVisiveis = when (currentStage) {
        "flop" -> 3
        "turn" -> 4
        "river", "showdown" -> 5
        else -> 0
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .scale(scale.value),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Primeira fileira (3 cartas)
        Row(horizontalArrangement = Arrangement.Center) {
            for (i in 0 until 3) {
                if (i < cartasVisiveis && i < cartas.size && mostrar) {
                    cartasView(cartas[i])
                } else {
                    cartaEscondida()
                }
            }
        }

        // Segunda fileira (2 cartas)
        Row(horizontalArrangement = Arrangement.Center) {
            for (i in 3 until 5) {
                if (i < cartasVisiveis && i < cartas.size && mostrar) {
                    cartasView(cartas[i])
                } else {
                    cartaEscondida()
                }
            }
        }
    }
}

@Composable
fun cartasDoJogador(cartas: List<Carta>, mostrar: Boolean) {
    var offsetY by remember { mutableStateOf(0f) }
    var stateTriggered by remember { mutableStateOf(false) }

    val triggerDistance = with(LocalDensity.current) { 100.dp.toPx() }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(start = 20.dp)
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (-offsetY > triggerDistance) {
                            stateTriggered = !stateTriggered
                        }
                        offsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        val (_, y) = dragAmount
                        offsetY = (offsetY + y).coerceIn(-300f, 20f)
                    }
                )
            }
    ){
        for (carta in cartas) {
            if (mostrar) cartasView(carta) else cartaEscondida()
        }
    }
}

@Composable
fun cartaEscondida(){
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(120.dp)
            .background(branco, shape = MaterialTheme.shapes.medium)
            .padding(8.dp)
    ){
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "?", color = cinzaTexto, fontSize = 20.sp)
        }
    }
    Spacer(modifier = Modifier.width(8.dp))
}

@Composable
fun cartasView(carta: Carta, modifier: Modifier = Modifier){

    val corTexto = when (carta.naipe) {
        Naipe.OURO, Naipe.COPAS -> Color.Red
        Naipe.ESPADAS, Naipe.PAUS -> Color.Black
    }

    val simboloNaipe = when (carta.naipe) {
        Naipe.OURO -> "♦"
        Naipe.PAUS -> "♣"
        Naipe.ESPADAS -> "♠"
        Naipe.COPAS -> "♥"
    }

    Box(
        modifier = Modifier
            .width(80.dp)
            .height(120.dp)
            .background(branco, shape = MaterialTheme.shapes.medium)
            .padding(8.dp)
    ){
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = carta.valor.simbolo,
                color = corTexto,
                fontSize = 20.sp
            )
            Text(
                text = simboloNaipe,
                color = corTexto,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
    Spacer(modifier = Modifier.width(8.dp))
}


@Preview
@Composable
fun PokerPreview() {
    PokerTheme {
        mesa(navController = NavController(LocalContext.current))
    }
}

fun configPlayer(nomePlayer: String) : Player {
    val cartaAleatoria1: Carta = Carta.getCartaAleatoria();
    val cartaAleatoria2: Carta = Carta.getCartaAleatoria();
    val maoPlayer: MutableList<Carta> = mutableListOf(cartaAleatoria1, cartaAleatoria2);
    val player: Player = Player(nomePlayer.capitalize(), 200.00, maoPlayer,true)
    return player;
}

fun configBot() : Bot {
    val cartaAleatoria1: Carta = Carta.getCartaAleatoria();
    val cartaAleatoria2: Carta = Carta.getCartaAleatoria();
    val maoBot: MutableList<Carta> = mutableListOf(cartaAleatoria1, cartaAleatoria2);
    val bot: Bot = Bot(nomesBot.entries.random().toString(), 200.00, maoBot,true)
    return bot;
}

@Composable
fun DragToReveal() {
    var offsetY by remember { mutableStateOf(0f) }
    var stateTriggered by remember { mutableStateOf(false) }

    val triggerDistance = with(LocalDensity.current) { 100.dp.toPx() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding(32.dp)
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.roundToInt()) }
                .fillMaxWidth()
                .height(200.dp)
                .background(if (stateTriggered) Color.Green else Color.Red)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            if (-offsetY > triggerDistance) {
                                stateTriggered = !stateTriggered
                            }
                            offsetY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            val (x, y) = dragAmount
                            offsetY += y
                        }
                    )
                }
        ) {
            Text(
                text = if (stateTriggered) "Estado: Ativado" else "Estado: Desativado",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}