package clib.presentation.components.navigation

public interface RouteInstance<out T : NavigationRoute<*, *>> {

    public val routeDefinition: T
}
