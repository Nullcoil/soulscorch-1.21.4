package net.nullcoil.soulscorch.event;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Call SleepHealthResetHandler.register() from your ModInitializer to enable:
 * - Reset player's max health to 20 when they stop sleeping.
 */
public final class SleepHealthResetHandler {
    private SleepHealthResetHandler() {}

    public static void register() {
        EntitySleepEvents.STOP_SLEEPING.register((LivingEntity entity, BlockPos sleepingPos) -> {
            // only act on players and only on the logical server
            if (!(entity instanceof PlayerEntity player)) return;
            if (player.getWorld().isClient) return;

            EntityAttributeInstance maxHealthAttr = player.getAttributeInstance(EntityAttributes.MAX_HEALTH);
            if (maxHealthAttr == null) return;

            // set base max health to 20 (20 = 10 hearts)
            maxHealthAttr.setBaseValue(20.0d);

            // clamp current health to new max
            double max = maxHealthAttr.getValue();
            if (player.getHealth() > max) {
                player.setHealth((float) max);
            }
        });
    }
}
