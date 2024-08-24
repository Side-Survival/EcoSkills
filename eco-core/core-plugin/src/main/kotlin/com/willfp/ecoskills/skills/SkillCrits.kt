package com.willfp.ecoskills.skills

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.util.BlockVector
import java.util.HashSet
import java.util.WeakHashMap

private val map = WeakHashMap<EntityDamageByEntityEvent, Double>()

var EntityDamageByEntityEvent.skillCrit: Double
    get() = map[this] ?: 1.0
    set(value) {
        map[this] = value
    }

val EntityDamageByEntityEvent.isSkillCrit: Boolean
    get() = map.containsKey(this)

object SkillCritListener : Listener {
    val placed = HashSet<BlockVector>()

    @EventHandler(
        priority = EventPriority.HIGH
    )
    fun handle(event: EntityDamageByEntityEvent) {
        if (event.isSkillCrit) {
            event.damage *= event.skillCrit
        }
    }

    @EventHandler(
        ignoreCancelled = true,
        priority = EventPriority.MONITOR
    )
    fun onBlockPlace(event: BlockPlaceEvent) {
        placed.add(BlockVector(event.block.x, event.block.y, event.block.z))
    }

    @EventHandler(
        ignoreCancelled = true,
        priority = EventPriority.MONITOR
    )
    fun onBlockBreak(event: BlockPlaceEvent) {
        placed.remove(BlockVector(event.block.x, event.block.y, event.block.z))
    }
}
