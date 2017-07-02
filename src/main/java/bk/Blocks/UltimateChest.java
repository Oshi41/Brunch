package bk.Blocks;

import bk.Base.BaseVanilla.IBkBase;
import bk.BookCraft;
import bk.Gui.GuiHandler;
import bk.Gui.TileEntity.UnlimitedTileEntity;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by User on 02.07.2017.
 */
public class UltimateChest extends BlockChest implements IBkBase {    
    
    public UltimateChest() {
        super(Type.BASIC);
        setCreativeTab(BookCraft.mixTab);
        setUnlocalizedName("ultimatechest");
        setRegistryName("ultimatechest");
    }    
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        UnlimitedTileEntity tileEntity = (UnlimitedTileEntity) worldIn.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(worldIn, pos, tileEntity);
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UnlimitedTileEntity(UnlimitedTileEntity.Tiles.Ultimate);
    }
    
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        return;
    }
    
    @Override
    public String getName() {
        return "ultimatechest";
    }
    
    @Override
    public void registerItemModel() {
        BookCraft.proxy.registerItemRenderer(new ItemBlock(this), 0, this.getName());
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;
        
        playerIn.openGui(BookCraft.instance, GuiHandler.UltimateGUIId, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
