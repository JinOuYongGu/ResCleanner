package me.jinou.rescleaner;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidenceManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 69142
 */
public class RcUtil {
    static public boolean purgeRes() {
        Residence resPlugin = Residence.getInstance();
        ResidenceManager resManager = resPlugin.getResidenceManager();

        Map<String, ClaimedResidence> resNameList =
                new HashMap<>(resManager.getResidences());

        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();

        HashMap<UUID, OfflinePlayer> playerMapUuid = new HashMap<>(100);
        HashMap<String, OfflinePlayer> playerMapName = new HashMap<>(100);
        for (OfflinePlayer player : offlinePlayers) {
            playerMapUuid.put(player.getUniqueId(), player);
            playerMapName.put(player.getName(), player);
        }

        long curTime = System.currentTimeMillis();
        int purgeTime = RcConfig.purgeTimeInMins;

        Bukkit.getLogger().info("Start trying to clean Residences from " + playerMapUuid.size() + " players and " + resNameList.size() + " res");

        int skipped = 0;
        int purgedRes = 0;
        for (Map.Entry<String, ClaimedResidence> resName : resNameList.entrySet()) {
            ClaimedResidence res = resName.getValue();
            if (res == null) {
                continue;
            }

            OfflinePlayer player = playerMapUuid.get(res.getOwnerUUID());

            if (player == null) {
                player = playerMapName.get(res.getOwner());
            }

            if (player == null) {
                skipped++;
                continue;
            }

            if (res.getMainArea().getXSize() > RcConfig.maxResSize || res.getMainArea().getZSize() > RcConfig.maxResSize) {
                skipped++;
                continue;
            }

            if (ResCleaner.vaultEco.getBalance(player) > RcConfig.ownerMoney) {
                skipped++;
                continue;
            }

            long lastPlayed = player.getLastPlayed();
            int offlineTime = (int) ((curTime - lastPlayed) / 1000 / 60);
            if (offlineTime < purgeTime) {
                skipped++;
                continue;
            }

            //TODO: add perm check here

            if ("server land".equalsIgnoreCase(res.getOwner()) || "server_land".equalsIgnoreCase(res.getOwner())) {
                continue;
            }

            ResidencePlayer rPlayer = resPlugin.getPlayerManager().getResidencePlayer(player.getName(), player.getUniqueId());

            resPlugin.getResidenceManager().removeResidence(rPlayer, resName.getValue(), true, resPlugin.getConfigManager().isAutoCleanUpRegenerate());
            Bukkit.getLogger().info("Searching finished, " + purgedRes + " res can be purged and " + skipped + " will be skipped.");
            Bukkit.getLogger().info("Owner: " + player.getName() + " - Res: " + res.getName());

            purgedRes++;
        }
        Bukkit.getLogger().info("Clean finished, " + purgedRes + " res purged and " + skipped + " skipped.");
        return true;
    }

    static public boolean showResToBePurged() {
        Residence resPlugin = Residence.getInstance();
        ResidenceManager resManager = resPlugin.getResidenceManager();

        Map<String, ClaimedResidence> resNameList =
                new HashMap<>(resManager.getResidences());

        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();

        HashMap<UUID, OfflinePlayer> playerMapUuid = new HashMap<>(100);
        HashMap<String, OfflinePlayer> playerMapName = new HashMap<>(100);
        for (OfflinePlayer player : offlinePlayers) {
            playerMapUuid.put(player.getUniqueId(), player);
            playerMapName.put(player.getName(), player);
        }

        long curTime = System.currentTimeMillis();
        int purgeTime = RcConfig.purgeTimeInMins;

        Bukkit.getLogger().info("Start trying to search Residences from " + playerMapUuid.size() + " players and " + resNameList.size() + " res");

        int skipped = 0;
        int purgedRes = 0;
        for (Map.Entry<String, ClaimedResidence> resName : resNameList.entrySet()) {
            ClaimedResidence res = resName.getValue();
            if (res == null) {
                continue;
            }

            OfflinePlayer player = playerMapUuid.get(res.getOwnerUUID());

            if (player == null) {
                player = playerMapName.get(res.getOwner());
            }

            if (player == null) {
                skipped++;
                continue;
            }

            if (res.getMainArea().getXSize() > RcConfig.maxResSize || res.getMainArea().getZSize() > RcConfig.maxResSize) {
                skipped++;
                continue;
            }

            if (ResCleaner.vaultEco.getBalance(player) > RcConfig.ownerMoney) {
                skipped++;
                continue;
            }

            long lastPlayed = player.getLastPlayed();
            int offlineTime = (int) ((curTime - lastPlayed) / 1000 / 60);
            if (offlineTime < purgeTime) {
                skipped++;
                continue;
            }

            //TODO: add perm check here

            if ("server land".equalsIgnoreCase(res.getOwner()) || "server_land".equalsIgnoreCase(res.getOwner())) {
                continue;
            }

            Bukkit.getLogger().info("Owner: " + player.getName() + " - Res: " + res.getName());
            purgedRes++;
        }
        Bukkit.getLogger().info("Searching finished, " + purgedRes + " res can be purged and " + skipped + " will be skipped.");
        return true;
    }

    public static boolean reloadCfg() {
        ResCleaner resCleaner = ResCleaner.getThisPlugin();
        resCleaner.reloadConfig();
        RcConfig.loadConfigFromFile(resCleaner.getConfig());
        return true;
    }
}