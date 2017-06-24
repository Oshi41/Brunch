package Bk.Base.Entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedList;

/**
 * Created by User on 24.06.2017.
 */
public class BkEntityContainer extends Entity {

    public LinkedList<ItemStack> drops;

    public BkEntityContainer(World worldIn, LinkedList<ItemStack> drops, BlockPos pos) {
        super(worldIn);
        setPosition(pos.getX(), pos.getY(), pos.getZ());
        setSize(0.7F, 0.7F);
        setVelocity(0,0,0);
        setEntityBoundingBox(new AxisAlignedBB(0,0,0,0.7,0.7,0.7));
        setDropItemsWhenDead(true);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {

        for (ItemStack stack : drops){
            if (!stack.isEmpty())
                InventoryHelper.spawnItemStack(player.getEntityWorld(), posX, posY, posZ, stack);
        }
        setDead();
    }
}
