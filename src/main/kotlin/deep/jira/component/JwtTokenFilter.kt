package deep.jira.component

import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtTokenFilter(
    val restTemplate: RestTemplate,
    private val authenticationBaseUrl: String
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (StringUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + header.split(" ".toRegex()).toTypedArray()[1].trim { it <= ' ' });
        val requestEntity: HttpEntity<Void> = HttpEntity(headers)

        val authDetails = restTemplate.exchange("$authenticationBaseUrl/token/authenticate", HttpMethod.GET, requestEntity, String::class.java)

        if(authDetails.body!!.isEmpty()) {
            filterChain.doFilter(request, response)
            return
        }

        val authentication = UsernamePasswordAuthenticationToken(authDetails, null, null)

        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }
}