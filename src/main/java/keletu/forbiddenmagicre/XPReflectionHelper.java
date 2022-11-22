package keletu.forbiddenmagicre;

import java.lang.reflect.Field;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;

public class XPReflectionHelper {
    public static Field stupidMojangProtectedVariable;

    static {
        try {
            stupidMojangProtectedVariable = ReflectionHelper.findField(EntityLiving.class, "experienceValue", "field_70728_aV");
            stupidMojangProtectedVariable.setAccessible(true);
        }
        catch(Exception e){
            LogHandler.log(Level.ERROR, e.toString());
            e.printStackTrace();
        }
    }

    public static int getXP(EntityLiving entityLiving) {
        try {
            return stupidMojangProtectedVariable.getInt(entityLiving);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
