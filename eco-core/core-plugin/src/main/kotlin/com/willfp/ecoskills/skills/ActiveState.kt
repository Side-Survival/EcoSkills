package com.willfp.ecoskills.skills

import com.willfp.eco.core.data.keys.PersistentDataKey
import com.willfp.eco.core.data.keys.PersistentDataKeyType
import com.willfp.eco.core.data.profile
import com.willfp.eco.util.namespacedKeyOf
import org.bukkit.entity.Player

private val activeKey = PersistentDataKey(
    namespacedKeyOf("ecoskills", "gain_sound_enabled"),
    PersistentDataKeyType.BOOLEAN,
    true
)

fun Player.toggleSkillsActive() {
    this.profile.write(activeKey, !this.profile.read(activeKey))
}

val Player.isSkillsActive: Boolean
    get() = this.profile.read(activeKey)
