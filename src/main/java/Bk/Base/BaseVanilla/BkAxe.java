package Bk.Base.BaseVanilla;

import Bk.Base.Entity.BkEntityContainer;
import Bk.BookCraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 24.06.2017.
 */
public class BkAxe extends ItemAxe {

    public final String name;
    public static BkAxe instance = null;
    private final int amount;
    private final double range;

    protected BkAxe(String name,ToolMaterial material, float damage, float speed, int amount, double range) {
        super(material, damage, speed);
        this.name = name;
        this.range = range;
        this.amount = amount;
        setUnlocalizedName(name);
        setRegistryName(name);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (state.getBlock() instanceof BlockLog && entityLiving instanceof EntityPlayer){
            chopTree(stack, worldIn, state, pos, (EntityPlayer) entityLiving);
        }

        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }


    //region Chopping task
    private void chopTree(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityPlayer player){
        LinkedList<BlockPos> logsPoses = findLogs(worldIn, pos);
        LinkedList<BlockPos> leavesPoses = findLeaves(worldIn, logsPoses);
        LinkedList<ItemStack> drop = destroyBlocks(worldIn, player, logsPoses, leavesPoses);

        BkEntityContainer entityContainer = new BkEntityContainer(worldIn, drop, pos);
        worldIn.spawnEntity(entityContainer);
        //todo spawn entity with container
    }

    private LinkedList<BlockPos> findLogs(World worldIn, BlockPos pos){

        int index = 0, lowY = pos.getY(), x, y, z;
        LinkedList<BlockPos> list = new LinkedList<>();
        BlockPos newPos;
        //First block
        list.add(pos);

        do{
            BlockPos currentLog = list.get(index);

            for (x = -1; x <= 1; x++)
                for (y =  -1; y <= 1; y++) {
                    for (z = -1; z <= 1; z++) {

                        newPos = currentLog.add(x, y, z);
                        //Check ranged
                        if (list.size() > amount && pos.getDistance(newPos.getX(), newPos.getY(), newPos.getZ()) > range) break;

                        if (!worldIn.isAirBlock(newPos)) {
                            if (worldIn.getBlockState(newPos) instanceof BlockLog) {
                                if (!list.contains(newPos))
                                    list.add(newPos);
                            }
                        }
                    }
                }
        }
        while (++index < list.size());

        if (list.contains(pos))
            list.remove(pos);

        return list;
    }
    private LinkedList<BlockPos> findLeaves(World world, LinkedList<BlockPos> poses){
        LinkedList<BlockPos> leaves = new LinkedList<>();

        for (BlockPos pos : poses){
            for (int x = -2; x <= 2; x++)
                for (int y = -2; y <= 2; y++)
                    for (int z = -2; z <= 2; z++){
                        BlockPos newPos = pos.add(x,y,z);
                        if (world.isAirBlock(pos)) continue;

                        Block block = world.getBlockState(newPos).getBlock();
                        if (block.isLeaves(world.getBlockState(newPos), world, newPos)){
                            if (!leaves.contains(newPos))
                                leaves.add(newPos);
                        }
                    }
        }

        return leaves;
    }
    private LinkedList<ItemStack> destroyBlocks(World world, EntityPlayer player, LinkedList<BlockPos>... pos){

        LinkedList<BlockPos> poses = new LinkedList<>();

        for(LinkedList<BlockPos> tempPos : pos){
            poses.addAll(tempPos);
        }
        LinkedList<ItemStack> drops = new LinkedList<>();
        while (poses.size() > 0){

            IBlockState state = world.getBlockState(poses.getFirst());
            Block block = state.getBlock();
            List<ItemStack> tempDrop = block.getDrops(world, poses.getFirst(), state,
                    EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand()));
            if (block.removedByPlayer(state, world, poses.getFirst(), player, true)) {
                if (tempDrop.size() > 0)
                    drops.addAll(tempDrop);
            }

            poses.remove();
        }
        return drops;
    }
    private int getTreeHeight(World world, BlockPos pos){
        BlockPos down = pos,
                up = pos;
        boolean findUp,
                findDown;
        int y = 0;

        do {
            findDown = false;
            y--;
            for (int x = -1; x <= 1; x++)
                for (int z = -1; z <= 1; z++){
                    BlockPos newPos = pos.add(x,y,z);
                    if (world.isAirBlock(newPos)) continue;

                    Block block = world.getBlockState(newPos).getBlock();
                    if (block instanceof BlockLog){
                        down = newPos;
                        findDown = true;
                    }
            }
        }
        while (findDown);

        y = 0;
        do {
            findUp = false;
            y++;
            for (int x = -1; x <= 1; x++)
                for (int z = -1; z <= 1; z++){
                    BlockPos newPos = pos.add(x,y,z);
                    if (world.isAirBlock(newPos)) continue;

                    Block block = world.getBlockState(newPos).getBlock();
                    if (block instanceof BlockLog){
                        up = newPos;
                        findUp = true;
                    }
                }
        }
        while (findUp);

        int result =  up.getY() - down.getY();
        return result > range
                ? (int) range
                : result;
    }

    //endregion

    @SubscribeEvent
    public void getPlayerBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (event.getEntityPlayer().isSneaking()) return;

        event.setNewSpeed(event.getOriginalSpeed() / getTreeHeight(event.getEntityPlayer().getEntityWorld(), event.getPos()) * (EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, event.getEntityPlayer().getHeldItemMainhand()) + 1));
    }


    public void registerItemModel() {
        BookCraft.proxy.registerItemRenderer(this, 0, name);
    }
}
