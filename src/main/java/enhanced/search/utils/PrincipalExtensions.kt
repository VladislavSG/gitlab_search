package enhanced.search.utils

import java.security.Principal

fun Principal.getFullName(): String {
    return this.name
}