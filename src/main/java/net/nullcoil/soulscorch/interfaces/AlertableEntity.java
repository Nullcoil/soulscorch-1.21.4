package net.nullcoil.soulscorch.interfaces;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public interface AlertableEntity {
    void onGlarynxAlert(PlayerEntity player, Vec3d glarynxPos, int priority);

    default boolean canBeAlerted() {
        return true;
    }
}
