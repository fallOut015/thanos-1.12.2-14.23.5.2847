package thanos;

import net.minecraftforge.common.config.Config;

@Config(modid = Main.MODID, type = Config.Type.INSTANCE)
public class MainConfig {
    @Config.Comment("Thanos's HP (salud)")
    public static int thanos_hp = 500;
    @Config.Comment("Thanos's Attack (daño de ataque)")
    public static double thanos_attack = 10;
}
