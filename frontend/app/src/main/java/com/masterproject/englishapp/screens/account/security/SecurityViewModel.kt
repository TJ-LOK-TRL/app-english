package com.masterproject.englishapp.screens.account.security

import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.user.UserRepository
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.settings.BaseUserModifierViewModel
import com.masterproject.englishapp.user.UserContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor(
    userContext: UserContext,
    userRepository: UserRepository,
    val uiEventService: UiEventService
) : BaseUserModifierViewModel(userContext, userRepository, uiEventService) {
    fun sendPasswordReset() {
        val email = userContext.currentUser?.email
        if (email.isNullOrBlank()) {
            uiEventService.showError("Email não encontrado.")
            return
        }

        viewModelScope.launch {
            try {
                userContext.sendPasswordReset()
                uiEventService.showSuccess("Verifica a tua caixa de entrada em $email")
            } catch (e: Exception) {
                uiEventService.showError("Não foi possível enviar o email agora.")
            }
        }
    }

    fun deleteAccount(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                userContext.deleteAccount()
                uiEventService.showWarning("Conta eliminada permanentemente.")
                onComplete()
            } catch (e: Exception) {
                uiEventService.showError("Erro. Por favor faça login novamente antes de apagar a conta.")
            }
        }
    }
}