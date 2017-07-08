package bk.Items;

import bk.Base.BaseVanilla.BkItem;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

import static net.minecraft.potion.PotionUtils.getEffectsFromStack;

/**
 * Created by User on 08.07.2017.
 */
public class PotionJar extends BkItem {
    
    public PotionJar(){
        super("potionjar");
        setCreativeTab(CreativeTabs.BREWING);
        setMaxStackSize(4);
    }
    
    /**
     * Use duration depends on effects amount
     * @param stack
     * @return
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return  Math.max(getEffectsFromStack(stack).size() * 10, 32);
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }
    
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        addPotionTooltip(stack, tooltip);
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        
        EntityPlayer player = (EntityPlayer) entityLiving;
        if (player == null || !player.capabilities.isCreativeMode) {
            stack.shrink(1);
    
            if (player != null)
                if (!player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE)))
                    Block.spawnAsEntity(worldIn, entityLiving.getPosition(), new ItemStack(Items.GLASS_BOTTLE));
        }
        
        //Make effect
        if (!worldIn.isRemote){
            
            for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)){
                if (effect.getPotion().isInstant()) {
                    effect.getPotion().affectEntity(player, player, entityLiving, effect.getAmplifier(), 1.0D);
                }
                else {
                    entityLiving.addPotionEffect(new PotionEffect(effect));
                }
            }
        }
        
        return stack;
    }
    
    //region Stolen methods
    
    @SideOnly(Side.CLIENT)
    public static void addPotionTooltip(ItemStack itemIn, List<String> lores) {
        List<PotionEffect> list = getEffectsFromStack(itemIn);
        List<Tuple<String, AttributeModifier>> list1 = Lists.<Tuple<String, AttributeModifier>>newArrayList();
    
        if (list.isEmpty()) {
            String s = I18n.translateToLocal("effect.none").trim();
            lores.add(TextFormatting.GRAY + s);
        }
        else {
            for (PotionEffect potioneffect : list) {
                String s1 = I18n.translateToLocal(potioneffect.getEffectName()).trim();
                Potion potion = potioneffect.getPotion();
                Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();
            
                if (!map.isEmpty()) {
                    for (Map.Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
                        AttributeModifier attributemodifier = entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        list1.add(new Tuple(entry.getKey().getName(), attributemodifier1));
                    }
                }
            
                if (potioneffect.getAmplifier() > 0) {
                    s1 = s1 + " " + I18n.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
                }
            
                if (potioneffect.getDuration() > 20) {
                    s1 = s1 + " (" + Potion.getPotionDurationString(potioneffect, potioneffect.getDuration()) + ")";
                }
            
                if (potion.isBadEffect()) {
                    lores.add(TextFormatting.RED + s1);
                }
                else {
                    lores.add(TextFormatting.BLUE + s1);
                }
            }
        }
    
        if (!list1.isEmpty()) {
            lores.add("");
            lores.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("potion.whenDrank"));
        
            for (Tuple<String, AttributeModifier> tuple : list1) {
                AttributeModifier attributemodifier2 = tuple.getSecond();
                double d0 = attributemodifier2.getAmount();
                double d1;
            
                if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2) {
                    d1 = attributemodifier2.getAmount();
                }
                else {
                    d1 = attributemodifier2.getAmount() * 100.0D;
                }
            
                if (d0 > 0.0D) {
                    lores.add(TextFormatting.BLUE + I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + tuple.getFirst())));
                }
                else if (d0 < 0.0D) {
                    d1 = d1 * -1.0D;
                    lores.add(TextFormatting.RED + I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + tuple.getFirst())));
                }
            }
        }
    }
    //endregion
}
