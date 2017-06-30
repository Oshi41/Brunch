package bk.Base.BaseVanilla;

import bk.BookCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import java.util.List;

/**
 * Created by User on 23.06.2017.
 */
public class BkSword extends ItemSword implements IBkBase {

    private boolean hasSpecialEffect;
    private String name;
    private long prev, timeout;

    public BkSword(String name, ToolMaterial material){
        this(name, material, false, 1);
    }
    public BkSword(String name, ToolMaterial material, boolean hasSpecialEffect, float timeoutSeconds) {
        super(material);
        this.hasSpecialEffect = hasSpecialEffect;
        this.name = name;
        this.timeout = (long) (timeoutSeconds * 1000);

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(BookCraft.swordsTab);
    }


    public boolean isHasSpecialEffect(){return hasSpecialEffect;}
    public void specialEffect(ItemStack stack, EntityPlayer player, Entity target){ }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (isHasSpecialEffect() && attacker instanceof EntityPlayer)
            if (System.currentTimeMillis() - prev >= timeout) {
                prev = System.currentTimeMillis();
                specialEffect(stack, (EntityPlayer) attacker, target);
            }

        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public String getName() {
        return name;
    }

    public void registerItemModel() {
        BookCraft.proxy.registerItemRenderer(this, 0, name);
    }
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        
        //tooltip.add(String.format("Usages {0}/{1}", stack.getItemDamage(), stack.getMaxDamage()));
    }
}
