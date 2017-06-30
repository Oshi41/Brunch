package bk.PacketWork.ServerHandlers;

import bk.Base.Weapons.BkRangeSword;
import bk.PacketWork.Messages.AttackPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.time.Instant;

/**
 * Created by User on 30.06.2017.
 */
public class AttackServerHandler implements IMessageHandler<AttackPacket, IMessage> {   
    
    @Override
    public IMessage onMessage(AttackPacket message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            System.err.println("Client packet recived by server --> " + ctx.side);
            return null;
        }
    
        if (!message.isValid()) {
            System.err.println("Message was invilid" + message.toString());
            return null;
        }
    
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (player == null) {
            System.err.println("Attacker was null when packet was recived" + java.util.Date.from(Instant.now()));
            return null;
        }
    
        Entity target = player.getServerWorld().getEntityByID(message.getUUID());
        if (target == null) return null;
        
        if (player.getHeldItemMainhand().getItem() instanceof BkRangeSword){
            player.attackTargetEntityWithCurrentItem(target);
        }
        
        return null;
    }   
    
}
