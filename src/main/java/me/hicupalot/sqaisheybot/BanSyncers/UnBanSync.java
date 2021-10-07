package me.hicupalot.sqaisheybot.BanSyncers;

import me.hicupalot.sqaisheybot.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.CompletableFuture;

public class UnBanSync extends ListenerAdapter {

    private final Main main;
    private final JDA jda;

    public UnBanSync(Main main, JDA jda) {
        this.main = main;
        this.jda = jda;
    }

    public void onGuildUnban(GuildUnbanEvent event) {

        String userID = event.getUser().getId();
        String userMention = event.getUser().getAsMention();

        if (!main.getSQLHandler().isBanned(userID)) {
            return;
        }

        if (event.getGuild().getId().equals(main.getConfig().getQuacktopiaDiscord())) {

            jda.getGuildById(main.getConfig().getSqaisheyDiscord()).unban(userID).queue();
            jda.getTextChannelById(main.getConfig().getSqaisheyDiscordLog()).sendMessage(userMention + " was unbanned in Quacktopia so has been unbanned here").queue();

        }

        else if (event.getGuild().getId().equals(main.getConfig().getSqaisheyDiscord())){

            jda.getGuildById(main.getConfig().getQuacktopiaDiscord()).unban(userID).queue();
            jda.getTextChannelById(main.getConfig().getQuacktopiaDiscordLog()).sendMessage(userMention+ " was unbanned in Sqaishey's Discord so has been unbanned here").queue();

        }

        CompletableFuture.runAsync(() -> {
           main.getSQLHandler().deleteBan(userID);
        });

    }

}


