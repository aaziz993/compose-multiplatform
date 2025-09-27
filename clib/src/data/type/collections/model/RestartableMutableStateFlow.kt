package data.type.collections.model

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
public interface RestartableMutableStateFlow<T> : RestartableStateFlow<T>, MutableStateFlow<T>
