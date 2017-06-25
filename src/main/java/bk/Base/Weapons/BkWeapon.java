package bk.Base.Weapons;

import bk.Base.BaseVanilla.BkItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by User on 23.06.2017.
 */
public class BkWeapon extends BkItem {

    private final List<ItemStack> bullets;
    public long prev;
    public final long timeoutSec;
    public final EntityThrowable bulletEntity;

    public BkWeapon(String name, int maxDamage, float timeoutSec, EntityThrowable entityThrowable, ItemStack... bulltes) {
        super(name);
        this.bullets = Arrays.asList(bulltes);
        this.timeoutSec = (long) (timeoutSec * 1000);
        this.bulletEntity = entityThrowable;
        setMaxDamage(maxDamage);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        //Find slot with bullets
        Optional<Slot> slot = playerIn.inventoryContainer.inventorySlots.stream().filter(x ->
                bullets.stream().anyMatch(y -> y == x.getStack())).findFirst();

        //Find bullets
        if (slot.isPresent()) {
            if (System.currentTimeMillis() - prev >= timeoutSec) {
                prev = System.currentTimeMillis();
                bulletEntity.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0, 3, 1F);
                worldIn.spawnEntity(bulletEntity);
                setDamage(playerIn.getHeldItem(handIn), 1);
                slot.get().decrStackSize(1);
                return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            }
        } else {
            playerIn.sendMessage(new TextComponentString("You need a(n) " +
                    bullets.get(0).getUnlocalizedName()));

            for (int i = 1; i < bullets.size(); i++)
                playerIn.sendMessage(new TextComponentString("or " +
                        bullets.get(i).getUnlocalizedName()));
            return ActionResult.newResult(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
