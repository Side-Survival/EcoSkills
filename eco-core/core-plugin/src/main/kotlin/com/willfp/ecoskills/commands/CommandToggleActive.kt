package com.willfp.ecoskills.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.ecoskills.skills.isSkillsActive
import com.willfp.ecoskills.skills.toggleSkillsActive
import org.bukkit.entity.Player

class CommandToggleActive(plugin: EcoPlugin) : Subcommand(
    plugin, "toggle", "ecoskills.command.toggleactive", true
) {

    override fun onExecute(player: Player, args: List<String>) {
        when (player.isSkillsActive) {
            true -> {
                player.sendMessage(plugin.langYml.getMessage("disabled-skill-toggle"))
            }

            false -> player.sendMessage(plugin.langYml.getMessage("enabled-skill-toggle"))
        }

        player.toggleSkillsActive()
    }
}
