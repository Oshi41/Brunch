package bk.PacketWork.Messages.Base;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by User on 29.06.2017.
 */
public class BasePacket implements IMessage {
    
    private boolean isValide;
    public boolean isValid(){
        return isValide;
    }
    
    // for use by the message handler only
    public BasePacket(){isValide = false;}
    
    
    /**
     * Use
     * {@link BasePacket#Read(ByteBuf)} 
     * insted of this
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        try{
            Read(buf);
        }
        catch (Exception e) {
            System.err.println("Error while reading packet: " + e);
            return;
        }
        isValide = true;
    }
    
    /**
     * Use
     * {@link BasePacket#Write(ByteBuf)}
     * insted of this
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        if (isValide){
            Write(buf);
        }
    }
    
    
    //Use it to write bytes
    public void Read(ByteBuf buf) throws Exception {}
    //Use it to write bytes
    public void Write(ByteBuf buf){}
}
