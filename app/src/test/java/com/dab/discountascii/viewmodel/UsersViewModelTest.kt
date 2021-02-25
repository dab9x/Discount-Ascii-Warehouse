package com.dab.discountascii.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dab.discountascii.TestCoroutineRule
import com.dab.discountascii.data.entities.User
import com.dab.discountascii.data.repository.AppRepository
import com.dab.discountascii.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UsersViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var appRepository: AppRepository

    @Mock
    private lateinit var apiUsersObserver: Observer<Resource<List<User>>>

    @Before
    fun setUp() {
        // do something if required
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(emptyList<User>())
                .`when`(appRepository)
                .getUsers()
            val viewModel = UsersViewModel(appRepository)
            viewModel.getUsers().observeForever(apiUsersObserver)
            verify(appRepository).getUsers()
            verify(apiUsersObserver).onChanged(Resource.success(emptyList()))
            viewModel.getUsers().removeObserver(apiUsersObserver)
        }
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Fetch User Error"
            doThrow(RuntimeException(errorMessage))
                .`when`(appRepository)
                .getUsers()
            val viewModel = UsersViewModel(appRepository)
            viewModel.getUsers().observeForever(apiUsersObserver)
            verify(appRepository).getUsers()
            verify(apiUsersObserver).onChanged(
                Resource.error(
                    RuntimeException(errorMessage).toString(),
                    null
                )
            )
            viewModel.getUsers().removeObserver(apiUsersObserver)
        }
    }

    @After
    fun tearDown() {
        // do something if required
    }

}