package com.casainteligente.spring.service

import com.casainteligente.spring.domain.dto.UserDTO
import com.casainteligente.spring.domain.entity.Role
import com.casainteligente.spring.domain.entity.User
import com.casainteligente.spring.repository.RoleRepository
import com.casainteligente.spring.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var roleRepository: RoleRepository
    private lateinit var userService: UserService
    private lateinit var webClient: WebClient


    @BeforeEach
    fun setUp(){
        userRepository = mockk()
        roleRepository = mockk()
        webClient = mockk()
        userService = UserService(userRepository, roleRepository, webClient)
    }

    @Test
    fun `buscarUsuarioGitHub deve salvar novos usuarios e ignorar existentes`(){

        // Usuarios que viriam do github
        val gitHubUser1 = UserDTO(login = "user1",url = "http://user1.com",roles = emptySet())
        val gitHubUser2 = UserDTO(login = "user2",url = "http://user2.com",roles = emptySet())

        every { webClient.get() } returns mockk{
            every { uri("https://api.github.com/users") } returns mockk{
                every { retrieve() } returns mockk{
                    every { bodyToFlux(UserDTO::class.java) } returns Flux.just(gitHubUser1, gitHubUser2)
                }
            }
        }

        // Mock do UserRepository: user1 não existe, user2 já existe
        every { userRepository.findByLogin("user1") } returns null
        every { userRepository.findByLogin("user2") } returns User(id = 2, login = "user2", url = "http://user2.com")

        // Mock do save para user1
        every { userRepository.save(any<User>()) } returnsArgument 0 // Retorna o mesmo objeto que foi salvo

        userService.buscarUsuariosGitHub()

        // Verifica se o user1 foi salvo
        verify(exactly = 1) { userRepository.save(match { it.login == "user1" && it.url == "http://user1.com" }) }
        // Verifica se o user2 não foi salvo (porque já existia)
        verify(exactly = 0) { userRepository.save(match { it.login == "user2" }) }
    }

    @Test
    fun `atribuirPerfil deve atribuir perfil com sucesso`() {
        val user = User(id = 1, login = "testuser", url = "testurl")
        val role = Role(id = 10, nome = "TEST_ROLE")

        every { userRepository.findById(1L) } returns Optional.of(user)
        every { roleRepository.findById(10L) } returns Optional.of(role)
        every { userRepository.save(any<User>()) } returnsArgument 0

        userService.atribuirPerfil(1L, 10L)

        assertTrue(user.roles.contains(role))
        verify(exactly = 1) { userRepository.save(user) }
    }

    @Test
    fun `atribuirPerfil deve lancar RuntimeException se usuario nao encontrado`() {
        every { userRepository.findById(any()) } returns Optional.empty()

        val exception = assertThrows(RuntimeException::class.java) {
            userService.atribuirPerfil(1L, 10L)
        }
        assertEquals("Usuário não encontrado", exception.message)
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `atribuirPerfil deve lancar RuntimeException se perfil nao encontrado`() {
        val user = User(id = 1, login = "testuser", url = "testurl")
        every { userRepository.findById(1L) } returns Optional.of(user)
        every { roleRepository.findById(any()) } returns Optional.empty()

        val exception = assertThrows(RuntimeException::class.java) {
            userService.atribuirPerfil(1L, 10L)
        }
        assertEquals("Perfil não encontrado", exception.message)
        verify(exactly = 0) { userRepository.save(any()) }
    }


}