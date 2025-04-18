package org.simplefeatureflags.internal

import org.simplefeatureflags.service.FeatureFlagService

/**
 * FeatureFlagService 인터페이스의 기본 내부 구현체.
 * FlagRegistry를 사용하여 플래그 상태를 조회합니다.
 */
internal class DefaultFeatureFlagService(
    private val flagRegistry: FlagRegistry
) : FeatureFlagService {

    /**
     * FlagRegistry를 통해 플래그 활성화 여부를 조회합니다.
     * 플래그가 레지스트리에 정의되지 않은 경우, 디폴트 값인 false를 반환합니다.
     */
    override fun isEnabled(flagName: String): Boolean {
        return flagRegistry.isEnabled(flagName)
    }
}
