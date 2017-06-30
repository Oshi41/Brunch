package bk.Events;

import bk.Base.Weapons.BkRangeSword;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by User on 26.06.2017.
 */
public class EventHook {

    @SubscribeEvent
    public void OnLiftClick(PlayerInteractEvent.LeftClickEmpty event){
        ItemStack itemStack = event.getEntityPlayer().getHeldItemMainhand();

        if (itemStack.getItem() instanceof BkRangeSword){
            ((BkRangeSword)itemStack.getItem()).performAttack(event.getWorld(), event.getEntityPlayer());
        }
    }
}
