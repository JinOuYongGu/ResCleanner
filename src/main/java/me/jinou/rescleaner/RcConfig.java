package me.jinou.rescleaner;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author 69142
 */
public class RcConfig {
    static int purgeTimeInMins;
    static int ownerMoney;
    static int maxResSize;

    public static void loadConfigFromFile(FileConfiguration fileConfig) {
        purgeTimeInMins = fileConfig.getInt("purge-time-in-mins");
        ownerMoney = fileConfig.getInt("owner-money");
        maxResSize = fileConfig.getInt("max-res-size");
    }
}
