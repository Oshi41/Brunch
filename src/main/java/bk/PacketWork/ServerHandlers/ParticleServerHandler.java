package bk.PacketWork.ServerHandlers;

import bk.PacketWork.Messages.ParticlePacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.time.Instant;

/**
 * Created by User on 01.07.2017.
 */
public class ParticleServerHandler implements IMessageHandler<ParticlePacket, IMessage> {
    
    @Override
    public IMessage onMessage(ParticlePacket message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            System.err.println("Client packet recived by server --> " + ctx.side);
            return null;
        }
    
        if (!message.isValid()) {
            System.err.println("Message was invilid." + message.toString());
            return null;
        }
    
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (player == null) {
            System.err.println("Attacker was null when packet was recived" + java.util.Date.from(Instant.now()));
            return null;
        }
        
        player.getServerWorld().spawnParticle(message.getType(), message.getPos().xCoord,
                message.getPos().yCoord, message.getPos().zCoord, 1, 0, 0, 0, 1, new int[0]);
        
        return null;
    }
}
