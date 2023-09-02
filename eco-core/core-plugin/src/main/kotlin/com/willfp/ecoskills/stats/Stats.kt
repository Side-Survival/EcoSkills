package com.willfp.ecoskills.stats

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.placeholder.PlayerPlaceholder
import com.willfp.eco.util.toNiceString
import com.willfp.ecoskills.CategoryWithRegistry
import com.willfp.ecoskills.EcoSkillsPlugin
import com.willfp.ecoskills.api.averageSkillLevel
import com.willfp.ecoskills.api.totalSkillLevel
import com.willfp.libreforge.loader.LibreforgePlugin

object Stats : CategoryWithRegistry<Stat>("stat", "stats") {
    override val supportsSharing = false

    override fun beforeReload(plugin: LibreforgePlugin) {
        PlayerPlaceholder(plugin, "total_skill_level") {
            it.totalSkillLevel.toNiceString()
        }.register()

        PlayerPlaceholder(plugin, "average_skill_level") {
            it.averageSkillLevel.toNiceString()
        }.register()
    }

    override fun clear(plugin: LibreforgePlugin) {
        registry.clear()
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        registry.register(Stat(id, config, plugin as EcoSkillsPlugin))
    }
}
