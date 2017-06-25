package bk.Base.Weapons;

import bk.Base.BaseVanilla.BkSword;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by User on 23.06.2017.
 */
public class BkRangeSword extends BkSword {

    private float range;

    public BkRangeSword(String name, ToolMaterial material){
        this(name, material, false, 0, 6.5F);
    }

    public BkRangeSword(String name, ToolMaterial material, boolean hasSpecialEffect, float timeoutSeconds, float range) {
        super(name, material, hasSpecialEffect, timeoutSeconds);
        this.range = range;
    }

    //Scanning by adjusted radius
    @Override
    protected RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
        float f = playerIn.rotationPitch;
        float f1 = playerIn.rotationYaw;
        double d0 = playerIn.posX;
        double d1 = playerIn.posY + (double)playerIn.getEyeHeight();
        double d2 = playerIn.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        //Only changed this, lol
        double d3 = (playerIn instanceof EntityPlayerMP) ? range : 5;
        Vec3d vec3d1 = vec3d.addVector((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
        return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
    }

    public void attack(World worldIn, EntityPlayer player){
        RayTraceResult res = rayTrace(worldIn, player, false);
        if (res.typeOfHit == RayTraceResult.Type.ENTITY){
            if (res.entityHit instanceof EntityLivingBase){
                player.attackTargetEntityWithCurrentItem(res.entityHit);
            }
        }
    }
}
