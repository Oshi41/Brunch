package Bk.Base.Tools;

import com.sun.glass.ui.Size;
import net.minecraft.block.state.IBlockState;

/**
 * Created by User on 23.06.2017.
 */
public class Boers extends Hammer {

    public Boers(String name, Size range, ToolMaterial material) {
        super(name, range, material);
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return blockIn.getBlock().getHarvestLevel(blockIn) <= toolMaterial.getHarvestLevel();
    }
}
