package enhanced.search.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.data.ldap.repository.config.EnableLdapRepositories
import org.springframework.ldap.core.ContextSource
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.support.LdapContextSource

@Configuration
@EnableLdapRepositories(basePackages = ["enhanced.search.ldap"])
@PropertySource("classpath:application.properties")
open class LdapConfig(
    @Autowired private val env: Environment
) {

    @Bean
    open fun contextSource(): LdapContextSource {
        return LdapContextSource().apply {
            userDn = env.getProperty("ldap.userDn")
            password = env.getProperty("ldap.password")
            setBase(env.getProperty("ldap.base"))
            setUrl(env.getProperty("ldap.url"))
        }
    }

    @Bean
    open fun ldapTemplate(
        @Autowired contextSource: ContextSource
    ): LdapTemplate = LdapTemplate(contextSource)

}