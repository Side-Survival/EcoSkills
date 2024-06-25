package com.willfp.ecoskills.gui.menus

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.FillerMask
import com.willfp.eco.core.gui.slot.MaskItems
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.evaluateExpression
import com.willfp.eco.util.formatEco
import com.willfp.eco.util.toNiceString
import com.willfp.ecoskills.api.getSkillLevel
import com.willfp.ecoskills.skills.Skill
import com.willfp.libreforge.filters.impl.FilterBlocks
import com.willfp.libreforge.filters.impl.FilterEntities
import com.willfp.libreforge.triggers.impl.TriggerKill
import com.willfp.libreforge.triggers.impl.TriggerMineBlock
import org.bukkit.Material
import org.bukkit.entity.Player
import kotlin.math.ceil

class WorthGUI(
    plugin: EcoPlugin,
    private val skill: Skill
) {
    private val menu: Menu

    init {
        val slotList = mutableListOf<Slot>()
        val baseLore = plugin.configYml.getStrings("worth-gui.lore")

        for (xpGainMethod in skill.xpGainMethods) {
            // mine block
            if (xpGainMethod.trigger is TriggerMineBlock) {
                for (filterList in xpGainMethod.filters) {
                    val filter = filterList.filter
                    if (filter !is FilterBlocks)
                        continue

                    val blocks = filter.getValue(filterList.config, null, "blocks")
                    for (block in blocks) {
                        slotList.add(getItem(
                            block,
                            baseLore,
                            xpGainMethod.multiplierExpression,
                            xpGainMethod.extraExpression
                        ))
                    }
                }
            }
            // kill entity
            else if (xpGainMethod.trigger is TriggerKill) {
                for (filterList in xpGainMethod.filters) {
                    val filter = filterList.filter
                    if (filter !is FilterEntities)
                        continue

                    val entities = filter.getValue(filterList.config, null, "entities")
                    for (entity in entities) {
                        slotList.add(getItem(
                            getSpawnEgg(entity),
                            baseLore,
                            xpGainMethod.multiplierExpression,
                            xpGainMethod.extraExpression
                        ))
                    }
                }
            }
        }

        // calculate row size
        val rows = (2 + ceil(slotList.size / 9.0)).toInt().coerceAtMost(6)

        menu = menu(rows) {
            title = plugin.configYml.getString("worth-gui.title")
                .replace("%skill%", skill.name)
                .formatEco()

            val pattern = plugin.configYml.getStrings("worth-gui.mask.pattern")
            val maskRows = pattern.subList(0, rows).toTypedArray()
            maskRows[maskRows.size - 1] = pattern[5]

            setMask(
                FillerMask(
                    MaskItems.fromItemNames(plugin.configYml.getStrings("worth-gui.mask.materials")),
                    *maskRows
                )
            )

            var r = 2
            var c = 1

            for (itemSlot in slotList) {
                addComponent(
                    r, c,
                    itemSlot
                )
                c++
                if (c == 10) {
                    r++
                    c = 1
                }
            }
        }
    }

    private fun getSpawnEgg(entity: String): String {
        val test = Items.lookup(entity + "_SPAWN_EGG")
        if (test.item.type == Material.AIR)
            return "EGG"

        return test.item.type.name
    }

    private fun getItem(material: String, baseLore: List<String>, multiplier: String, extra: String): Slot {
        val finalMaterial = when {
            material.equals("POTATOES", false) -> "POTATO"
            material.equals("CARROTS", false) -> "CARROT"
            material.equals("BEETROOTS", false) -> "BEETROOT"
            material.equals("COCOA", false) -> "COCOA_BEANS"
            else -> material
        }

        return slot { player, _ ->
            val moneyFormula = skill.moneyFormula
                .replace("%level%", player.getSkillLevel(skill).toString())
                .replace("%extra%", extra)
            val money = evaluateExpression(moneyFormula).toNiceString()

            val item = Items.lookup(finalMaterial).item
            val lore = baseLore.map { s ->
                s.replace("%xp%", multiplier)
                    .replace("%money%", money)
            }.toMutableList()

            ItemStackBuilder(item)
                .addLoreLines(StringUtils.formatList(
                    lore,
                    player,
                    StringUtils.FormatOption.WITH_PLACEHOLDERS
                ))
                .build()
        }
    }

    fun open(player: Player) {
        menu.open(player)
    }
}
