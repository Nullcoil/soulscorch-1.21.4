package net.nullcoil.soulscorch.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Hoglin;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.world.World;

public class RestlessEntity extends ZoglinEntity implements Monster, Hoglin {
    private static final TrackedData<Boolean> AWAKENED;
    public RestlessEntity(EntityType<? extends ZoglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(AWAKENED, false);
    }

    public boolean isStagnating() {
        return this.dataTracker.get(AWAKENED);
    }

    static {
        AWAKENED = DataTracker.registerData(RestlessEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
