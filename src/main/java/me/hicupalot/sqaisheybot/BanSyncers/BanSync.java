package me.hicupalot.sqaisheybot.BanSyncers;

import me.hicupalot.sqaisheybot.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

public class BanSync extends ListenerAdapter {

    private final Main main;
    private final JDA jda;

    public BanSync(Main main, JDA jda) {
        this.main = main;
        this.jda = jda;
    }

    public void onGuildBan(GuildBanEvent event) {

        String userID = event.getUser().getId();
        String userMention = event.getUser().getAsMention();

        if (main.getSQLHandler().isBanned(userID)) { //fuck it its 11pm, sql on the main thread
            return;
        }

        if (event.getGuild().getId().equals(main.getConfig().getQuacktopiaDiscord())) {
            jda.getGuildById(main.getConfig().getSqaisheyDiscord()).ban(userID, 0).queue();
            jda.getTextChannelById(main.getConfig().getSqaisheyDiscordLog()).sendMessage(userMention + " was banned in Quacktopia so has been banned here").queue();
        }

        else if (event.getGuild().getId().equals(main.getConfig().getSqaisheyDiscord())) {
            jda.getGuildById(main.getConfig().getQuacktopiaDiscord()).ban(userID, 0).queue();
            jda.getTextChannelById(main.getConfig().getQuacktopiaDiscordLog()).sendMessage(userMention + " was banned in Sqaishey's Discord so has been banned here").queue();
        }

        CompletableFuture.runAsync(() -> {
            if (main.getSQLHandler().isBanned(userID)) {
                return;
            }
            main.getSQLHandler().insertBan(userID, new Timestamp(System.currentTimeMillis()));
        });

    }

}

