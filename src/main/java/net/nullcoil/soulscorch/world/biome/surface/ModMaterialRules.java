package net.nullcoil.soulscorch.world.biome.surface;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.nullcoil.soulscorch.block.ModBlocks;
import net.nullcoil.soulscorch.world.biome.ModBiomes;

public class ModMaterialRules {
    private static final MaterialRules.MaterialRule SOUL_STONE = makeStateRule(ModBlocks.SOUL_STONE);
    private static final MaterialRules.MaterialRule SOUL_SAND = makeStateRule(Blocks.SOUL_SAND);
    private static final MaterialRules.MaterialRule SOUL_SOIL = makeStateRule(Blocks.SOUL_SOIL);

    public static MaterialRules.MaterialRule makeRules() {
        return MaterialRules.sequence(
                MaterialRules.condition(
                        MaterialRules.biome(ModBiomes.SOULVORE_CAVERNS),
                        MaterialRules.sequence(
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, SOUL_SAND),
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH, SOUL_SOIL),
                                SOUL_STONE
                        )
                )
        );
    }

    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
}