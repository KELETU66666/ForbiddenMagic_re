/**
 * This class based on a class created by <Vazkii>.
 *
 * It was distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * File Created @ [May 15, 2014, 4:47:41 PM (GMT)]
 */

package keletu.forbiddenmagicre.compat.botania.flowers;

import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;

import java.util.List;

public class SubTileBloodthorn extends SubTileFunctional {
    public static LexiconEntry lexicon;

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(redstoneSignal > 0)
            return;
        final int range = 6;
        final int cost = 40;
        List<EntityLivingBase> entities = supertile.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(supertile.getPos().getX() - range, supertile.getPos().getY() - range, supertile.getPos().getZ() - range, supertile.getPos().getX() + range, supertile.getPos().getY() + range, supertile.getPos().getZ() + range));
        for(EntityLivingBase entity : entities) {
            if(!(entity instanceof EntityPlayer) && entity.getActivePotionEffect(RegistrarBloodMagic.SOUL_SNARE) == null && mana >= cost && !entity.world.isRemote) {
                entity.addPotionEffect(new PotionEffect(RegistrarBloodMagic.SOUL_SNARE, 60, 2));
                mana -= cost;
            }
        }
    }
    @Override
    public boolean acceptsRedstone() {
        return true;
    }
    @Override
    public int getColor() {
        return 0x7D0600;
    }
    @Override
    public int getMaxMana() {
        return 360;
    }
    @Override
    public LexiconEntry getEntry() {
        return lexicon;
    }
}