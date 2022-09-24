package xyz.raitaki.AkumaKOTH.Commands.Completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import xyz.raitaki.AkumaKOTH.Commands.ArenaCommand;
import xyz.raitaki.AkumaKOTH.Objects.KOTHArena;
import xyz.raitaki.AkumaKOTH.Utils.Methods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArenaCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if(args.length == 1){
            commands.add("create");
            commands.add("remove");
            commands.add("list");
            commands.add("setcooldown");
            commands.add("setcapturetime");
            commands.add("setname");
            commands.add("setlocation");
            commands.add("pos1");
            commands.add("pos2");
            commands.add("editloot");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if(args.length == 2){
            if(!args[0].equalsIgnoreCase("list"))
                commands.addAll(KOTHArena.getArenas().keySet());

            if(args[0].equalsIgnoreCase("create"))
                commands.clear();

            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        if(args.length == 3){
            if(args[0].equalsIgnoreCase("setcooldown")){
                commands.add("<cooldown>");
            }
            else if(args[0].equalsIgnoreCase("setcapturetime")){
                commands.add("<capturetime>");
            }
            else if(args[0].equalsIgnoreCase("setname")){
                commands.add("<new name>");
            }
            else if(args[0].equalsIgnoreCase("setlocation")){
                commands.add("It will be set to your location");
            }
            StringUtil.copyPartialMatches(args[2], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
