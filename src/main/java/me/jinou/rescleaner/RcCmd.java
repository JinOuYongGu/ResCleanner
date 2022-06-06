package me.jinou.rescleaner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RcCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return false;
        }

        if (args.length == 1 && "show".equalsIgnoreCase(args[0])) {
            if (RcUtil.showResToBePurged()) {
                sender.sendMessage("ResCleaner: residences have been listed in console, see console for more info");
            }
        }

        if (args.length == 1 && "clean".equalsIgnoreCase(args[0])) {
            if (RcUtil.purgeRes()) {
                sender.sendMessage("ResCleaner: residence cleaned, see console for more info");
                return true;
            } else {
                return false;
            }
        }

        if (args.length == 1 && "reload".equalsIgnoreCase(args[0])) {
            if (RcUtil.reloadCfg()) {
                sender.sendMessage("ResCleaner: Plugin reloaded");
                return true;
            } else {
                return false;
            }
        }

        sender.sendMessage("Available subcommands: reload, show, clean");
        sender.sendMessage("/resc reload : reload params from config.yml");
        sender.sendMessage("/resc show : shows which res will be deleted");
        sender.sendMessage("/resc clean: [BE CAREFUL] remove res that meet the params in config");
        return true;
    }
}
