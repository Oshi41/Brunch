package bk.Items;

import bk.Base.BaseVanilla.BkItem;
import bk.BookCraft;
import bk.PacketWork.Messages.ParticlePacket;
import bk.Proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by User on 01.07.2017.
 */
public class Magnet extends BkItem {
    
    private float range;
    private boolean isActive;
    private boolean onlyMainInventory;
    
    public Magnet(float range, boolean onlyMainInventory) {
        super("magnet");
        setMaxStackSize(1);
        setMaxDamage(Integer.MAX_VALUE);
        setCreativeTab(BookCraft.mixTab);
        this.range = range;
        this.onlyMainInventory = onlyMainInventory;
    }
    
    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        stack.setItemDamage(1000);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        
        if (playerIn.isSneaking() && !worldIn.isRemote){
            isActive = !isActive;
            String s = "Magnet is " + (isActive ? "enabled" : "diabled");
            playerIn.sendMessage(new TextComponentString(s));
        }
        
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.isRemote || !(entityIn instanceof  EntityPlayer) || worldIn.getTotalWorldTime() % 20 != 0) return;
        if (!isActive || (onlyMainInventory && itemSlot > 9) ) return;
        if (!((EntityPlayer) entityIn).capabilities.isCreativeMode && stack.getItemDamage() <= 0) return;
            
        Vec3d vec1 = entityIn.getPositionVector().addVector(range, range, range);
        Vec3d vec2 = entityIn.getPositionVector().subtract(range, range, range);
        AxisAlignedBB aabb = new AxisAlignedBB(vec1, vec2);
        List<Entity> entityList = worldIn.getEntitiesInAABBexcluding(entityIn, aabb, x -> x instanceof EntityItem);
        for (Entity entity : entityList) {
            entity.onCollideWithPlayer((EntityPlayer) entityIn);
            CommonProxy.simpleNetworkWrapper.sendToServer(new ParticlePacket(EnumParticleTypes.SPELL_MOB, entity.getPositionVector()));
//            Vec3d look = entity.getPositionVector();
//            worldIn.spawnParticle(EnumParticleTypes.SPELL_MOB, look.xCoord, look.yCoord, look.zCoord, 0.7, 0.5, 0.2, new int[0]);
        }
        
        if (entityList.size() > 0 && !((EntityPlayer) entityIn).capabilities.isCreativeMode)
            stack.damageItem(1, (EntityLivingBase) entityIn);
            
    }
    
    @Override
    public boolean hasEffect(ItemStack stack) {
        return isActive;
    }
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add("Uses " + stack.getItemDamage() + "/" + stack.getMaxDamage());
    }
}
