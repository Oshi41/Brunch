package bk.Blocks.Machines;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by User on 08.07.2017.
 */
public class PotionCompressor extends BlockContainer {
    
    
    protected PotionCompressor() {
        super(Material.ANVIL);
        setRegistryName("potioncompressor");
        setUnlocalizedName("potioncompressor");
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
