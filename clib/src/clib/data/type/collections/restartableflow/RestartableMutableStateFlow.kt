package clib.data.type.collections.restartableflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
public interface RestartableMutableStateFlow<T> : RestartableStateFlow<T>, MutableStateFlow<T>
