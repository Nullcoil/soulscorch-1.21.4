//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.nullcoil.soulscorch.gui.screen.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.nullcoil.soulscorch.screen.SoulBrewingStandScreenHandler;

@Environment(EnvType.CLIENT)
public class SoulBrewingStandScreen extends HandledScreen<SoulBrewingStandScreenHandler> {
    private static final Identifier FUEL_LENGTH_TEXTURE = Identifier.of("soulscorch","container/brewing_stand/soul_fuel_length");
    private static final Identifier BREW_PROGRESS_TEXTURE = Identifier.ofVanilla("container/brewing_stand/brew_progress");
    private static final Identifier BUBBLES_TEXTURE = Identifier.ofVanilla("container/brewing_stand/bubbles");
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/brewing_stand.png");
    private static final int[] BUBBLE_PROGRESS = new int[]{29, 24, 20, 16, 11, 6, 0};

    public SoulBrewingStandScreen(SoulBrewingStandScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
        int k = ((SoulBrewingStandScreenHandler)this.handler).getFuel();
        int l = MathHelper.clamp((18 * k + 20 - 1) / 20, 0, 18);
        if (l > 0) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, FUEL_LENGTH_TEXTURE, 18, 4, 0, 0, i + 60, j + 44, l, 4);
        }

        int m = ((SoulBrewingStandScreenHandler)this.handler).getBrewTime();
        if (m > 0) {
            int n = (int)(28.0F * (1.0F - (float)m / 200.0F));
            if (n > 0) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, BREW_PROGRESS_TEXTURE, 9, 28, 0, 0, i + 97, j + 16, 9, n);
            }

            n = BUBBLE_PROGRESS[m / 2 % 7];
            if (n > 0) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, BUBBLES_TEXTURE, 12, 29, 0, 29 - n, i + 63, j + 14 + 29 - n, 12, n);
            }
        }

    }
}
