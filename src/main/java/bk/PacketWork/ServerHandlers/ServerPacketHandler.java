package bk.PacketWork.ServerHandlers;

import bk.Base.Weapons.BkRangeSword;
import bk.PacketWork.Messages.AttackPacket;
import bk.PacketWork.Messages.Base.BasePacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.time.Instant;

/**
 * Created by User on 29.06.2017.
 */
public class ServerPacketHandler implements IMessageHandler<BasePacket, IMessage> {
    
    
    @Override
    public IMessage onMessage(BasePacket message, MessageContext ctx) {
        
        if (ctx.side.isClient()){
            System.err.println("Client packet recived by server --> " + ctx.side);
            return null;
        }
        
        if (!message.isValid()){
            System.err.println("Message was invilid" + message.toString());
            return null;
        }
    
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (player == null){
            System.err.println("Attacker was null when packet was recived" + java.util.Date.from(Instant.now()));
            return null;
        }
        
        //choose Type
        chooseType(message, player);
        
        return null;
    }
    
    //region Choosing Type
    private void chooseType(BasePacket message, EntityPlayerMP player){
        
        if (message instanceof AttackPacket){
            Entity entity = null;
            if (checkEntity(player.getServerWorld(), ((AttackPacket) message).getUUID(), entity)){
                attackProcess(player, entity);
            }
        }
    }
    //endregion
    
    //region Processing
    
    void attackProcess(EntityPlayerMP attacker, Entity target){
        
        //todo send messages to client

        Item sword = attacker.getHeldItemMainhand().getItem();

        if (sword instanceof BkRangeSword) {
            attacker.attackTargetEntityWithCurrentItem(target);            
        }
    }    
    
    //endregion
    
    //region Help
    
    /**
     * Check for entity existance
     * @param server 
     * @param ID Unique entity id 
     * @param entity Returns founded entity
     * @return
     */
    private boolean checkEntity(WorldServer server, int ID, Entity entity){
        entity = server.getEntityByID(ID);
        return entity == null;
    }
    //endregion
}
