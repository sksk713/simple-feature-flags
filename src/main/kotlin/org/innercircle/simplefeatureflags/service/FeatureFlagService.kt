package org.innercircle.simplefeatureflags.service

interface FeatureFlagService {
    /**
     * 지정된 이름의 피처 플래그가 활성화되었는지 확인합니다.
     *
     * @param flagName 확인할 피처 플래그의 이름.
     * @return 플래그가 활성화되어 있으면 true, 그렇지 않으면 false.
     */
    fun isEnabled(flagName: String): Boolean

    /**
     * 지정된 이름의 피처 플래그가 비활성화되었는지 확인합니다.
     * isEnabled와 거의 동일한 기능이지만 사용하는 곳에서 !를 사용하는 것을 피하기 위해 추가했습니다.
     */
    fun isDisabled(flagName: String): Boolean = !isEnabled(flagName)
}
