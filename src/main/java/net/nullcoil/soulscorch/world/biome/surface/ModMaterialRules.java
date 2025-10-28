package net.nullcoil.soulscorch.world.biome.surface;

import net.minecraft.block.Block;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.nullcoil.soulscorch.block.ModBlocks;
import net.nullcoil.soulscorch.world.biome.ModBiomes;

public class ModMaterialRules {
    private static final MaterialRules.MaterialRule SOUL_STONE = makeStateRule(ModBlocks.SOUL_STONE);

    public static MaterialRules.MaterialRule makeRules() {
        return MaterialRules.sequence(
                MaterialRules.condition(
                        MaterialRules.biome(ModBiomes.SOULVORE_CAVERNS),
                        MaterialRules.sequence(
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, SOUL_STONE),
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_CEILING, SOUL_STONE),
                                SOUL_STONE
                        )
                )
        );
    }

    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
}