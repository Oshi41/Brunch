package bk.PacketWork.ClientHandler;

import bk.PacketWork.Messages.Base.BasePacket;
import bk.PacketWork.Messages.LightningPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by User on 29.06.2017.
 */
public class ClientPacketHandler implements IMessageHandler<BasePacket, IMessage> {    
    
    @Override
    public IMessage onMessage(BasePacket message, MessageContext ctx) {
        
        if (ctx.side.isServer()){
            System.err.println("Server packet recived by client --> " + ctx.side);
            return null;
        }
        
        if (!message.isValid()){
            System.err.println("Message was invilid" + message.toString());
            return null;
        }
    
        chooseType(message, Minecraft.getMinecraft().world);
        
        return null;
    }
    
    //region Choose type
    private void chooseType(BasePacket message, WorldClient world){
        
        if (message instanceof LightningPacket){
            spawnLightning(world, ((LightningPacket) message).getPos());
        }
    } 
    //endregion
    
    //region Proccessing
    void spawnLightning(World world, Vec3d pos){
        world.spawnEntity(new EntityLightningBolt(world, pos.xCoord, pos.yCoord, pos.zCoord, false));
    }
    //endregion
}
