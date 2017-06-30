package bk.PacketWork.Messages;

import bk.PacketWork.Messages.Base.BasePacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by User on 29.06.2017.
 */
public class AttackPacket extends BasePacket {
    
    //private EntityTracker tracker;
    private int target;    
    
    public AttackPacket(){super();}
    
    public AttackPacket(EntityLivingBase target) {
        super();
        this.target = target.getEntityId();
        setValide();
    }
    
    @Override
    public void Read(ByteBuf buf) throws Exception {
        try{
            target = buf.readInt();
        }
        catch (Exception e){
            throw e;
        }
    }
    
    @Override
    public void Write(ByteBuf buf) {
        buf.writeInt(target);      
    }

    public int getUUID() {
        return target;
    }
}
