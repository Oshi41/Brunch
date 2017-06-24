package Bk.Base.Tools;

import Bk.Base.BaseVanilla.BkPickaxe;
import Bk.BookCraft;
import com.sun.glass.ui.Size;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by User on 23.06.2017.
 */
public class Hammer extends BkPickaxe {

    public Size _range;

    public static Hammer instance = null;
//    public Hammer(Hammers hammers) {
//        super(hammers.name);
//        setCreativeTab(BookCraft.toolTab);
//        //this.efficiencyOnProperMaterial
//    }

    public Hammer(Hammers hammers){
        this(hammers.name, hammers.size, hammers.material);
    }
    public Hammer(String name, Size range, Item.ToolMaterial material){
        super(name, material);
        _range = range;
        setCreativeTab(BookCraft.toolTab);
    }


    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        if (player.getEntityWorld().isRemote) return true;

        if (player.capabilities.isCreativeMode && canHarvestBlock((IBlockState) pos)) {
            return onBlockDestroyed(itemstack, player.getEntityWorld(), player.getEntityWorld().getBlockState(pos), pos, player);
        }
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (hand == EnumHand.MAIN_HAND){
            if (!player.isSneaking()) {
                for (ItemStack s : player.inventory.mainInventory) {
                    if (s != null && !s.isEmpty()) {
                        Item item = s.getItem();
                        if (item != null && item instanceof ItemBlock && ((ItemBlock) item).block instanceof BlockTorch) {
                            return item.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
                        }
                    }
                }
            }
            else {
                for (ItemStack s : player.inventory.mainInventory) {
                    if (s != null && !s.isEmpty()) {
                        Item item = s.getItem();
                        if (item != null && item instanceof ItemBlock && ((ItemBlock) item).canPlaceBlockOnSide(worldIn, pos, facing, player, s)){
                            return item.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
//                            boolean res = ((ItemBlock) item).placeBlockAt(s, playerIn, worldIn, pos, facing, hitX, hitY, hitZ, worldIn.getBlockState(pos));
//                             int i = 0;
//                            return res ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
                        }
                    }
                }
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (worldIn.isRemote) return true;

        if (entityLiving instanceof EntityPlayer){
            if (((EntityPlayer)entityLiving).isSneaking()) return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);


            BlockPos[] toBreak = getBlockRange(worldIn, pos, (EntityPlayer)entityLiving);

            for (BlockPos pos1 : toBreak){

                Block block = worldIn.getBlockState(pos1).getBlock();
                //Проходим только по тому, что можем срубить
                if (stack.canHarvestBlock(state)){

                    block.harvestBlock(worldIn, (EntityPlayer)entityLiving, pos1,
                            worldIn.getBlockState(pos1), worldIn.getTileEntity(pos1), stack);
                    worldIn.destroyBlock(pos1, false);
                }
            }
        }
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

    //region Helping enum
    public static enum Hammers{
        BUILDER_HAMMER("BuilderHammer", new Size(2,1), ToolMaterial.STONE),
        LIGHT_HAMMER("LightHammer", new Size(2,2), ToolMaterial.IRON),
        MJÖLLNIR("Mjöllnir", new Size(3,2), ToolMaterial.DIAMOND),
        WAR_HAMMER("WarHammer", new Size(2,1), ToolMaterial.DIAMOND),
        LEGENDARY_HAMMER("LegendaryHammer", new Size(5,3), ToolMaterial.DIAMOND);

        public final String name;
        public final Size size;
        public final ToolMaterial material;
        public final float damage;

        Hammers(String name, Size size, ToolMaterial material){
            this.name = name;
            this.size = size;
            this.material = material;
            this.damage = material.getDamageVsEntity();
        }
    }

    //endregion

    //region Helping methods
    private BlockPos[] getBlockRange(World worldIn, BlockPos pos, EntityPlayer player){
        ArrayList<BlockPos> poses = new ArrayList<BlockPos>();
        BlockPos begin = pos, end = pos;
        RayTraceResult orientation = rayTrace(worldIn, player, false);


        int depth = _range.height - 1,
                maxY = (_range.width - 1) / 2,
                minY = _range.width - maxY - 1,
                minSizeSquare = minY,
                maxSizeSquare = maxY;

        if (minY > 1){
            int addition = minY - 1;
            minY -= addition;
            maxY += addition;
        }

        switch (orientation.sideHit){
            case DOWN:
                begin = begin.add(-minSizeSquare, 0, -minSizeSquare);
                end = end.add(maxSizeSquare, depth , maxSizeSquare);
                break;
            case UP:
                begin = begin.add(-minSizeSquare, 0, -minSizeSquare);
                end = end.add(maxSizeSquare, -depth, maxSizeSquare);
                break;
            case SOUTH:
                begin = begin.add(-minSizeSquare, -minY, 0);
                end = end.add(maxSizeSquare, maxY, -depth);
                break;
            case NORTH:
                begin = begin.add(-minSizeSquare, -minY, 0);
                end = end.add(maxSizeSquare, maxY, depth);
                break;
            case WEST:
                begin = begin.add(0, -minY, -minSizeSquare);
                end = end.add(depth, maxY, maxSizeSquare);
                break;
            case EAST:
                begin = begin.add(0, -minY, -minSizeSquare);
                end = end.add(-depth, maxY, maxSizeSquare);
                break;
        }


        int xMin = Math.min(begin.getX(), end.getX()),
                xMax = Math.max(begin.getX(), end.getX()),

                yMin = Math.min(begin.getY(), end.getY()),
                yMax = Math.max(begin.getY(), end.getY()),

                zMin = Math.min(begin.getZ(), end.getZ()),
                zMax = Math.max(begin.getZ(), end.getZ());

        for (int x = xMin; x <= xMax; x++)
            for (int y = yMin; y <= yMax; y++)
                for (int z = zMin; z <= zMax; z++){
                    BlockPos temp = new BlockPos(x,y,z);
                    if (!poses.contains(temp) && !worldIn.isAirBlock(temp)){
                        poses.add(temp);
                    }
                }


        BlockPos[] result = new BlockPos[poses.size()];
        result = poses.toArray(result);
        return result;
    }

    @Override
    protected RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids){
        return super.rayTrace(worldIn, playerIn, useLiquids);
    }
    //endregion
}
