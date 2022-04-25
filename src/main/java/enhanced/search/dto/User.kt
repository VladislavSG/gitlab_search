package enhanced.search.dto

import org.springframework.ldap.odm.annotations.Attribute
import org.springframework.ldap.odm.annotations.Entry
import org.springframework.ldap.odm.annotations.Id
import javax.naming.Name

@Entry(
    base = "ou=users",
    objectClasses = ["top", "inetOrgPerson", "person", "organizationalPerson"]
)

data class User(
    @Id var id: Name? = null,
    @Attribute(name = "uid") var uid: String? = null,
    @Attribute(name = "password") var password: String? = null,
    @Attribute(name = "cn") var firstName: String? = null,
    @Attribute(name = "sn") var secondName: String? = null,
    @Attribute(name = "description") var accessToken: String? = null
)