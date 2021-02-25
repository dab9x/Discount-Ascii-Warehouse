package com.dab.discountascii.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.dab.discountascii.TestCoroutineRule
import com.dab.discountascii.data.entities.Product
import com.dab.discountascii.data.entities.Purchase
import com.dab.discountascii.data.entities.User
import com.dab.discountascii.data.repository.AppRepository
import com.dab.discountascii.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
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
class UsersPurchaseViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var appRepository: AppRepository

    @Mock
    private lateinit var apiProductsObserver: Observer<Resource<List<Product>>>

    @Mock
    private lateinit var username: String

    @Before
    fun setUp() {
        // do something if required
        username = "TestName"
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(emptyList<Product>())
                .`when`(appRepository)
                .getPurchasesUser(username)

            val viewModel = UsersPurchaseViewModel(appRepository)
            viewModel.getProducts().observeForever(apiProductsObserver)

            val purchaseUsersFromApiDeferred =
                async {  verify(appRepository).getPurchasesUser(username) }
            val purchaseUsersFromApi = purchaseUsersFromApiDeferred.await()

            val allPurchasesFromApi = mutableListOf<Purchase>()

            if (purchaseUsersFromApi.status == Resource.Status.SUCCESS) {
                purchaseUsersFromApi.data?.purchases?.let {
                    allPurchasesFromApi.addAll(it)
                }

                val productsDetail =
                    viewModel.fetchProductsDetail(allPurchasesFromApi.asIterable())
                verify(viewModel.fetchProductsWithUser(productsDetail.asIterable()))
            }
            verify(apiProductsObserver).onChanged(Resource.success(emptyList()))
            viewModel.getProducts().removeObserver(apiProductsObserver)
        }
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Fetch User Error"
            doThrow(RuntimeException(errorMessage))
                .`when`(appRepository)
                .getPurchasesUser(username)

            val viewModel = UsersPurchaseViewModel(appRepository)
            viewModel.getProducts().observeForever(apiProductsObserver)

            val purchaseUsersFromApiDeferred =
                async {  verify(appRepository).getPurchasesUser(username) }
            val purchaseUsersFromApi = purchaseUsersFromApiDeferred.await()

            val allPurchasesFromApi = mutableListOf<Purchase>()

            if (purchaseUsersFromApi.status == Resource.Status.SUCCESS) {
                purchaseUsersFromApi.data?.purchases?.let {
                    allPurchasesFromApi.addAll(it)
                }

                val productsDetail =
                    viewModel.fetchProductsDetail(allPurchasesFromApi.asIterable())
                verify(viewModel.fetchProductsWithUser(productsDetail.asIterable()))
            }

            verify(apiProductsObserver).onChanged(
                Resource.error(
                    RuntimeException(errorMessage).toString(),
                    null
                )
            )
            viewModel.getProducts().removeObserver(apiProductsObserver)
        }
    }

    @After
    fun tearDown() {
        // do something if required
    }

}