package Bk.Base;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Created by User on 23.06.2017.
 */
public class Materials {

    public static Item.ToolMaterial Forgotten =
            EnumHelper.addToolMaterial("Forgotten", 3, 2000, 10.0F, 3.0F, 15);
    public static Item.ToolMaterial Knowledge =
            EnumHelper.addToolMaterial("Knowledge", 3, 1000, 9.0F, 5.0F, 16);
    public static Item.ToolMaterial Wisdom =
            EnumHelper.addToolMaterial("Wisdom", 4, 2500, 12.0F, 7.0F, 18);
    public static Item.ToolMaterial Cosmic =
            EnumHelper.addToolMaterial("Cosmic", 4, 7000, 17, 12, 10);
    public static Item.ToolMaterial Fictitious =
            EnumHelper.addToolMaterial("Fictitious", 4, 10000, 18, 13, 12);
    public static Item.ToolMaterial Speedy =
            EnumHelper.addToolMaterial("Speedy", 2, 100, 28, 20, 3);
    public static Item.ToolMaterial Legendary =
            EnumHelper.addToolMaterial("Legendary", 4, 7000, 19, 21, 15);
    public static Item.ToolMaterial Magician =
            EnumHelper.addToolMaterial("Magician", 3, 1561, 8.0F, 3.0F, 44);
    public static Item.ToolMaterial Unbreakium =
            EnumHelper.addToolMaterial("Unbreakium", 4, 64000, 8, 5, 10);

}
