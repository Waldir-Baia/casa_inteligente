import jakarta.servlet.http.HttpServletRequest

import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        // Este método é invocado quando um usuário tenta acessar um recurso protegido
        // sem fornecer credenciais válidas.
        // Enviamos uma resposta 401 Unauthorized, pois não há página de login para redirecionar.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: ${authException.message}")
    }
}