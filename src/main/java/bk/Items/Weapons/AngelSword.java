package bk.Items.Weapons;

import bk.Base.Weapons.BkRangeSword;
import bk.Utils.EffectsHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by User on 26.06.2017.
 */
public class AngelSword extends BkRangeSword {


    public AngelSword() {
        super("AngelSword", ToolMaterial.DIAMOND, true, 3, 10);
    }

    @Override
    public void specialEffect(ItemStack stack, EntityPlayer player, Entity target) {
        EffectsHelper.spawnLightning(player, (EntityLivingBase) target, stack);
    }
}
