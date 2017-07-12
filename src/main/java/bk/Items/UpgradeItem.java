package bk.Items;

import bk.Base.BaseVanilla.BkItem;
import bk.BookCraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

import java.util.List;

/**
 * Created by User on 10.07.2017.
 */
public class UpgradeItem extends BkItem {
    
    public static String powerName = "power";
    public static String speedName = "speed";
    public static String rangeName = "range";
    
    private static int maxValue = 5000; 
    
    public UpgradeItem() {
        super("upgradeitem");
        setMaxStackSize(4);
        setCreativeTab(BookCraft.mixTab);
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        int speed = compound.getInteger(speedName);
        int power = compound.getInteger(powerName);
        int range = compound.getInteger(rangeName);
        return (speed + power + range) / (5000 * 3);
    }
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        if (!stack.hasTagCompound() || stack.getTagCompound().hasNoTags()) return;
        NBTTagCompound compound = stack.getTagCompound();
        
        if (compound.hasKey(rangeName))
            tooltip.add("Range is " + compound.getInteger(rangeName));
        if (compound.hasKey(speedName))
            tooltip.add("Speed is " + compound.getInteger(speedName));
        if (compound.hasKey(powerName))
            tooltip.add("Poswer is " + compound.getInteger(powerName));
    }
    
    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.hasTagCompound() && !stack.getTagCompound().hasNoTags();
    }
    
    /**
     * Add tag to ItemStack
     * @param stack
     * @param amount
     * @param tagName
     */
    public static void addTag(ItemStack stack, int amount, String tagName){
        NBTTagCompound compound = stack.getTagCompound();
        
        if (compound == null || compound.hasNoTags()) {
            compound = new NBTTagCompound();
            stack.setTagCompound(compound);
        }
        if (compound.hasKey(tagName)){
            int tempSpeed = compound.getInteger(tagName);
            tempSpeed = MathHelper.clamp(tempSpeed + amount, 0, maxValue);            
            compound.removeTag(tagName);
            compound.setInteger(tagName, tempSpeed);            
        }
        else         
        compound.setInteger(tagName, MathHelper.clamp(amount,0,maxValue));
    }
    
    public static ItemStack customizeUpgrade(ItemStack stack, int speed, int range, int power){
        addTag(stack, speed, speedName);
        addTag(stack, power, powerName);
        addTag(stack, range, rangeName);
        return stack;
    }
}
