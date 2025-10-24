package net.nullcoil.soulscorch.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.entity.custom.jellyfish.JellyfishEntity;

public class HytodomEntity extends JellyfishEntity {
    public HytodomEntity(EntityType<? extends JellyfishEntity> type, World world) {
        super(type, world);
    }
}
