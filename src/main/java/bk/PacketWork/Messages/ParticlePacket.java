package bk.PacketWork.Messages;

import bk.PacketWork.Messages.Base.BasePacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;

/**
 * Created by User on 01.07.2017.
 */
public class ParticlePacket extends BasePacket {
    
    private int argumentCount;
    private Vec3d pos;
    
    public ParticlePacket() {super();}
    public ParticlePacket(EnumParticleTypes type, Vec3d pos){
        this.argumentCount = type.getArgumentCount();
        this.pos = pos;
        setValide();
    }
    
    public EnumParticleTypes getType(){ return EnumParticleTypes.getParticleFromId(argumentCount);}
    public Vec3d getPos(){return pos;}
    
    
    @Override
    public void Write(ByteBuf buf) {
        buf.writeInt(argumentCount);
        buf.writeDouble(pos.xCoord);
        buf.writeDouble(pos.yCoord);
        buf.writeDouble(pos.zCoord);
    }
    
    @Override
    public void Read(ByteBuf buf) throws Exception {
        try{
            argumentCount = buf.readInt();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            pos = new Vec3d(x,y,z);
        }
        catch (Exception e){
            throw e;
        }
    }
}
