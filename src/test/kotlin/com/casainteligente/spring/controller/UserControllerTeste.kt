package com.casainteligente.spring.controller

import com.casainteligente.spring.domain.dto.UserDTO
import com.casainteligente.spring.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@WebMvcTest(UserController::class)
class UserControllerTeste(@Autowired private val mockMvc: MockMvc) {

    @TestConfiguration
    class UserControlllerTesteConfig{
        @Bean
        fun userService() = mockk< UserService>()
    }

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun `POST - sincronizarUsuarios deve retornar status 200 OK`() {
        every { userService.buscarUsuariosGitHub() } returns Unit

        mockMvc.perform(post("/users/syncGit"))
            .andExpect(status().isOk)
            .andExpect(content().string("Sincronização concluída!"))

        verify(exactly = 1) { userService.buscarUsuariosGitHub() }
    }

    @Test
    fun `GET - listarUsuarios deve retornar lista de usuarios e status 200 OK`() {
        val user1 = UserDTO(login = "user1", url = "url1", roles = setOf("ADMIN"))
        val user2 = UserDTO(login = "user2", url = "url2", roles = setOf("USER"))
        val userList = listOf(user1, user2)

        every { userService.listarUsuarios() } returns userList

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].login").value("user1"))
            .andExpect(jsonPath("$[1].roles[0]").value("USER"))

        verify(exactly = 1) { userService.listarUsuarios() }
    }

    @Test
    fun `POST - atribuirPerfil deve retornar status 200 OK`() {
        every { userService.atribuirPerfil(1L, 10L) } returns Unit // O método não retorna nada

        mockMvc.perform(post("/users/{userId}/roles/{roleId}", 1, 10))
            .andExpect(status().isOk)
            .andExpect(content().string("Perfil atribuído ao usuário!"))

        verify(exactly = 1) { userService.atribuirPerfil(1L, 10L) }
    }

    @Test
    fun `POST - atribuirPerfil deve retornar status 404 Not Found se usuario nao encontrado`() {
        every { userService.atribuirPerfil(1L, 10L) } throws RuntimeException("Usuário não encontrado")

        mockMvc.perform(post("/users/{userId}/roles/{roleId}", 1, 10))
            .andExpect(status().isInternalServerError) // RuntimeException padrão retorna 500
            .andExpect(content().string("Usuário não encontrado"))

        verify(exactly = 1) { userService.atribuirPerfil(1L, 10L) }
    }
}