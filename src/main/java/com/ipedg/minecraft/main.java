package com.ipedg.minecraft;

import com.ipedg.minecraft.entity.ModData;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class main extends JavaPlugin implements Listener {
    private List<ModData> modDataList = new ArrayList<>();
    private Boolean OpenSystem = false;
    private String PlayerMsg;
    @Override
    public void onEnable() {
        System.out.print("【GetMods启动】QQ44920040");
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(),"config.yml");
        if (!(file.exists())){
            saveDefaultConfig();
        }
        loadConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        super.onEnable();
    }

    public void loadConfig(){
        //saveConfig();
        reloadConfig();
        List<String> main = getConfig().getStringList("ModList");
        for (String tmp:main){
            String[] split = tmp.split("\\|");
            System.out.print(split[0]+"|"+split[1]);
            modDataList.add(new ModData(split[0],split[1]));
        }
        OpenSystem = getConfig().getBoolean("OpenSystem");
        PlayerMsg = getConfig().getString("Msg");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player&&sender.isOp()&&args.length==1){
            if (args[0].equalsIgnoreCase("save")){
                CraftPlayer player = (CraftPlayer) sender;
                EntityPlayerMP handle = player.getHandle();
                modDataList.clear();
                modDataList = getPlayerMods(handle);
                List<String> modlist = new ArrayList<>();
                for (ModData d:modDataList){
                    player.sendMessage(d.getName()+"|"+d.getVersion());
                    modlist.add(d.getName()+"|"+d.getVersion());
                }
                getConfig().set("ModList",modlist);
                try {
                    getConfig().save(new File(getDataFolder(),"config.yml"));
                    sender.sendMessage("保存成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (args[0].equalsIgnoreCase("reload")){
                loadConfig();
                sender.sendMessage("重载成功");
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    @EventHandler
    public void PlayerJoinGame(PlayerJoinEvent event){
        CraftPlayer player = (CraftPlayer)event.getPlayer();
        EntityPlayerMP handle = player.getHandle();
        List<ModData> playerMods = getPlayerMods(handle);
        for (ModData mod:playerMods){
            if (OpenSystem){
                //System.out.print(mod.getName()+","+mod.getVersion());
                if (!modDataList.contains(mod)){
                    player.kickPlayer(PlayerMsg);
                }
            }

        }
    }

    public static List<ModData> getPlayerMods(EntityPlayerMP player){
        List<ModData> data = new ArrayList<>();
        NetworkManager nm = null;
        NetworkDispatcher np = null;
        NetHandlerPlayServer connection = player.field_71135_a;
        nm = connection.field_147371_a;
        np = NetworkDispatcher.get(nm);
        Map<String, String> modList = np.getModList();
        for(Map.Entry<String, String> mod : modList.entrySet()){
            data.add(new ModData(mod.getKey(),mod.getValue()));
        }
        return data;
    }
}