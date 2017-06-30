package bk.PacketWork.ClientHandler;

import bk.PacketWork.Messages.LightningPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by User on 30.06.2017.
 */
public class LightningPacketHandler implements IMessageHandler<LightningPacket, IMessage> {
    
    @Override
    public IMessage onMessage(LightningPacket message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            System.err.println("Server packet recived by client --> " + ctx.side);
            return null;
        }
    
        if (!message.isValid()) {
            System.err.println("Message was invilid" + message.toString());
            return null;
        }
    
        World world = Minecraft.getMinecraft().world;
        Vec3d pos = message.getPos();
        world.spawnEntity(new EntityLightningBolt(world, pos.xCoord, pos.yCoord, pos.zCoord, false));
        
        return null;
    }
}
