package org.innercircle.simplefeatureflags.internal

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.core.io.ResourceLoader
import java.io.FileNotFoundException
import java.io.IOException

internal class FlagLoader(
    private val resourceLoader: ResourceLoader,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 지정된 위치에서 feature flags JSON 파일을 로드하고 파싱하여 Map으로 반환합니다.
     * @param location feature_flags.json 리소스 위치
     * @return 파싱된 플래그 맵
     * @throws FileNotFoundException 파일을 찾을 수 없을 때
     * @throws JsonProcessingException JSON 파싱 실패
     * @throws IOException 파일 읽기 실패 시
     */
    fun loadFlags(location: String): Map<String, Boolean> {
        log.debug("Attempting to load feature flags.")
        val resource = resourceLoader.getResource(location)

        if (!resource.exists()) {
            log.warn("Feature flag resource not found.")
            throw FileNotFoundException("Feature flag resource not found.")
        }

        return try {
            resource.inputStream.use { inputStream ->
                val typeRef = object : TypeReference<Map<String, Boolean>>() {}
                val flags: Map<String, Boolean> = objectMapper.readValue(inputStream, typeRef)
                flags
            }
        } catch (e: JsonProcessingException) {
            log.error("Failed to parse feature flag resource at location.", e)
            throw RuntimeException("Failed to parse feature flags from JSON.", e)
        } catch (e: IOException) {
            log.error("Failed to read feature flag resource.", e)
            throw RuntimeException("Failed to read feature flags.", e)
        }
    }
}