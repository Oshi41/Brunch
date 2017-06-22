package Bk.Base;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Created by User on 23.06.2017.
 */
public class Materials {

    public static Item.ToolMaterial Forgotten = EnumHelper.addToolMaterial("Forgotten", 3, 2000, 10.0F, 3.0F, 15);
    public static Item.ToolMaterial Knowledge = EnumHelper.addToolMaterial("Knowledge", 3, 1000, 9.0F, 5.0F, 16);
    public static Item.ToolMaterial Wisdom = EnumHelper.addToolMaterial("Wisdom", 4, 2500, 12.0F, 7.0F, 18);
    public static Item.ToolMaterial Cosmic = EnumHelper.addToolMaterial("Cosmic", 4, 7000, 17, 12, 10);
}
