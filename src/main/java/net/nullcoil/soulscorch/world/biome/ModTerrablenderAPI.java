package net.nullcoil.soulscorch.world.biome;

import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.world.biome.surface.ModMaterialRules;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class ModTerrablenderAPI implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        // Register with a lower weight to reduce conflicts
        Regions.register(new ModNetherRegion(Identifier.of(Soulscorch.MOD_ID, "nether"), 2));

        SurfaceRuleManager.addSurfaceRules(
                SurfaceRuleManager.RuleCategory.NETHER,
                Soulscorch.MOD_ID,
                ModMaterialRules.makeRules()
        );
    }
}