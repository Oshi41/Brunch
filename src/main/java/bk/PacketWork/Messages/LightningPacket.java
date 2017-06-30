package bk.PacketWork.Messages;

import bk.PacketWork.Messages.Base.BasePacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;

/**
 * Created by User on 29.06.2017.
 */
public class LightningPacket extends BasePacket {
    
    private Vec3d pos;
    
    public Vec3d getPos(){
        return pos;
    }
    
    public LightningPacket(){super();}
    
    public LightningPacket(Vec3d pos) {
        this.pos = pos;
        setValide();
    }
    
    @Override
    public void Read(ByteBuf buf) throws Exception {
        try {
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            pos = new Vec3d(x, y, z);
        }
        catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    public void Write(ByteBuf buf) {
        buf.writeDouble(pos.xCoord);
        buf.writeDouble(pos.yCoord);
        buf.writeDouble(pos.zCoord);
    }    
}
