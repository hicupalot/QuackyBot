package me.hicupalot.sqaisheybot.command;

import me.hicupalot.sqaisheybot.Main;
import me.hicupalot.sqaisheybot.configs.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandForceSync extends DiscordCommand {

    public CommandForceSync(Main main) {

        super(main, "forcesync", Permission.ADMINISTRATOR);

    }

    @Override
    public void onCommand(SlashCommandEvent event) {

        JDA jda = main.getJda();
        List<User> bans = new ArrayList<>();

        jda.getGuildById(Config.QUACKTOPIA).retrieveBanList().queue(bansList -> {
            bansList.forEach(b -> {
                bans.add(b.getUser());
            });
        });
        Iterator<User> it = bans.iterator();
        jda.getGuildById(Config.SQAISHEY_DISCORD).ban(it.next(),7).queue();

        while(it.hasNext()) {
            jda.getGuildById(Config.SQAISHEY_DISCORD).ban(it.next(), 7).queue();
        }

        event.reply("Successfully banned "+ bans.toArray().length+ " users").setEphemeral(true).queue();
        jda.getTextChannelById(Config.SQAISHEY_DISCORD_LOG).sendMessage("Successfully synced! "+ bans.toArray().length+ " users were banned").queue();

    }

    @Override
    public CommandData buildCommand() {
        return new CommandData("forcesync", "Force sync bans");
    }

}
