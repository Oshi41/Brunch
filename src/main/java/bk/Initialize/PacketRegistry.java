package bk.Initialize;

import bk.PacketWork.ClientHandler.LightningPacketHandler;
import bk.PacketWork.Messages.AttackPacket;
import bk.PacketWork.Messages.LightningPacket;
import bk.PacketWork.Messages.ParticlePacket;
import bk.PacketWork.ServerHandlers.AttackServerHandler;
import bk.PacketWork.ServerHandlers.ParticleServerHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by User on 30.06.2017.
 */
public class PacketRegistry {
    
    public static void init(SimpleNetworkWrapper simpleNetworkWrapper){
        simpleNetworkWrapper.registerMessage(AttackServerHandler.class, AttackPacket.class, 1, Side.SERVER );
        simpleNetworkWrapper.registerMessage(LightningPacketHandler.class, LightningPacket.class, 2, Side.CLIENT);
        simpleNetworkWrapper.registerMessage(ParticleServerHandler.class, ParticlePacket.class, 3, Side.SERVER);
    }
}
