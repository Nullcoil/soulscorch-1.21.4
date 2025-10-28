package net.nullcoil.soulscorch.world.biome;

import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public class ModTerrablenderAPI implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new ModNetherRegion(Identifier.of(Soulscorch.MOD_ID, "nether"),4));
    }
}
