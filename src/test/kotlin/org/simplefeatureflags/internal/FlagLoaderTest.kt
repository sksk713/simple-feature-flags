package org.simplefeatureflags.internal

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.simplefeatureflags.internal.FlagLoader
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import java.io.ByteArrayInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

internal class FlagLoaderTest {

    private val mockResourceLoader: ResourceLoader = mock()
    private val mockObjectMapper: ObjectMapper = mock()
    private val mockResource: Resource = mock()
    private val flagLoader = FlagLoader(mockResourceLoader, mockObjectMapper)
    private val testLocation = "classpath:test-flags.json"

    @Test
    fun `loadFlags는 성공적으로 플래그를 로드하고 Map을 반환해야 한다`() {
        // Arrange
        val jsonContent = """{"featureA": true, "featureB": false}"""
        val expectedMap = mapOf("featureA" to true, "featureB" to false)
        val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
        val typeRefCaptor = argumentCaptor<TypeReference<Map<String, Boolean>>>()

        whenever(mockResourceLoader.getResource(testLocation)).thenReturn(mockResource)
        whenever(mockResource.exists()).thenReturn(true)
        whenever(mockResource.inputStream).thenReturn(inputStream)
        whenever(mockObjectMapper.readValue(any<InputStream>(), typeRefCaptor.capture()))
            .thenReturn(expectedMap)

        // Act
        val loadedFlags = flagLoader.loadFlags(testLocation)

        // Assert
        assertEquals(expectedMap, loadedFlags)
        verify(mockResourceLoader).getResource(testLocation)
        verify(mockResource).exists()
        verify(mockResource).inputStream
        verify(mockObjectMapper).readValue(any<InputStream>(), any<TypeReference<Map<String, Boolean>>>())
        assertNotNull(typeRefCaptor.firstValue)
    }

    @Test
    fun `loadFlags는 리소스를 찾을 수 없을 때 FileNotFoundException을 던져야 한다`() {
        // Arrange
        whenever(mockResourceLoader.getResource(testLocation)).thenReturn(mockResource)
        whenever(mockResource.exists()).thenReturn(false)

        // Act
        val exception = assertThrows<FileNotFoundException> {
            flagLoader.loadFlags(testLocation)
        }

        // Assert
        assertTrue(exception.message?.contains("Feature flag resource not found") ?: false)

        verify(mockResourceLoader).getResource(testLocation)
        verify(mockResource).exists()
        verify(mockResource, never()).inputStream
        verify(mockObjectMapper, never()).readValue(any<InputStream>(), any<TypeReference<Map<String, Boolean>>>())
    }

    @Test
    fun `loadFlags는 리소스 읽기 중 IOException 발생 시 RuntimeException을 던져야 한다`() {
        // Arrange
        val ioException = IOException("Disk read error")
        whenever(mockResourceLoader.getResource(testLocation)).thenReturn(mockResource)
        whenever(mockResource.exists()).thenReturn(true)
        whenever(mockResource.inputStream).thenThrow(ioException)

        // Act
        val exception = assertThrows<RuntimeException> {
            flagLoader.loadFlags(testLocation)
        }

        // Assert
        assertEquals("Failed to read feature flags.", exception.message)

        verify(mockResourceLoader).getResource(testLocation)
        verify(mockResource).exists()
        verify(mockResource).inputStream
        verify(mockObjectMapper, never()).readValue(any<InputStream>(), any<TypeReference<Map<String, Boolean>>>())
    }
}