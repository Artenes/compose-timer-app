package dev.artenes.template

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SampleTest {

    private val fixture = TestFixtures()

    @Before
    fun setUp() {
        fixture.setUp()
    }

    @Test
    fun test() {
        assertEquals(4, 2 + 2)
    }

    @After
    fun tearDown() {
        fixture.tearDown()
    }

}