package org.innercircle.simplefeatureflags.autoconfigure

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.innercircle.simplefeatureflags.internal.DefaultFeatureFlagService
import org.innercircle.simplefeatureflags.internal.FlagLoader
import org.innercircle.simplefeatureflags.internal.FlagRegistry
import org.innercircle.simplefeatureflags.internal.FlagReloader
import org.innercircle.simplefeatureflags.service.FeatureFlagService
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import java.time.Clock

@Configuration
@EnableConfigurationProperties(SimpleFlagsProperties::class)
class SimpleFlagsAutoConfiguration(
    private val properties: SimpleFlagsProperties,
    private val resourceLoader: ResourceLoader,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DefaultFeatureFlagService::class)
    internal fun simpleFlagsSystemClock(): Clock {
        return Clock.systemUTC()
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DefaultFeatureFlagService::class)
    internal fun flagRegistry(): FlagRegistry {
        return FlagRegistry()
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DefaultFeatureFlagService::class)
    internal fun flagLoader(): FlagLoader {
        return FlagLoader(resourceLoader, objectMapper)
    }

    @Bean
    @ConditionalOnProperty(prefix = "simple.flags", name = ["dynamic-reloading-enabled"], havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    internal fun flagReloader(flagLoader: FlagLoader, flagRegistry: FlagRegistry, clock: Clock): FlagReloader {
        return FlagReloader(properties, flagLoader, flagRegistry, clock)
    }

    @Bean
    @ConditionalOnMissingBean(FeatureFlagService::class)
    internal fun simpleFlagsFeatureFlagService(flagRegistry: FlagRegistry): FeatureFlagService {
        return DefaultFeatureFlagService(
            flagRegistry
        )
    }

    @PostConstruct
    internal fun initializeFlags(flagLoader: FlagLoader, flagRegistry: FlagRegistry) {
        log.info("Initializing Simple Flags from location: {}", properties.location)
        try {
            val loadedFlags = flagLoader.loadFlags(properties.location)
            flagRegistry.updateFlags(loadedFlags)
        } catch (e: Exception) {
            log.error("Failed to initialize feature flags.")
        }
    }
}