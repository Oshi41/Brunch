package bk.Base.Entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.LinkedList;

/**
 * Created by User on 24.06.2017.
 */
public class BkEntityContainer extends Entity {

    public LinkedList<ItemStack> drops;
    private final AxisAlignedBB SIZE = new AxisAlignedBB(0, 0, 0, 0.45, 0.45, 0.45);

    public BkEntityContainer(World worldIn, LinkedList<ItemStack> drop, BlockPos pos) {
        super(worldIn);
        setPosition(pos.getX(), pos.getY(), pos.getZ());
        setSize(0.45F,0.45F);
        this.drops = drop;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (world.isAirBlock(getPosition().down()) && world.isAirBlock(getPosition().down(2))){
            move(MoverType.SELF, 0,-0.1,0);
        }
    }

    @Override
    protected void entityInit() {

    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return SIZE;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return SIZE;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {

        for (ItemStack stack : drops) {
            if (!player.inventory.addItemStackToInventory(stack))
                //Use written method, no dupe :)
                Block.spawnAsEntity(player.getEntityWorld(), getPosition(), stack);
        }
        setDead();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

}
