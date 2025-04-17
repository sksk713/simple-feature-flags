package org.innercircle.simplefeatureflags.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * location: feature_flags.json의 위치를 지정합니다.
 * dynamicReloadingEnabled: true로 설정하면, 플래그가 변경될 때마다 자동으로 리로드됩니다.
 * reloadCron: 리로드 주기를 설정합니다. default는 매 분마다 리로드됩니다.
 * reloadIntervalSeconds: 리로드 간격을 초 단위로 설정합니다. 기본값은 20초입니다.
 */
@ConfigurationProperties(prefix = "simple.flags")
data class SimpleFlagsProperties(
    var location: String,
    var dynamicReloadingEnabled: Boolean = true,
    var reloadCron: String = "0 * * * * ?",
    var reloadIntervalSeconds: Long = 20L
)
