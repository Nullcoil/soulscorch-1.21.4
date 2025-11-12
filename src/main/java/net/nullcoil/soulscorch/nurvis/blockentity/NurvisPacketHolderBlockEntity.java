package net.nullcoil.soulscorch.nurvis.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.nullcoil.soulscorch.enums.NurvisPacketType;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;

public class NurvisPacketHolderBlockEntity extends BlockEntity {
    protected boolean hasPacket;
    protected NurvisPacketParent heldPacket;

    protected NurvisPacketHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.hasPacket = false;
        this.heldPacket = null;
    }

    public void createPacket(NurvisPacketType type) {
        if(heldPacket != null) {
            NurvisPacketParent packet = type.create(this.world);
            this.heldPacket = packet;
            this.toggleHas();
        }
    }

    public void toggleHas() {
        this.hasPacket = !this.hasPacket;
    }


}
