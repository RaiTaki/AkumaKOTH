package xyz.raitaki.AkumaKOTH.Commands;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import xyz.raitaki.AkumaKOTH.Objects.KOTHArena;
import xyz.raitaki.AkumaKOTH.Utils.LocationUtil;
import xyz.raitaki.AkumaKOTH.Utils.Methods;

public class ArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            sender.sendMessage("§cUsage: /arena <create, remove, list, pos1, pos2, setcooldown, setcapturetime, setname, setlocation, editloot>");
            return true;
        }
        if(!(sender instanceof Player p)){
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }
        else if(args[0].equalsIgnoreCase("create")){
            if(args.length == 1){
                sender.sendMessage("§cUsage: /arena create <name>");
                return true;
            }
            if(args.length == 2){
                if(KOTHArena.getArena(args[1]) != null){
                    sender.sendMessage("§cAn arena with this name already exists!");
                    return true;
                }
                KOTHArena.createDefaultArena(args[1]);
                sender.sendMessage("§aArena created!");
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("remove")){
            if(args.length == 1){
                sender.sendMessage("§cUsage: /arena remove <name>");
                return true;
            }
            if(args.length == 2){
                if(KOTHArena.getArena(args[1]) == null){
                    sender.sendMessage("§cAn arena with this name doesn't exist!");
                    return true;
                }
                KOTHArena.deleteArena(args[1]);
                sender.sendMessage("§aArena removed!");
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("list")){
            sender.sendMessage("§aArenas:");
            for(KOTHArena arena : KOTHArena.getArenas()){
                sender.sendMessage("§7- §a" + arena.getName());
            }
            return true;
        }
        else if(args[0].equalsIgnoreCase("setcooldown")){
            if(args.length == 1){
                sender.sendMessage("§cUsage: /arena setcooldown <name> <cooldown>");
                return true;
            }
            if(args.length == 2){
                sender.sendMessage("§cUsage: /arena setcooldown <name> <cooldown>");
                return true;
            }
            if(args.length == 3){
                if(KOTHArena.getArena(args[1]) == null){
                    sender.sendMessage("§cAn arena with this name doesn't exist!");
                    return true;
                }
                if(!Methods.isNumber(args[2])){
                    sender.sendMessage("§cUsage: /arena setcooldown <name> <cooldown>");
                    return true;
                }
                KOTHArena.getArena(args[1]).setCooldown(Integer.parseInt(args[2]));
                sender.sendMessage("§aCooldown set!");
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("setcapturetime")){
            if(args.length == 1){
                sender.sendMessage("§cUsage: /arena setcapturetime <name> <capturetime>");
                return true;
            }
            if(args.length == 2){
                sender.sendMessage("§cUsage: /arena setcapturetime <name> <capturetime>");
                return true;
            }
            if(args.length == 3){
                if(KOTHArena.getArena(args[1]) == null){
                    sender.sendMessage("§cAn arena with this name doesn't exist!");
                    return true;
                }
                if(!Methods.isNumber(args[2])){
                    sender.sendMessage("§cUsage: /arena setcapturetime <name> <capturetime>");
                    return true;
                }
                KOTHArena.getArena(args[1]).setCaptureTime(Integer.parseInt(args[2]));
                sender.sendMessage("§aCapture time set!");
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("setname")){
            if(args.length == 1){
                sender.sendMessage("§cUsage: /arena setname <name> <newname>");
                return true;
            }
            if(args.length == 2){
                sender.sendMessage("§cUsage: /arena setname <name> <newname>");
                return true;
            }
            if(args.length == 3){
                if(KOTHArena.getArena(args[1]) == null){
                    sender.sendMessage("§cAn arena with this name doesn't exist!");
                    return true;
                }
                if(KOTHArena.getArena(args[2]) != null){
                    sender.sendMessage("§cAn arena with this name already exists!");
                    return true;
                }
                KOTHArena.getArena(args[1]).setName(args[2]);
                sender.sendMessage("§aName set!");
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("setlocation")){
            if(args.length == 1){
                sender.sendMessage("§cUsage: /arena setlocation <name>");
                return true;
            }
            if(args.length == 2){
                if(KOTHArena.getArena(args[1]) == null){
                    sender.sendMessage("§cAn arena with this name doesn't exist!");
                    return true;
                }
                KOTHArena.getArena(args[1]).setLocation(p.getLocation());
                sender.sendMessage("§aLocation set!");
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("editloot")){
            if(args.length == 1){
                sender.sendMessage("§cUsage: /arena editloot <name>");
                return true;
            }
            if(args.length == 2){
                if(KOTHArena.getArena(args[1]) == null){
                    sender.sendMessage("§cAn arena with this name doesn't exist!");
                    return true;
                }
                Inventory inv = Bukkit.createInventory(null, 54, "§aEdit loot for arena " + args[1]);
                for(int i = 0; i < KOTHArena.getArena(args[1]).getItems().size(); i++){
                    inv.setItem(i, KOTHArena.getArena(args[1]).getItems().get(i));
                }
                p.openInventory(inv);
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("pos1")){
            if(args.length == 1){
                sender.sendMessage("§cUsage: /arena pos1 <name>");
                return true;
            }
            if(args.length == 2){
                if(KOTHArena.getArena(args[1]) == null){
                    sender.sendMessage("§cAn arena with this name doesn't exist!");
                    return true;
                }
                Block block = LocationUtil.getTargetBlock(p, 7);
                if(block == null){
                    sender.sendMessage("§cYou have to look at a block!");
                    return true;
                }
                KOTHArena.getArena(args[1]).setPos1(block.getLocation().add(0.5, 1, 0.5));
                sender.sendMessage("§aPos1 set!");
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("pos2")){
            if(args.length == 1){
                sender.sendMessage("§cUsage: /arena pos2 <name>");
                return true;
            }
            if(args.length == 2){
                if(KOTHArena.getArena(args[1]) == null){
                    sender.sendMessage("§cAn arena with this name doesn't exist!");
                    return true;
                }
                Block block = LocationUtil.getTargetBlock(p, 7);
                if(block == null){
                    sender.sendMessage("§cYou have to look at a block!");
                    return true;
                }
                KOTHArena.getArena(args[1]).setPos2(block.getLocation().add(0.5, 1, 0.5));
                sender.sendMessage("§aPos2 set!");
                return true;
            }
        }
        else{
            sender.sendMessage("§cUsage: /arena <create, remove, list, pos1, pos2, setcooldown, setcapturetime, setname, setlocation, editloot>");
            return true;
        }

        return false;
    }
}
