package bk.Base.BaseVanilla;

import bk.BookCraft;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by User on 23.06.2017.
 */
public class BkPrivateBlock extends BlockContainer {

    private final String name;
    public static BkPrivateBlock instance = null;
    public final int GUIID;
    public UUID owner = null;

    protected BkPrivateBlock(String name, Material materialIn, int GUIID) {
        super(materialIn);
        this.name = name;
        this.GUIID = GUIID;

        setUnlocalizedName(name);
        setRegistryName(name);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        chekOrSetOwner(placer);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;

        if (playerIn.capabilities.isCreativeMode || chekOrSetOwner(playerIn)){
            if (!playerIn.isSneaking()) {
                TileEntity entity = worldIn.getTileEntity(pos);

                if (entity != null){
                    playerIn.openGui(BookCraft.instance, GUIID, worldIn, pos.getX(), pos.getY(), pos.getZ());


                    return true;
                }
            }
            return true;
        }
        else {
            playerIn.sendMessage(new TextComponentString("You are not an owner!"));
            return false;
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {

        if (player == null) return false;

        if (player.capabilities.isCreativeMode || chekOrSetOwner(player)){
            return super.removedByPlayer(state, world, pos, player, willHarvest);
        }
        else {
            Optional<EntityPlayer> name = world.playerEntities.stream().filter(x -> x.getUniqueID() == owner).findFirst();
            TextComponentString message = new TextComponentString("Only " +
                    (name.isPresent() ? name.get() : "owner") + " can destroy it");
            player.sendMessage(message);
            return false;
        }
    }

    //region Helping methods
    public boolean chekOrSetOwner(Entity entity){
        if (owner == null){
            owner = entity.getUniqueID();
            if (entity instanceof EntityPlayer)
                entity.sendMessage(new TextComponentString("Now you are an owner"));
            return true;
        }
        else {
            return owner == entity.getUniqueID();
        }
    }

    public void registerItemModel(ItemBlock itemBlock){
        BookCraft.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    //endregion
}
