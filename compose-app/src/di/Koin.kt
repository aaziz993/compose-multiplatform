package di

import org.koin.core.annotation.KoinApplication
import org.koin.core.option.viewModelScopeFactory
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.koinConfiguration

@KoinApplication
public object App

public fun koinConfiguration(): KoinAppDeclaration =
    App.koinConfiguration {
        options(
            viewModelScopeFactory(),
        )
        modules(ui.navigation.presentation.App.module())
    }
