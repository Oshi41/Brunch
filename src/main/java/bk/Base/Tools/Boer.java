package bk.Base.Tools;

import bk.Base.Materials;
import com.sun.glass.ui.Size;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by User on 23.06.2017.
 */
public class Boer extends Hammer {

    //public Boer(String name, Size range, ToolMaterial material) {
//        super(name, range, material);
//    }
    
    public Boer(Boers boers){
        super(boers.name, boers.size, boers.material);
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return blockIn.getBlock().getHarvestLevel(blockIn) <= toolMaterial.getHarvestLevel();
    }
    
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (worldIn.isRemote) return true;
    
        if (entityLiving instanceof EntityPlayer) {
            if (((EntityPlayer) entityLiving).isSneaking())
                return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
        
        
            BlockPos[] toBreak = getBlockRange(worldIn, pos, (EntityPlayer) entityLiving);
        
            //_isBreakingNow = true;
            for (BlockPos pos1 : toBreak) {
            
                Block block = worldIn.getBlockState(pos1).getBlock();
                //Проходим только по тому, что можем срубить
                if (this.canHarvestBlock(worldIn.getBlockState(pos1))) {
                
                    block.harvestBlock(worldIn, (EntityPlayer) entityLiving, pos1,
                            worldIn.getBlockState(pos1), worldIn.getTileEntity(pos1), stack);
                    worldIn.destroyBlock(pos1, false);
                }
            }
            //_isBreakingNow = false;
        }
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }
    
    public enum Boers{
        BASE_DRILL("basedrill", new Size(2,2), ToolMaterial.DIAMOND),
        DEEP_DRILL("deepdrill", new Size(3,4), Materials.Wisdom);
        
        public final  String name;
        public final  Size size;
        public final  ToolMaterial material;
        
        Boers(String name, Size size, ToolMaterial toolMaterial){
            this.name = name;
            this.size = size;
            this.material = toolMaterial;
        }
    }
}
