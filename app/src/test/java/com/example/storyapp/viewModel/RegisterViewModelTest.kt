package com.example.storyapp.viewModel

import com.example.storyapp.model.ResponseRegister
import com.example.storyapp.repository.DataRepository
import com.example.storyapp.utils.CoroutinesTestRule
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.NetworkRequest
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import junit.framework.TestCase.assertTrue
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
class RegisterViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var dataRepository: DataRepository
    private lateinit var registerViewModel: RegisterViewModel

    private val mockRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val testName = "Fauzan"
    private val testEmail = "fauzan021103@gmail.com"
    private val testPassword = "fauzan021103"

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(dataRepository)
    }

    @Test
    fun `register successful - NetworkRequest Success`() = runTest {
        val expectedResponse = flowOf(NetworkRequest.Success(mockRegisterResponse))

        `when`(dataRepository.register(testName, testEmail, testPassword)).thenReturn(expectedResponse)

        registerViewModel.register(testName, testEmail, testPassword).collect { result ->
            when (result) {
                is NetworkRequest.Success -> {
                    assertNotNull(result.data)
                    assertSame(result.data, mockRegisterResponse)
                }
                is NetworkRequest.Error -> {
                    assertFalse(result.data!!.error)
                }
                is NetworkRequest.Loading -> {}
            }
        }
        verify(dataRepository).register(testName, testEmail, testPassword)
    }

    @Test
    fun `register failed - NetworkRequest Error`() = runTest {
        val expectedResponse: Flow<NetworkRequest<ResponseRegister>> = flowOf(NetworkRequest.Error("failed"))

        `when`(dataRepository.register(testName, testEmail, testPassword)).thenReturn(expectedResponse)

        registerViewModel.register(testName, testEmail, testPassword).collect { result ->
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
        verify(dataRepository).register(testName, testEmail, testPassword)
    }
}