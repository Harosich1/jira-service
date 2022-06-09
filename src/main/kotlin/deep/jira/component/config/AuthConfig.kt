package deep.jira.component.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfig {
    @Value("\${app.authentication.url}")
    private lateinit var authenticationBaseUrl: String

    @Bean
    fun authenticationBaseUrl(): String{
        return authenticationBaseUrl
    }
}