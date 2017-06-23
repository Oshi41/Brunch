package Bk.Base.Weapons;

import Bk.Base.BaseVanilla.BkItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by User on 23.06.2017.
 */
public class BkWeapon extends BkItem {

    private final Stream<Item> bullets;
    //public final Stream<Item> bullets;
    public long prev;
    public final long timeoutSec;


    public BkWeapon(String name,float timeoutSec, Item... bulltes) {
        super(name);
        this.bullets = Arrays.asList(bulltes).stream();
        this.timeoutSec = (long) (timeoutSec * 1000);
        //this.bullets.stream().anyMatch()
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
