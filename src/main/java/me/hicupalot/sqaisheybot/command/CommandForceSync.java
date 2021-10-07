package me.hicupalot.sqaisheybot.command;

import me.hicupalot.sqaisheybot.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommandForceSync extends DiscordCommand {

    public CommandForceSync(Main main) {

        super(main, "forcesync", Permission.ADMINISTRATOR);

    }

    @Override
    public void onCommand(SlashCommandEvent event) {

        if (main.getSQLHandler().getLatestBan() != null) {
            event.reply("This command can no longer be used!").queue();
        }

        JDA jda = main.getJda();
        List<User> bans = new ArrayList<>();

        jda.getGuildById(main.getConfig().getQuacktopiaDiscord()).retrieveBanList().queue(bansList -> {
            bansList.forEach(b -> {
                bans.add(b.getUser());
            });
        });

        for (User user : bans) {
            jda.getGuildById(main.getConfig().getSqaisheyDiscord()).ban(user, 0).queue();
            main.getSQLHandler().insertBan(user.getId(), new Timestamp(System.currentTimeMillis()));
        }

        event.reply("Successfully banned "+ bans.size() + " users").setEphemeral(true).queue();
        jda.getTextChannelById(main.getConfig().getSqaisheyDiscordLog()).sendMessage("Successfully synced! "+ bans.size() + " users were banned").queue();

    }

    @Override
    public CommandData buildCommand() {
        return new CommandData("forcesync", "Force sync bans");
    }

}
