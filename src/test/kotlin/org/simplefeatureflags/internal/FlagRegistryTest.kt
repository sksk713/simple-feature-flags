package org.simplefeatureflags.internal

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.simplefeatureflags.internal.FlagRegistry

internal class FlagRegistryTest {

    private lateinit var flagRegistry: FlagRegistry

    @BeforeEach
    fun setUp() {
        flagRegistry = FlagRegistry()
    }

    @Test
    fun `isEnabled는 정의된 플래그의 상태를 반환해야 한다`() {
        // Arrange
        val initialFlags = mapOf("featureA" to true, "featureB" to false)

        // Act
        flagRegistry.updateFlags(initialFlags)

        // Assert
        assertTrue(flagRegistry.isEnabled("featureA"))
        assertFalse(flagRegistry.isEnabled("featureB"))
    }

    @Test
    fun `isEnabled는 정의되지 않은 플래그에 대해 false를 반환해야 한다`() {
        // Arrange
        val initialFlags = mapOf("featureA" to true)

        // Act
        flagRegistry.updateFlags(initialFlags)

        // Assert
        assertFalse(flagRegistry.isEnabled("nonExistentFeature"))
    }

    @Test
    fun `updateFlags는 기존 플래그를 지우고 새 플래그로 업데이트해야 한다`() {
        // Arrange
        val initialFlags = mapOf("featureA" to true, "featureB" to false)
        flagRegistry.updateFlags(initialFlags)
        assertTrue(flagRegistry.isEnabled("featureA"))
        assertFalse(flagRegistry.isEnabled("featureB"))

        // Act
        val newFlags = mapOf("featureC" to true, "featureA" to false)
        flagRegistry.updateFlags(newFlags)

        // Assert
        assertFalse(flagRegistry.isEnabled("featureA"))
        assertFalse(flagRegistry.isEnabled("featureB"))
        assertTrue(flagRegistry.isEnabled("featureC"))
    }

    @Test
    fun `updateFlags에 빈 맵이 전달되면 모든 플래그가 비활성화된다`() {
        // Arrange
        val initialFlags = mapOf("featureA" to true)
        flagRegistry.updateFlags(initialFlags)
        assertTrue(flagRegistry.isEnabled("featureA"))

        // Act
        flagRegistry.updateFlags(emptyMap())

        // Assert
        assertFalse(flagRegistry.isEnabled("featureA"))
        assertFalse(flagRegistry.isEnabled("anyOtherFeature"))
    }
}