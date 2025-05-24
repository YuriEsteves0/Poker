package com.yuri.poker.Model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var apelido by mutableStateOf("")
    private set

    fun setarApelidoUsuario(apelido: String){
        this.apelido = apelido;
    }
}