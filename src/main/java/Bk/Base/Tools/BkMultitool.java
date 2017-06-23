package Bk.Base.Tools;

import Bk.Base.Materials;
import Bk.BookCraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by User on 23.06.2017.
 */
public class BkMultitool extends ItemTool {

    protected String name;
    public static BkMultitool instance = null;

    protected BkMultitool(Multitools materialIn) {
        this(materialIn.name, materialIn.material, materialIn.isUnbreakable);
    }
    public BkMultitool(String name, ToolMaterial material, boolean isUnbreakable){
        super(material, null);

        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(BookCraft.toolTab);
        setMaxDamage((int) (material.getMaxUses() * 2.5));
        if (isUnbreakable)
            setMaxDamage(-1);
        hasSubtypes = true;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return blockIn.getBlock().getHarvestLevel(blockIn) <= toolMaterial.getHarvestLevel();
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        return this.toolMaterial.getEfficiencyOnProperMaterial();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
            if (hook != 0) return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up()))
            {
                if (block == Blocks.GRASS || block == Blocks.GRASS_PATH)
                {
                    this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                    return EnumActionResult.SUCCESS;
                }

                if (block == Blocks.DIRT)
                {
                    switch ((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT))
                    {
                        case DIRT:
                            this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                            return EnumActionResult.SUCCESS;
                        case COARSE_DIRT:
                            this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                            return EnumActionResult.SUCCESS;
                    }
                }
            }

            return EnumActionResult.PASS;
        }
    }

    protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

        if (!worldIn.isRemote)
        {
            worldIn.setBlockState(pos, state, 11);
            stack.damageItem(1, player);
        }
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        int i = 0;
        for (Multitools tool : Multitools.values()){
            subItems.add(new ItemStack(new BkMultitool(tool), 1, i));
            i++;
        }
    }

    //region Help enum
    public static enum Multitools{
        DIAMOND("DiamondMultitool", ToolMaterial.DIAMOND),
        FORGOTTEN("ForgottenMultitool", Materials.Forgotten),
        KNOWLEDGE("KnowledgeMultitool", Materials.Knowledge),
        WISDOM("WisdomMultitool", Materials.Wisdom, true);

        public final String name;
        public final ToolMaterial material;
        public final boolean isUnbreakable;

        Multitools(String name, ToolMaterial material) {
            this(name, material, false);
        }
        Multitools(String name, ToolMaterial material, boolean isUnbreakable){
            this.name = name;
            this.material = material;
            this.isUnbreakable = isUnbreakable;
        }
    }
    //endregion
}
