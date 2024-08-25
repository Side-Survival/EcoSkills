package com.willfp.ecoskills.libreforge

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.isPlayerPlaced
import com.willfp.ecoskills.skills.SkillCritListener
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import org.bukkit.util.BlockVector

object FilterPlayerPlacedReal : Filter<NoCompileData, Boolean>("player_placed_real") {

    override fun getValue(config: Config, data: TriggerData?, key: String): Boolean {
        return config.getBool(key)
    }

    override fun isMet(data: TriggerData, value: Boolean, compileData: NoCompileData): Boolean {
        val block = data.block ?: return true

        if (SkillCritListener.placed.contains(BlockVector(block.x, block.y, block.z)) == value)
            return true

        return block.isPlayerPlaced == value
    }
}
