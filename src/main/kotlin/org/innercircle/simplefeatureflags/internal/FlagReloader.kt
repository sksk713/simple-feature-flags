package org.innercircle.simplefeatureflags.internal

import org.innercircle.simplefeatureflags.autoconfigure.SimpleFlagsProperties
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference

internal class FlagReloader(
    private val properties: SimpleFlagsProperties,
    private val flagLoader: FlagLoader,
    private val flagRegistry: FlagRegistry,
    private val clock: Clock,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val lastReloadAttemptTime = AtomicReference(Instant.MIN)

    /**
     * 1. 최소 간격 체크
     * 2. 마지막 시도 시간 업데이트 시도
     * 3. 실제 리로드 로직 수행
     */
    @Scheduled(cron = "\${simple.flags.reload-cron}")
    fun reloadFlagsPeriodically() {
        val now = clock.instant()
        val lastAttempt = lastReloadAttemptTime.get()

        if (shouldSkipReloadDueToInterval(now, lastAttempt)) {
            return
        }

        if (!tryUpdateLastAttemptTime(lastAttempt, now)) {
            return
        }

        log.info("Scheduled reload triggered for feature flags.")
        performReload()
    }

    /**
     * 현재 시간과 마지막 시도 시간을 비교하여 최소 리로드 간격이 지났는지 확인합니다,
     * 지나지 않았으면 true를 반환하여 이번 주기를 스킵합니다.
     */
    private fun shouldSkipReloadDueToInterval(now: Instant, lastAttempt: Instant): Boolean {
        val durationSinceLastAttempt = Duration.between(lastAttempt, now)
        return durationSinceLastAttempt.seconds < properties.reloadIntervalSeconds
    }

    /**
     * Compare-and-Swap을 사용하여 마지막 리로드 시도 시간을 업데이트하려고 시도합니다.
     * 업데이트 성공 시 true, 실패(동시 접근) 시 false를 반환하여 이번 주기를 스킵합니다..
     */
    private fun tryUpdateLastAttemptTime(expectedTime: Instant, newTime: Instant): Boolean {
        return lastReloadAttemptTime.compareAndSet(expectedTime, newTime)
    }

    /**
     * 실제 플래그 로딩 및 레지스트리 업데이트 로직을 수행합니다.
     */
    private fun performReload() {
        try {
            val loadedFlags = flagLoader.loadFlags(properties.location)
            flagRegistry.updateFlags(loadedFlags)
            log.info("Feature flags reloaded successfully.")
        } catch (e: Exception) {
            log.error("Failed to reload feature flags.")
        }
    }
}
