package com.xfinity.util

import android.content.Context
import android.text.TextUtils
import com.xfinity.R
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner


@RunWith(PowerMockRunner::class)
@PrepareForTest(TextUtils::class)
class ValidationTest {

    private val searchFieldEmptyMsg = "Search Field Is Empty"

    private lateinit var validation: Validation
    private lateinit var context: Context

    @Before
    fun setup() {
        context = PowerMockito.mock(Context::class.java)
        PowerMockito.mockStatic(TextUtils::class.java)
        PowerMockito.`when`(TextUtils.isEmpty(ArgumentMatchers.any(CharSequence::class.java))).thenAnswer { invocation ->
            !((invocation.arguments[0] as CharSequence).isNotEmpty())
        }
        validation = Validation(context)
        PowerMockito.`when`(context.getString(R.string.search_field_empty)).thenReturn(searchFieldEmptyMsg)
    }

    @Test
    fun testEmptyString() {
        val emptyString = ""
        assertEquals(validation.validSearchQueryOrErrorMsg(emptyString), searchFieldEmptyMsg)
    }

    @Test
    fun testNonEmptyString() {
        val emptyString = "a search string"
        assertEquals(validation.validSearchQueryOrErrorMsg(emptyString), "")
    }

    @Test
    fun testWhiteSpaceString() {
        val emptyString = "     "
        assertEquals(validation.validSearchQueryOrErrorMsg(emptyString), searchFieldEmptyMsg)
    }
}