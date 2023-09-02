@file:Suppress("DEPRECATION")

package com.willfp.ecoskills.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.ecoskills.api.resetSkills
import com.willfp.ecoskills.util.offlinePlayers
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil

class CommandReset(plugin: EcoPlugin) :
    Subcommand(
        plugin,
        "reset",
        "ecoskills.command.reset",
        false
    ) {

    override fun onExecute(sender: CommandSender, args: List<String>) {
        val player = notifyPlayerRequired(args.getOrNull(0), "invalid-player")
        player.resetSkills()

        sender.sendMessage(plugin.langYml.getMessage("reset-player"))
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()

        if (args.size == 1) {
            StringUtil.copyPartialMatches(
                args[0],
                Bukkit.getOnlinePlayers().map { player -> player.name }.toCollection(ArrayList()),
                completions
            )
            return completions
        }

        return emptyList()
    }
}
