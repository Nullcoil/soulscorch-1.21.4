package net.nullcoil.soulscorch.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public class ModSounds {
    public static final SoundEvent BLAZT_AMBIENT = registerSoundEvent("blazt_ambient");
    public static final SoundEvent BLAZT_DEATH = registerSoundEvent("blazt_death");
    public static final SoundEvent BLAZT_HURT = registerSoundEvent("blazt_hurt");
    public static final SoundEvent BLAZT_SHOOTING = registerSoundEvent("blazt_shooting");
    public static final SoundEvent BLAZT_SOUL_CHARGE = registerSoundEvent("blazt_soul_charge");
    public static final SoundEvent BLAZT_BREATHE_IN = registerSoundEvent("blazt_breathe_in");
    public static final SoundEvent BLAZT_BREATHE_OUT = registerSoundEvent("blazt_breathe_out");


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(Soulscorch.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Sounds for " + Soulscorch.MOD_ID);
    }
}
