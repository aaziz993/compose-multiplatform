package gradle

public val isCI: Boolean = System.getenv("CI_VERSION") != null
