package Bk.Base.Entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
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
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

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

//    @Override
//    public boolean attackEntityFrom(DamageSource source, float amount) {
//        if (source.getSourceOfDamage() instanceof EntityPlayer) {
//            onCollideWithPlayer((EntityPlayer) source.getSourceOfDamage());
//        }
//
//        return super.attackEntityFrom(source, amount);
//    }

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
    public boolean hitByEntity(Entity entityIn) {
        if (entityIn instanceof EntityPlayer){
            onCollideWithPlayer((EntityPlayer) entityIn);
        }
        return super.hitByEntity(entityIn);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getSourceOfDamage() instanceof EntityPlayer){
            onCollideWithPlayer((EntityPlayer) source.getSourceOfDamage());
            return true;
        }
        return false;
    }
}

//    }

    //    public LinkedList<ItemStack> drops;
//
//    public BkEntityContainer(World worldIn, LinkedList<ItemStack> drops, BlockPos pos) {
//        super(worldIn);
//        this.drops = drops;
//    }
//
//    @Override
//    public String toString() {
//        return "InventorySaveOrb";
//    }
//
//    //    public BkEntityContainer(World worldIn, LinkedList<ItemStack> drops, BlockPos pos) {
////        super(worldIn);
////        setPosition(pos.getX(), pos.getY(), pos.getZ());
////        setSize(0.7F, 0.7F);
////        setVelocity(0,0,0);
////        this.drops = drops;
////        setDropItemsWhenDead(true);
////    }
//
//    @Override
//    protected void entityInit() {
//
//    }
//
//    @Override
//    public Iterable<ItemStack> getArmorInventoryList() {
//        return null;
//    }
//
//    @Override
//    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
//        return null;
//    }
//
//    @Override
//    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
//
//    }
//
//    @Override
//    public EnumHandSide getPrimaryHand() {
//        return null;
//    }
//
//    @Override
//    public boolean attackEntityFrom(DamageSource source, float amount) {
//        if (source.getSourceOfDamage() instanceof EntityPlayer){
//            onCollideWithPlayer((EntityPlayer) source.getSourceOfDamage());
//            return true;
//        }
//        return false;
//        //return super.attackEntityFrom(source, amount);
//    }
//
//    @Override
//    protected void readEntityFromNBT(NBTTagCompound compound) {
//
//    }
//
//    @Override
//    protected void writeEntityToNBT(NBTTagCompound compound) {
//
//    }
//
//    @Override
//    public void onCollideWithPlayer(EntityPlayer player) {
//
//        for (ItemStack stack : drops){
//            if (!player.inventory.addItemStackToInventory(stack))
//                //Use written method, no dupe)
//                Block.spawnAsEntity(player.getEntityWorld(), getPosition(), stack);
//        }
//        setDead();
//    }
//
//    @Nullable
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox() {
//        return new AxisAlignedBB(0,0,0,0.7,0.7,0.7);
//    }
//
//    @Nullable
//    @Override
//    public AxisAlignedBB getCollisionBox(Entity entityIn) {
//        return getCollisionBoundingBox();
//    }
