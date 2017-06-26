package bk.Base.Entity;

import bk.Base.Weapons.BkRangeSword;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by User on 26.06.2017.
 */
public class BkSwordEntity extends EntityThrowable {

    final Vec3d startVector;
    final float range;

    public BkSwordEntity(World worldIn, EntityPlayer throwerIn, Float range) {
        super(worldIn, throwerIn);

        this.startVector = throwerIn.getPositionVector();
        this.range = range;
        setSize(0.1F,0.1F);
        setNoGravity(true);
        setHeadingFromThrower(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw,
                1,7,0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        //Reach the end distance
        if (getPositionVector().distanceTo(startVector) >= range)
            this.setDead();
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.typeOfHit != RayTraceResult.Type.ENTITY
                || result.entityHit.equals(getThrower()))
            return;
        
        EntityPlayer player = (EntityPlayer) getThrower();
        if (result.entityHit instanceof EntityLivingBase
                && getThrower().getHeldItemMainhand().getItem() instanceof BkRangeSword) {
//            ((BkRangeSword) getThrower().getHeldItemMainhand().getItem()).performAttack(((EntityPlayer) getThrower()), result.entityHit);
        }
        setDead();
    }
}
