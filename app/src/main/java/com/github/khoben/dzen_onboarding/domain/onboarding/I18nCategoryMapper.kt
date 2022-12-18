package com.github.khoben.dzen_onboarding.domain.onboarding

import com.github.khoben.dzen_onboarding.common.PlatformString
import com.github.khoben.dzen_onboarding.data.onboarding.I18nCategory
import com.github.khoben.dzen_onboarding.domain.base.Mapper

interface I18nCategoryMapper : Mapper<String, PlatformString> {
    class Mock(
        private val i18nMap: I18nCategory
    ) : I18nCategoryMapper {
        override fun map(data: String): PlatformString {
            val resource = i18nMap[data]
            if (resource != null) {
                return PlatformString.Resource(resource)
            }
            return PlatformString.Plain(data)
        }
    }
}