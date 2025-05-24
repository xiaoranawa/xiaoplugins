package org.plugins.xiaoplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class Xiaoplugin extends JavaPlugin implements Listener {

    private final String[] STARTUP_LOGO = {
            " __    __  ______   ______    ______   _______   __        __    __   ______   ______  __    __ ",
            "/  |  /  |/      | /      \\  /      \\ /       \\ /  |      /  |  /  | /      \\ /      |/  \\  /  |",
            "$$ |  $$ |$$$$$$/ /$$$$$$  |/$$$$$$  |$$$$$$$  |$$ |      $$ |  $$ |/$$$$$$  |$$$$$$/ $$  \\ $$ |",
            "$$  \\/$$/   $$ |  $$ |__$$ |$$ |  $$ |$$ |__$$ |$$ |      $$ |  $$ |$$ | _$$/   $$ |  $$$  \\$$ |",
            " $$  $$<    $$ |  $$    $$ |$$ |  $$ |$$    $$/ $$ |      $$ |  $$ |$$ |/    |  $$ |  $$$$  $$ |",
            "  $$$$  \\   $$ |  $$$$$$$$ |$$ |  $$ |$$$$$$$/  $$ |      $$ |  $$ |$$ |$$$$ |  $$ |  $$ $$ $$ |",
            " $$ /$$  | _$$ |_ $$ |  $$ |$$ \\__$$ |$$ |      $$ |_____ $$ \\__$$ |$$ \\__$$ | _$$ |_ $$ |$$$$ |",
            "$$ |  $$ |/ $$   |$$ |  $$ |$$    $$/ $$ |      $$       |$$    $$/ $$    $$/ / $$   |$$ | $$$ |",
            "$$/   $$/ $$$$$$/ $$/   $$/  $$$$$$/  $$/       $$$$$$$$/  $$$$$$/   $$$$$$/  $$$$$$/ $$/   $$/",
            "插件制作by霄染",
            "这是一个小功能补丁插件！"
    };

    @Override
    public void onEnable() {
        //TITLE
        Arrays.stream(STARTUP_LOGO).forEach(line -> getLogger().info(line));

        //生成/加载配置文件
        saveDefaultConfig();
        reloadConfig();

        //注册事件命令
        getServer().getPluginManager().registerEvents(this, this);
        setupCommands();
    }

    private void setupCommands() {
        //注册命令执行器
        getCommand("xiaoplugin").setExecutor(this);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().split(" ")[0].toLowerCase().substring(1);

        List<String> bannedCommands = getConfig().getStringList("bancommandlist");
        String bypassPermission = getConfig().getString("opbypassPermissions");

        if (bannedCommands.contains(command) && !player.hasPermission(bypassPermission)) {
            event.setCancelled(true);
            sendDenyMessage(player, command);
        }
    }

    private void sendDenyMessage(Player player, String command) {
        String prefix = getConfigString("messages.prefix");
        String message = player.hasPermission("xiaoplugin.seemessage") ?
                getConfigString("messages.command-denied").replace("%command%", command) :
                getConfigString("messages.permission-denied");

        player.sendMessage(prefix + message);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("xiaoplugin")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("xiaoplugin.reload")) {
                    sender.sendMessage(getConfigString("messages.prefix") + getConfigString("messages.permission-denied"));
                    return true;
                }

                reloadConfig();
                sender.sendMessage(getConfigString("messages.prefix") + getConfigString("messages.reload-success"));
                return true;
            }
        }
        return false;
    }

    private String getConfigString(String path) {
        return ChatColor.translateAlternateColorCodes('&',
                getConfig().getString(path, ""));
    }

    @Override
    public void onDisable() {
        getLogger().info("哇，感谢使用！插件已安全卸载");
    }
}
