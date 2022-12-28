package io.culturebook.data.encoders

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.Cipher.ENCRYPT_MODE

private fun generateJavaPublicKey(publicKey: String): PublicKey {
    val keyFactory = KeyFactory.getInstance("RSA")
    val publicKeyDecoded = Base64.decode(publicKey, Base64.NO_WRAP)
    val publicEncodedKeySpec = X509EncodedKeySpec(publicKeyDecoded)
    return keyFactory.generatePublic(publicEncodedKeySpec)
}

fun String.encrypt(key: String): String {
    val encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING").apply {
        init(ENCRYPT_MODE, generateJavaPublicKey(key))
    }
    val stringBytes = toByteArray(Charsets.ISO_8859_1)
    val secretBytes = encryptCipher.doFinal(stringBytes)

    return String(Base64.encode(secretBytes, Base64.NO_WRAP), StandardCharsets.ISO_8859_1)
}