package com.masterproject.englishapp.result

sealed class AppError(val message: String) {
    object Network : AppError("Erro de rede. Não foi possível conectar ao servidor.")
    object Server : AppError("Erro no servidor. Tente mais tarde.")
    object EmptyData : AppError("Nenhum dado disponível.")

    data class Unknown(val throwable: Throwable) : AppError(throwable.message ?: "Erro desconhecido")

    data class Custom(val msg: String) : AppError(msg)
}
