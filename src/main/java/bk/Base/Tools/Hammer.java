package bk.Base.Tools;

import bk.Base.BaseVanilla.BkPickaxe;
import bk.BookCraft;
import com.sun.glass.ui.Size;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by User on 23.06.2017.
 */
public class Hammer extends BkPickaxe {

    public Size _range;

    public static Hammer instance = null;

    public Hammer(Hammers hammers){
        this(hammers.name, hammers.size, hammers.material);
    }
    public Hammer(String name, Size range, Item.ToolMaterial material){
        super(name, material);
        this._range = range;
        setCreativeTab(BookCraft.toolTab);
        hasSubtypes = false;
        this.efficiencyOnProperMaterial /= MathHelper.sqrt(_range.width*_range.height);
    }


    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        if (player.getEntityWorld().isRemote) return false;

        if (player.capabilities.isCreativeMode && ForgeHooks.canToolHarvestBlock(player.getEntityWorld(), pos, itemstack)) {
            return onBlockDestroyed(itemstack, player.getEntityWorld(), player.getEntityWorld().getBlockState(pos), pos, player);
        }
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (hand != EnumHand.MAIN_HAND) return EnumActionResult.PASS;
        Optional<ItemStack> stack = Optional.empty();


        for (ItemStack s : player.inventory.mainInventory){
            if (!s.isEmpty() && s.getItem() instanceof ItemBlock){
                if (((ItemBlock)s.getItem()).block instanceof BlockTorch){
                    if (!player.isSneaking())
                        stack = Optional.of(s);
                }
                else {
                    if (player.isSneaking())
                        stack = Optional.of(s);
                }
            }
        }

//        if (player.isSneaking()){
//            stack = player.inventory.mainInventory.stream().filter(x -> !x.isEmpty() && x.getItem() instanceof ItemBlock && ((ItemBlock)x.getItem()).getBlock() instanceof BlockTorch).findFirst();
//        }
//        else {
//            stack = player.inventory.mainInventory.stream().filter(x -> !x.isEmpty() && x.getItem() instanceof ItemBlock && !(((ItemBlock)x.getItem()).getBlock() instanceof BlockTorch)).findFirst();
//        }

        if (stack.isPresent()){
            return tryToPlaceBlock(player, worldIn, pos, hand, facing, hitX, hitY, hitZ, (ItemBlock) stack.get().getItem(), stack.get());
        }
        return EnumActionResult.PASS;
    }

    //private boolean _isBreakingNow;
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (worldIn.isRemote) return true;
        //if (_isBreakingNow) return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);

        if (entityLiving instanceof EntityPlayer){
            if (((EntityPlayer)entityLiving).isSneaking()) return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);


            BlockPos[] toBreak = getBlockRange(worldIn, pos, (EntityPlayer)entityLiving);

            //_isBreakingNow = true;
            for (BlockPos pos1 : toBreak){

                Block block = worldIn.getBlockState(pos1).getBlock();
                //Проходим только по тому, что можем срубить
                if (ForgeHooks.canToolHarvestBlock(worldIn, pos1, stack)){

                    block.harvestBlock(worldIn, (EntityPlayer)entityLiving, pos1,
                            worldIn.getBlockState(pos1), worldIn.getTileEntity(pos1), stack);
                    worldIn.destroyBlock(pos1, false);
                }
            }
            //_isBreakingNow = false;
        }
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

//    @Override
//    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
//        for (Hammers h : Hammers.values()){
//            subItems.add(new ItemStack(new Hammer(h)));
//        }
//    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add("Usage " + stack.getItemDamage() + " / " + stack.getMaxDamage());
        tooltip.add("Digging size is "+ _range.width + " by " + _range.height);
        tooltip.add("Efficency is " + efficiencyOnProperMaterial);
        tooltip.add("Name is " + name);
        tooltip.add("Unlocolized name is " + getUnlocalizedName());
    }

    @Override
    public int getMetadata(ItemStack stack) {
        return super.getMetadata(stack);
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


    private  EnumActionResult tryToPlaceBlock(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, ItemBlock itemBlock, ItemStack itemstack){

        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(facing);
        }

        //ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(itemBlock.block, pos, false, facing, (Entity)null))
        {
            int i = itemBlock.getMetadata(itemstack.getMetadata());
            IBlockState iblockstate1 = itemBlock.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);

            if (itemBlock.placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
            {
                SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }
    //endregion
}
