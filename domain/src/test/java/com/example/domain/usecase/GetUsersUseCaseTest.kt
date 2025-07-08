package com.example.domain.usecase

import com.example.domain.model.Address
import com.example.domain.model.Company
import com.example.domain.model.Geo
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.domain.util.DomainError
import com.example.domain.util.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Unit tests for GetUsersUseCase
 * Tests business logic and error handling
 */
class GetUsersUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var getUsersUseCase: GetUsersUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getUsersUseCase = GetUsersUseCase(userRepository)
    }

    @Test
    fun `invoke should return filtered and sorted users when repository returns valid users`() = runTest {
        // Given
        val validUser1 = createValidUser(id = 1, name = "John Doe", email = "john@example.com")
        val validUser2 = createValidUser(id = 2, name = "Alice Smith", email = "alice@example.com")
        val invalidUser = createInvalidUser() // Invalid email
        val users = listOf(validUser1, invalidUser, validUser2)
        
        `when`(userRepository.getUsers()).thenReturn(Result.success(users))

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        val resultData = result.getOrNull()
        assertNotNull("Result data should not be null", resultData)
        assertEquals("Should contain only valid users", 2, resultData!!.size)
        
        // Should be sorted by name
        assertEquals("First user should be Alice (alphabetically first)", "Alice Smith", resultData[0].name)
        assertEquals("Second user should be John", "John Doe", resultData[1].name)
    }

    @Test
    fun `invoke should filter out users with blank names`() = runTest {
        // Given
        val validUser = createValidUser(id = 1, name = "John Doe", email = "john@example.com")
        val userWithBlankName = createValidUser(id = 2, name = "", email = "test@example.com")
        val users = listOf(validUser, userWithBlankName)
        
        `when`(userRepository.getUsers()).thenReturn(Result.success(users))

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        val resultData = result.getOrNull()
        assertEquals("Should contain only user with valid name", 1, resultData!!.size)
        assertEquals("Should contain the valid user", "John Doe", resultData[0].name)
    }

    @Test
    fun `invoke should filter out users with invalid emails`() = runTest {
        // Given
        val validUser = createValidUser(id = 1, name = "John Doe", email = "john@example.com")
        val userWithInvalidEmail = createValidUser(id = 2, name = "Jane Doe", email = "invalid-email")
        val users = listOf(validUser, userWithInvalidEmail)
        
        `when`(userRepository.getUsers()).thenReturn(Result.success(users))

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        val resultData = result.getOrNull()
        assertEquals("Should contain only user with valid email", 1, resultData!!.size)
        assertEquals("Should contain the user with valid email", "john@example.com", resultData[0].email)
    }

    @Test
    fun `invoke should return empty list when all users are invalid`() = runTest {
        // Given
        val invalidUser1 = createInvalidUser()
        val invalidUser2 = createValidUser(id = 2, name = "", email = "test@example.com")
        val users = listOf(invalidUser1, invalidUser2)
        
        `when`(userRepository.getUsers()).thenReturn(Result.success(users))

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        val resultData = result.getOrNull()
        assertTrue("Result should be empty list", resultData!!.isEmpty())
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val domainError = DomainError.NetworkError("Network error")
        `when`(userRepository.getUsers()).thenReturn(Result.error(domainError))

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertNotNull("Error should not be null", error)
        assertTrue("Error should be NetworkError", error is DomainError.NetworkError)
    }

    @Test
    fun `invoke should handle empty user list from repository`() = runTest {
        // Given
        `when`(userRepository.getUsers()).thenReturn(Result.success(emptyList()))

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        val resultData = result.getOrNull()
        assertNotNull("Result data should not be null", resultData)
        assertTrue("Result should be empty list", resultData!!.isEmpty())
    }

    private fun createValidUser(id: Int, name: String, email: String): User {
        return User(
            id = id,
            name = name,
            username = "username$id",
            email = email,
            phone = "123-456-7890",
            website = "www.example.com",
            address = Address(
                street = "123 Main St",
                suite = "Apt 1",
                city = "Anytown",
                zipcode = "12345",
                geo = Geo(lat = "0.0", lng = "0.0")
            ),
            company = Company(
                name = "Example Corp",
                catchPhrase = "Doing great things",
                bs = "Business solutions"
            )
        )
    }

    private fun createInvalidUser(): User {
        return createValidUser(id = 999, name = "Invalid User", email = "invalid-email")
    }
}