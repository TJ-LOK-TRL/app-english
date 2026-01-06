package com.masterproject.englishapp.settings

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.data.user.UserRepository
import com.masterproject.englishapp.data.user.mapper.toEntity
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.result.AppError
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.user.UserContext
import com.masterproject.englishapp.user.UserModel
import com.masterproject.englishapp.utils.toAppError
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseUserModifierViewModel(
    val userContext: UserContext,
    val userRepository: UserRepository,
    private val uiEventService: UiEventService
) : ViewModel() {

    val userState: StateFlow<UserModel?> = snapshotFlow { userContext.currentUser }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = userContext.currentUser
        )

    fun performUpdate(updatedUser: UserModel, onResult: (AppResult<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                userContext.setUser(updatedUser)
                userRepository.updateUser(updatedUser.id, updatedUser.toEntity())
                uiEventService.showSuccess("Alterações salvas com sucesso!")
                onResult(AppResult.Success(Unit))
            } catch (e: Exception) {
                uiEventService.showError("Erro ao salvar: ${e.localizedMessage}")
                onResult(AppResult.Error(e.toAppError()))
            }
        }
    }
}