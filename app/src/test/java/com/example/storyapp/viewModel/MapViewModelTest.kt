package com.example.storyapp.viewModel

import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.repository.DataRepository
import com.example.storyapp.model.ResponseHome
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
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {

    @Mock
    private lateinit var dataRepository: DataRepository
    private lateinit var mapViewModel: MapViewModel

    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()
    private val dummyToken = DataDummy.generateDummyToken()

    @Before
    fun setup() {
        mapViewModel = MapViewModel(dataRepository)
    }

    @Test
    fun `getStoriesLocation returns success result`() = runTest {
        val expectedResponse = flowOf(NetworkRequest.Success(dummyStoriesResponse))

        `when`(dataRepository.getStoriesLocation(dummyToken)).thenReturn(expectedResponse)

        mapViewModel.getStoriesLocation(dummyToken).collect { result ->
            when (result) {
                is NetworkRequest.Success -> {
                    assertTrue(true)
                    assertNotNull(result.data)
                    assertSame(result.data, dummyStoriesResponse)
                }
                is NetworkRequest.Error -> {
                    assertFalse(result.data!!.error)
                }
                is NetworkRequest.Loading -> {}
            }
        }

        verify(dataRepository).getStoriesLocation(dummyToken)
    }

    @Test
    fun `getStoriesLocation returns error result`() = runTest {
        val expectedResponse: Flow<NetworkRequest<ResponseHome>> = flowOf(NetworkRequest.Error("failed"))

        `when`(dataRepository.getStoriesLocation(dummyToken)).thenReturn(expectedResponse)

        mapViewModel.getStoriesLocation(dummyToken).collect { result ->
            when (result) {
                is NetworkRequest.Success -> {
                    assertTrue(false)
                    assertFalse(result.data!!.error)
                }
                is NetworkRequest.Error -> {
                    assertNotNull(result.message)
                }
                is NetworkRequest.Loading -> {}
            }
        }

        verify(dataRepository).getStoriesLocation(dummyToken)
    }
}