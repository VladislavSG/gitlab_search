package enhanced.search.utils

import org.springframework.security.crypto.password.PasswordEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class SHAPasswordEncoder : PasswordEncoder {

    override fun encode(charSequence: CharSequence): String {
        val digest = MessageDigest.getInstance("SHA")
        digest.update(charSequence.toString().toByteArray())
        val base64 = Base64.getEncoder().encodeToString(digest.digest())

        return "{SHA}$base64"
    }

    override fun matches(charSequence: CharSequence, s: String): Boolean {
        return encode(charSequence) == s
    }
}