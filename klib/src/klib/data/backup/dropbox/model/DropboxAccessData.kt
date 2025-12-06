package klib.data.backup.dropbox.model

public data class DropboxAccessData(
    val codeVerifier: String,
    val codeChallenge: String,
)
