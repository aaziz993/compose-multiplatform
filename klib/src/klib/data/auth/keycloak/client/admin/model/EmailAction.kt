package klib.data.auth.keycloak.client.admin.model

public enum class EmailAction {
    VERIFY_EMAIL,
    UPDATE_EMAIL,
    UPDATE_PASSWORD,
    CONFIGURE_TOTP,
    TERMS_AND_CONDITIONS,
    RECOVER_PASSWORD
}
