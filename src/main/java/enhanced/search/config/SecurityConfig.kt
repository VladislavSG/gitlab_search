package enhanced.search.config

import enhanced.search.utils.SHAPasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
open class SecurityConfig(
    @Autowired val contextSource: LdapContextSource,
    @Autowired private val env: Environment
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .ldapAuthentication()
            .contextSource(contextSource)
            .userSearchBase("ou=users")
            .groupSearchBase("ou=groups")
            .userDnPatterns(env.getProperty("ldap.base"))
            .userSearchFilter("uid={0}")
            .passwordCompare()
            .passwordAttribute("userPassword")
            .passwordEncoder(SHAPasswordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http
            .httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/", "/search")
            .authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .and()
            .logout()
            .logoutUrl("/logout")
            .deleteCookies()
    }

}