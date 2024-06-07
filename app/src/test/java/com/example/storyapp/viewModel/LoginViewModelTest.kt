package com.example.storyapp.viewModel

import com.example.storyapp.model.ResponseLogin
import com.example.storyapp.repository.DataRepository
import com.example.storyapp.utils.CoroutinesTestRule
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.NetworkRequest
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var dataRepository: DataRepository
    private lateinit var loginViewModel: LoginViewModel

    private val mockLoginResponse = DataDummy.generateDummyLoginResponse()
    private val testEmail = "fauzan021103@gmail.com"
    private val testPassword = "fauzan021103"

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(dataRepository)
    }

    @Test
    fun `login successful - NetworkRequest Success`() = runTest {
        val expectedResponse = flowOf(NetworkRequest.Success(mockLoginResponse))

        `when`(dataRepository.login(testEmail, testPassword)).thenReturn(expectedResponse)

        loginViewModel.login(testEmail, testPassword).collect { result ->
            when (result) {
                is NetworkRequest.Success -> {
                    assertNotNull(result.data)
                    assertSame(result.data, mockLoginResponse)
                }
                is NetworkRequest.Error -> {
                    assertFalse(result.data!!.error)
                }
                is NetworkRequest.Loading -> {}
            }
        }
        verify(dataRepository).login(testEmail, testPassword)
    }

    @Test
    fun `login failed - NetworkRequest Error`() = runTest {
        val expectedResponse: Flow<NetworkRequest<ResponseLogin>> = flowOf(NetworkRequest.Error("failed"))

        `when`(dataRepository.login(testEmail, testPassword)).thenReturn(expectedResponse)

        loginViewModel.login(testEmail, testPassword).collect { result ->
            when (result) {
                is NetworkRequest.Success -> {
                    assertFalse(true)
                }
                is NetworkRequest.Error -> {
                    assertNotNull(result.message)
                }
                is NetworkRequest.Loading -> {}
            }
        }
        verify(dataRepository).login(testEmail, testPassword)
    }
}