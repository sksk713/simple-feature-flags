package org.simplefeatureflags.internal

import java.util.concurrent.ConcurrentHashMap

internal class FlagRegistry {

    private val flags: MutableMap<String, Boolean> = ConcurrentHashMap()

    /**
     * 주어진 플래그 이름에 대한 활성화 상태를 반환합니다.
     * 플래그가 정의되지 않은 경우 false를 디폴트값으로 반환합니다.
     * @param flagName 확인할 피처 플래그 이름
     * @return 활성화 여부
     */
    fun isEnabled(flagName: String): Boolean {
        return flags.getOrDefault(flagName, false)
    }

    /**
     * 기존 정보는 초기화됩니다.
     * 로드된 플래그 정보를 업데이트합니다.
     * @param loadedFlags 새로 로드된 플래그 맵
     */
    fun updateFlags(loadedFlags: Map<String, Boolean>) {
        flags.clear()
        flags.putAll(loadedFlags)
    }
}
