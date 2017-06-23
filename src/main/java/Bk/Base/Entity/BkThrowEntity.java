package Bk.Base.Entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nullable;

/**
 * Created by User on 23.06.2017.
 */
public class BkThrowEntity extends EntityThrowable {

    public final float damage;
    public final boolean hasSpecialEffect;

    public BkThrowEntity(EntityLivingBase player, float damage, boolean hasSpecialEffect){
        super(player.getEntityWorld(), player);
        this.damage = damage;
        this.hasSpecialEffect = hasSpecialEffect;
    }
    public BkThrowEntity(EntityLivingBase throwerIn) {
        this(throwerIn, 0, false);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote && result.typeOfHit == RayTraceResult.Type.ENTITY){
            DamageSource source = getThrower() != null ?
                    (new EntityDamageSourceIndirect("arrow", this, getThrower())).setProjectile() :
                    (new EntityDamageSourceIndirect("arrow", this, this)).setProjectile();

            result.entityHit.attackEntityFrom(source, damage);
            if (hasSpecialEffect) specialEffect(getThrower(), result.entityHit);
        }

        setDead();
    }

    public void specialEffect(@Nullable Entity player, Entity target){}
}
