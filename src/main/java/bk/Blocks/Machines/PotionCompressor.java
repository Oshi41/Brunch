package bk.Blocks.Machines;

import bk.BookCraft;
import bk.Gui.GuiHandler;
import bk.Gui.TileEntity.PotionCompressorTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by User on 08.07.2017.
 */
public class PotionCompressor extends BlockContainer {
    
    
    public PotionCompressor() {
        super(Material.ANVIL);
        setRegistryName("potioncompressor");
        setUnlocalizedName("potioncompressor");
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new PotionCompressorTileEntity();
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
            playerIn.openGui(BookCraft.instance, GuiHandler.PotionGuiID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
