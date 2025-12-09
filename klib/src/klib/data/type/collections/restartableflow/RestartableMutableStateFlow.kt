package klib.data.type.collections.restartableflow

import kotlinx.coroutines.flow.MutableStateFlow

public interface RestartableMutableStateFlow<T> : RestartableStateFlow<T>, MutableStateFlow<T>
