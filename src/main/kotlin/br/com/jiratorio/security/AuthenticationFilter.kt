package br.com.jiratorio.security

import br.com.jiratorio.extension.isPresent
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    private val authenticationManager: AuthenticationManager
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        response as HttpServletResponse

        val authToken: String? = request.getHeader("X-Auth-Token")
        if (authToken.isPresent()) {
            val authentication = PreAuthenticatedAuthenticationToken(authToken, null)
            val authenticateResult: Authentication = try {
                authenticationManager.authenticate(authentication)
            } catch (e: PreAuthenticatedCredentialsNotFoundException) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
                return
            }

            if (!authenticateResult.isAuthenticated) {
                throw RuntimeException("Internal Authentication Service Error")
            }

            SecurityContextHolder.getContext().authentication = authenticateResult
        }

        chain.doFilter(request, response)
    }

}
