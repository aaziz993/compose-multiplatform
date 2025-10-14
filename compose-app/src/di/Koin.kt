package di

import di.module.CommonModule
import di.module.PlatformModule
import org.koin.core.KoinApplication
import org.koin.ksp.generated.module

public fun KoinApplication.koinConfiguration() {
    printLogger()

    modules(
        CommonModule().module,
        PlatformModule().module,
    )
}
