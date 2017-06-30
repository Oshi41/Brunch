package bk.Utils;

import bk.PacketWork.Messages.LightningPacket;
import bk.Proxy.CommonProxy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Created by User on 24.06.2017.
 */
public class EffectsHelper {

    public static void setOnFire(EntityLivingBase target, int seconds){
        World world = target.getEntityWorld();

        for (int i = 0; i < 5; i++){
            BlockPos pos = target.getPosition().add(MathHelper.getInt(world.rand, -5, 5),MathHelper.getInt(world.rand, -5, 5),MathHelper.getInt(world.rand, -5, 5));
            world.spawnParticle(EnumParticleTypes.FLAME, pos.getX(), pos.getY(), pos.getZ(), 1,1,1,new int[0]);
        }

        target.setFire(seconds);
    }

    public static void tryToHeal(EntityPlayer player, ItemStack stack, float healAmount){

        int damage = 1;
        float heal = healAmount;
        if (player.isSneaking()){
            damage = 3;
            healAmount *= 2;
        }

        player.heal(heal);
        if (!player.capabilities.isCreativeMode)
            stack.damageItem(damage, player);
    }

    public static void poisonTarget(EntityLivingBase target, int seconds){
        target.addPotionEffect(new PotionEffect(MobEffects.POISON, seconds, 2));
    }

    public static void spawnLightning(EntityPlayer player, EntityLivingBase target, ItemStack stack){
        if (player.getPositionVector().distanceTo(target.getPositionVector()) > 5){
            EntityLightningBolt entityLightningBolt = new EntityLightningBolt(target.getEntityWorld(),
                    target.posX, target.posY, target.posZ, true);
            target.getEntityWorld().spawnEntity(entityLightningBolt);
            if (!player.capabilities.isCreativeMode)
                stack.damageItem(2, player);
            
            if (player instanceof EntityPlayerMP){                
                ((EntityPlayerMP) player).getServerWorld().addScheduledTask(() -> {
                    for (EntityPlayerMP playerMP : ((EntityPlayerMP) player).getServerWorld().getPlayers(EntityPlayerMP.class,
                            x -> x.dimension == player.dimension))
                        CommonProxy.simpleNetworkWrapper.sendTo(new LightningPacket(target.getPositionVector()), playerMP);
                });
            }
        }
    }


}
