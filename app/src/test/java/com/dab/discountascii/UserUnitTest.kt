package com.dab.discountascii

import com.dab.discountascii.data.entities.User
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UserUnitTest {

    lateinit var user: User

    @Before
    fun setUp(){
        user = User("DatVu", "datvu@gmail.com")
    }

    @Test
    fun isUserNameTest() {
        assertEquals(user.username, "DatVu")
    }

    @Test
    fun isEmailTest() {
        assertEquals(user.email, "datvu@gmail.com")
    }

}