package com.finapp.core.settings.impl.repository

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.finapp.core.settings.api.repository.PasscodeRepository
import com.finapp.core.settings.impl.DataStoreKeys
import com.finapp.core.settings.impl.di.PasscodeSettingsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject
import javax.inject.Singleton

private const val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256"
private const val PBKDF2_ITERATIONS = 100_000
private const val PBKDF2_KEY_LENGTH_BITS = 256
private const val SALT_LENGTH_BYTES = 16

@Singleton
class PasscodeRepositoryImpl @Inject constructor(
    @PasscodeSettingsDataStore private val dataStore: DataStore<Preferences>
) : PasscodeRepository {

    override val isSet: Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[DataStoreKeys.PASSCODE_HASH] != null
        }

    override suspend fun set(plain: String) {
        val (salt, hash) = withContext(Dispatchers.Default) {
            val s = ByteArray(SALT_LENGTH_BYTES).also { SecureRandom().nextBytes(it) }
            s to pbkdf2(plain, s)
        }
        dataStore.edit { prefs ->
            prefs[DataStoreKeys.PASSCODE_SALT] = salt.encodeBase64()
            prefs[DataStoreKeys.PASSCODE_HASH] = hash.encodeBase64()
        }
    }

    override suspend fun verify(plain: String): Boolean = withContext(Dispatchers.Default) {
        val prefs = dataStore.data.first()
        val storedHash = prefs[DataStoreKeys.PASSCODE_HASH]?.decodeBase64() ?: return@withContext false
        val storedSalt = prefs[DataStoreKeys.PASSCODE_SALT]?.decodeBase64() ?: return@withContext false
        val computed = pbkdf2(plain, storedSalt)
        constantTimeEquals(computed, storedHash)
    }

    override suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(DataStoreKeys.PASSCODE_HASH)
            prefs.remove(DataStoreKeys.PASSCODE_SALT)
        }
    }

    private fun pbkdf2(plain: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(plain.toCharArray(), salt, PBKDF2_ITERATIONS, PBKDF2_KEY_LENGTH_BITS)
        return try {
            SecretKeyFactory.getInstance(PBKDF2_ALGORITHM).generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    private fun constantTimeEquals(a: ByteArray, b: ByteArray): Boolean {
        if (a.size != b.size) return false
        var diff = 0
        for (i in a.indices) diff = diff or (a[i].toInt() xor b[i].toInt())
        return diff == 0
    }

    private fun ByteArray.encodeBase64(): String =
        Base64.encodeToString(this, Base64.NO_WRAP)

    private fun String.decodeBase64(): ByteArray =
        Base64.decode(this, Base64.NO_WRAP)
}
