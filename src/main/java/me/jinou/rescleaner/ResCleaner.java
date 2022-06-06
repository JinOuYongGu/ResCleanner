package me.jinou.rescleaner;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ResCleaner extends JavaPlugin {
    @Getter
    public static ResCleaner thisPlugin;

    public static Economy vaultEco;
    public static Permission vaultPerm;

    @Override
    public void onEnable() {
        boolean hasResidence = getServer().getPluginManager().isPluginEnabled("Residence");
        if (!hasResidence) {
            getLogger().severe("Disabled ResCleaner due to no Residence found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupVaultEco() || !setupVaultPerm()) {
            getLogger().severe("Disabled due to no Vault setup error!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        thisPlugin = this;

        saveDefaultConfig();
        RcConfig.loadConfigFromFile(getConfig());
        Objects.requireNonNull(Bukkit.getPluginCommand("ResCleaner")).setExecutor(new RcCmd());
    }

    private boolean setupVaultEco() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        vaultEco = rsp.getProvider();
        return true;
    }

    private boolean setupVaultPerm() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        vaultPerm = rsp.getProvider();
        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
