package bk.Blocks;

import bk.Base.BaseVanilla.IBkBase;
import bk.BookCraft;
import bk.Gui.GuiHandler;
import bk.Gui.TileEntity.SingularityTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by User on 07.07.2017.
 */
public class SingularityChest extends BlockContainer implements IBkBase {    
    
    public static final String name = "singularitychest";
    
    public SingularityChest() {
        super(Material.ROCK);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(BookCraft.mixTab);
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new SingularityTileEntity();
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) 
            playerIn.openGui(BookCraft.instance, GuiHandler.SingularityGUIId, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        IInventory inventory = (IInventory) worldIn.getTileEntity(pos);
        //Drop items with la-a-a-arge stacks (up to int.Max)
        if (inventory != null){
            for (int i = 0; i < inventory.getSizeInventory(); i++){
                if (!inventory.getStackInSlot(i).isEmpty())
                    Block.spawnAsEntity(worldIn, pos, inventory.getStackInSlot(i));
            }
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void registerItemModel() {
    }
}
