package clib.data.type.collections.restartableflow

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
public interface RestartableMutableStateFlow<T> : RestartableStateFlow<T>, MutableStateFlow<T>
