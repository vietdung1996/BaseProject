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
 * Unit tests for GetUserByIdUseCase
 * Tests business logic validation and error handling
 */
class GetUserByIdUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var getUserByIdUseCase: GetUserByIdUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getUserByIdUseCase = GetUserByIdUseCase(userRepository)
    }

    @Test
    fun `invoke should return user when valid id is provided and user exists`() = runTest {
        // Given
        val userId = 1
        val user = createValidUser(id = userId, name = "John Doe", email = "john@example.com")
        `when`(userRepository.getUserById(userId)).thenReturn(Result.success(user))

        // When
        val result = getUserByIdUseCase(userId)

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        val resultData = result.getOrNull()
        assertNotNull("Result data should not be null", resultData)
        assertEquals("Should return the correct user", user.id, resultData!!.id)
        assertEquals("Should return the correct user name", user.name, resultData.name)
    }

    @Test
    fun `invoke should return validation error when user id is zero`() = runTest {
        // Given
        val invalidUserId = 0

        // When
        val result = getUserByIdUseCase(invalidUserId)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertNotNull("Error should not be null", error)
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention invalid ID", 
            error.message.contains("Invalid user ID"))
        
        // Verify repository was not called
        verify(userRepository, never()).getUserById(any())
    }

    @Test
    fun `invoke should return validation error when user id is negative`() = runTest {
        // Given
        val invalidUserId = -5

        // When
        val result = getUserByIdUseCase(invalidUserId)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertNotNull("Error should not be null", error)
        assertTrue("Error should be ValidationError", error is DomainError.ValidationError)
        assertTrue("Error message should mention invalid ID", 
            error.message.contains("Invalid user ID"))
        
        // Verify repository was not called
        verify(userRepository, never()).getUserById(any())
    }

    @Test
    fun `invoke should return repository error when repository fails`() = runTest {
        // Given
        val userId = 1
        val networkError = DomainError.NetworkError("Network connection failed")
        `when`(userRepository.getUserById(userId)).thenReturn(Result.error(networkError))

        // When
        val result = getUserByIdUseCase(userId)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertNotNull("Error should not be null", error)
        assertTrue("Error should be NetworkError", error is DomainError.NetworkError)
        assertEquals("Error message should match", "Network connection failed", error.message)
    }

    @Test
    fun `invoke should return not found error when user does not exist`() = runTest {
        // Given
        val userId = 999
        val notFoundError = DomainError.NotFoundError("User not found")
        `when`(userRepository.getUserById(userId)).thenReturn(Result.error(notFoundError))

        // When
        val result = getUserByIdUseCase(userId)

        // Then
        assertTrue("Result should be error", result.isError)
        val error = result.errorOrNull()
        assertNotNull("Error should not be null", error)
        assertTrue("Error should be NotFoundError", error is DomainError.NotFoundError)
    }

    @Test
    fun `invoke should validate user data after successful fetch`() = runTest {
        // Given
        val userId = 1
        val validUser = createValidUser(id = userId, name = "John Doe", email = "john@example.com")
        `when`(userRepository.getUserById(userId)).thenReturn(Result.success(validUser))

        // When
        val result = getUserByIdUseCase(userId)

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        verify(userRepository).getUserById(userId)
    }

    @Test
    fun `invoke should handle user with blank name after fetch`() = runTest {
        // Given
        val userId = 1
        val userWithBlankName = createValidUser(id = userId, name = "", email = "john@example.com")
        `when`(userRepository.getUserById(userId)).thenReturn(Result.success(userWithBlankName))

        // When & Then - This should throw an exception during validation
        try {
            getUserByIdUseCase(userId)
            fail("Expected IllegalArgumentException for blank name")
        } catch (e: Exception) {
            // Expected behavior - validation should catch this
            assertTrue("Should catch validation error", e.message?.contains("name") == true)
        }
    }

    @Test
    fun `invoke should handle user with blank email after fetch`() = runTest {
        // Given
        val userId = 1
        val userWithBlankEmail = createValidUser(id = userId, name = "John Doe", email = "")
        `when`(userRepository.getUserById(userId)).thenReturn(Result.success(userWithBlankEmail))

        // When & Then - This should throw an exception during validation
        try {
            getUserByIdUseCase(userId)
            fail("Expected IllegalArgumentException for blank email")
        } catch (e: Exception) {
            // Expected behavior - validation should catch this
            assertTrue("Should catch validation error", e.message?.contains("email") == true)
        }
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
}