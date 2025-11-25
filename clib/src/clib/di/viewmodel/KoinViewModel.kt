package clib.di.viewmodel

import clib.presentation.viewmodel.ViewModel
import org.koin.core.component.KoinComponent

public abstract class KoinViewModel<T : Any> : ViewModel<T>(), KoinComponent
