package net.nullcoil.soulscorch.world.biome.surface;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.nullcoil.soulscorch.block.ModBlocks;
import net.nullcoil.soulscorch.world.biome.ModBiomes;

public class ModMaterialRules {
    private static final MaterialRules.MaterialRule SOUL_STONE = makeStateRule(ModBlocks.SOUL_STONE);
    private static final MaterialRules.MaterialRule BEDROCK = makeStateRule(Blocks.BEDROCK);
    private static final MaterialRules.MaterialRule NETHERRACK = makeStateRule(Blocks.NETHERRACK);

    public static MaterialRules.MaterialRule makeRules() {
        return MaterialRules.sequence(
                // 1. Bedrock at bottom (same as vanilla)
                MaterialRules.condition(
                        MaterialRules.verticalGradient("bedrock_floor", YOffset.getBottom(), YOffset.aboveBottom(5)),
                        BEDROCK
                ),

                // 2. Bedrock at top (same as vanilla)
                MaterialRules.condition(
                        MaterialRules.not(MaterialRules.verticalGradient("bedrock_roof", YOffset.belowTop(5), YOffset.getTop())),
                        BEDROCK
                ),

                // 3. Your biome-specific rules
                MaterialRules.condition(
                        MaterialRules.biome(ModBiomes.SOULVORE_CAVERNS),
                        MaterialRules.sequence(
                                // Soul stone for floor surfaces
                                MaterialRules.condition(
                                        MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
                                        SOUL_STONE
                                ),
                                // Soul stone for ceiling surfaces
                                MaterialRules.condition(
                                        MaterialRules.STONE_DEPTH_CEILING_WITH_SURFACE_DEPTH,
                                        SOUL_STONE
                                ),
                                // Default to soul stone
                                SOUL_STONE
                        )
                ),

                // 4. Fallback to vanilla netherrack (in case other biomes are nearby)
                NETHERRACK
        );
    }

    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
}