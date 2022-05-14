package enhanced.search.service

import enhanced.search.dto.User
import enhanced.search.utils.SHAPasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ldap.core.AttributesMapper
import org.springframework.ldap.core.DirContextAdapter
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.support.LdapNameBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.naming.Name
import javax.naming.directory.Attributes

@Service
class UserService(
    @Autowired val ldapTemplate: LdapTemplate
) {
    private val passwordEncoder: PasswordEncoder = SHAPasswordEncoder()

    fun create(
        uid: String,
        firstName: String,
        lastName: String,
        password: String,
        token: String
    ) {
        val dn: Name = buildName(uid)

        ldapTemplate.bind(
            DirContextAdapter(dn).apply {
                setAttributeValues("objectClass", arrayOf("top", "person", "inetOrgPerson", "organizationalPerson"))
                setAttributeValue("cn", firstName)
                setAttributeValue("sn", lastName)
                setAttributeValue("description", token)
                setAttributeValue("userPassword", passwordEncoder.encode(password))
            }
        )
    }

    fun search(username: String): User? {
        return ldapTemplate
            .search(
                "ou=users",
                "uid=$username",
                AttributesMapper { attrs: Attributes ->
                    User(
                        id = buildName(attrs["uid"].get() as String),
                        uid = attrs["uid"].get() as String,
                        firstName = attrs["cn"].get() as String,
                        secondName = attrs["sn"].get() as String,
                        accessToken = attrs["description"].get() as String
                    )
                } as AttributesMapper<User?>)[0]
    }

    private fun buildName(uid: String): Name {
        return LdapNameBuilder
            .newInstance()
            .add("ou", "users")
            .add("uid", uid)
            .build()
    }
}