package com.jdosantos.gratitudewavev1.utils

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

object EncryptionUtils {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    //TODO: add custom secret key
    private const val SECRET_KEY = ""

    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM))
        val encryptedValue = cipher.doFinal(value.toByteArray())
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }

    fun decrypt(encryptedValue: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM))
        val decryptedValue = cipher.doFinal(Base64.decode(encryptedValue, Base64.DEFAULT))
        return String(decryptedValue)
    }
}
