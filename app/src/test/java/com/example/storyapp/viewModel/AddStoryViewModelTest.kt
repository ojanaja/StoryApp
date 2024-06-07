package com.example.storyapp.viewModel

import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.model.ResponseUploadStory
import com.example.storyapp.repository.DataRepository
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.NetworkRequest
import junit.framework.Assert.*
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
class AddStoryViewModelTest {

    @Mock
    private lateinit var dataRepository: DataRepository

    private lateinit var addStoryViewModel: AddStoryViewModel

    private val dummyToken = DataDummy.generateDummyToken()
    private val dummyUploadResponse = DataDummy.generateDummyFileUploadResponse()
    private val dummyMultipartFile = DataDummy.generateDummyMultipartFile()
    private val dummyDescriptionRequestBody = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(dataRepository)
    }

    @Test
    fun `uploadStory succeeds`() = runTest {
        val expectedResponse = flowOf(NetworkRequest.Success(dummyUploadResponse))

        `when`(
            dataRepository.uploadStory(
                dummyToken,
                dummyDescriptionRequestBody,
                "",
                "",
                dummyMultipartFile
            )
        ).thenReturn(expectedResponse)

        addStoryViewModel.uploadStory(
            dummyToken,
            dummyDescriptionRequestBody,
            "",
            "",
            dummyMultipartFile
        ).collect { result ->
            when (result) {
                is NetworkRequest.Success -> {
                    assertNotNull(result.data)
                    assertSame(dummyUploadResponse, result.data)
                }
                is NetworkRequest.Error -> assertFalse(result.data!!.error)
                is NetworkRequest.Loading -> {}
            }
        }

        verify(dataRepository).uploadStory(dummyToken, dummyDescriptionRequestBody, "", "", dummyMultipartFile)
    }

    @Test
    fun `uploadStory fails`() = runTest {
        val expectedResponse: Flow<NetworkRequest<ResponseUploadStory>> = flowOf(NetworkRequest.Error("failed"))

        `when`(
            dataRepository.uploadStory(
                dummyToken,
                dummyDescriptionRequestBody,
                "",
                "",
                dummyMultipartFile
            )
        ).thenReturn(expectedResponse)

        addStoryViewModel.uploadStory(
            dummyToken,
            dummyDescriptionRequestBody,
            "",
            "",
            dummyMultipartFile
        ).collect { result ->
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

        verify(dataRepository).uploadStory(dummyToken, dummyDescriptionRequestBody, "", "", dummyMultipartFile)
    }
}
