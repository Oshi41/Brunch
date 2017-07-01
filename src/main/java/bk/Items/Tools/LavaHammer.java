package bk.Items.Tools;

import bk.Base.Tools.Hammer;
import bk.Utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 25.06.2017.
 */
public class LavaHammer extends Hammer {

    private boolean canBurn;
    private long prevDate;

    public LavaHammer() {
        super(Hammers.LAVA_HAMMER);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.isSneaking() && System.currentTimeMillis() - prevDate > 1000){
            prevDate = System.currentTimeMillis();
            canBurn = !canBurn;

            playerIn.sendMessage(new TextComponentString("Auto-smelt is " + (canBurn ? "enabled" : "disabled")));
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (worldIn.isRemote || !(entityLiving instanceof EntityPlayer)) return false;

        return canBurn
                ? burnTask(worldIn, entityLiving, stack, pos)
                : super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);

        //return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

    public boolean burnTask(World worldIn, EntityLivingBase entityLiving, ItemStack stack, BlockPos pos){
        if (!(entityLiving instanceof EntityPlayer)) return false;
        
        ArrayList<BlockPos> poses = new ArrayList<>();
        if (entityLiving.isSneaking()){
            poses.add(pos);
        }
        else {
            poses.addAll(Arrays.asList(getBlockRange(worldIn, pos, (EntityPlayer)entityLiving)));
        }

        ArrayList<ItemStack> drops = Utils.destroyArea(worldIn, poses, stack, (EntityPlayer) entityLiving);
        

        LinkedList<ItemStack> temp = Utils.mergeItems(new LinkedList<>(drops));
        drops = new ArrayList<>();

        for (ItemStack stack1 : temp){
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack1);
            result.setCount(stack1.getCount());
            if (result != ItemStack.EMPTY){

                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack) > 0) {
                    int fortune = worldIn.rand.nextInt(EnchantmentHelper.
                            getEnchantmentLevel(Enchantments.FORTUNE, stack) + 2) - 1;
                    if (fortune < 0) fortune = 0;
                    result.setCount(result.getCount() * (fortune + 1));
                }
                drops.add(result.copy());
            }
            else
                drops.add(stack1);
        }

        for (int i = 0; i < drops.size(); i++){
            Block.spawnAsEntity(worldIn, pos, drops.get(i));
        }

        
        int damage = (int)(1.5 * EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));
        
        stack.damageItem(damage, entityLiving);
        return true;
    }
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        if (canBurn)
            tooltip.add("Damage increased to " + (int) (1.5 * EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)));
    }
}
