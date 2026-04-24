package com.mauricio.apimonitoring.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiKeyFilter(
    @Value("\${api.security.key}") private val apiKey: String
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val path = request.requestURI

        // libera health
        if (path == "/health") {
            filterChain.doFilter(request, response)
            return
        }

        val apiKeyHeader = request.getHeader("x-api-key")

        if (apiKeyHeader == null || apiKeyHeader != apiKey) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        // ESSENCIAL: autenticar no contexto do Spring
        val authentication = UsernamePasswordAuthenticationToken(
            "api-key-user",
            null,
            emptyList()
        )

        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }
}