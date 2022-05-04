package enhanced.search.service

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
        val dn: Name = LdapNameBuilder
            .newInstance()
            .add("ou", "users")
            .add("uid", uid)
            .build()

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

    fun search(username: String): String? {
        return ldapTemplate
            .search(
                "ou=users",
                "uid=$username",
                AttributesMapper { attrs: Attributes ->
                    attrs["description"].get() as String
                } as AttributesMapper<String?>)[0]
    }
}