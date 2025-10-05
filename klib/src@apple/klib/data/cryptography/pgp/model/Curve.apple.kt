package klib.data.cryptography.pgp.model

import klib.data.cryptography.model.Curve

internal val CURVES = mapOf(
    Curve.CURVE25519 to "curve25519",
    Curve.ED25519 to "ed25519",
    Curve.P256 to "p256",
    Curve.P384 to "p384",
    Curve.P521 to "p521",
    Curve.P521 to "p521",
    Curve.BRAINPOOLP256R1 to "brainpoolP256r1",
    Curve.BRAINPOOLP384R1 to "brainpoolP384r1",
    Curve.BRAINPOOLP512R1 to "brainpoolP512r1",
    Curve.SECP256K1 to "secp256k1",
)
