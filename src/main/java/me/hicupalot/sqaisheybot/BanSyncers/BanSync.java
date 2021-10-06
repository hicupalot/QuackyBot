package me.hicupalot.sqaisheybot.BanSyncers;

import me.hicupalot.sqaisheybot.configs.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BanSync extends ListenerAdapter {
    private final JDA jda;

    public BanSync(JDA jda) {
        this.jda = jda;
    }

    public void onBan(GuildBanEvent event) {
        String BannedPlayer = event.getUser().getId();
        String BannedPlayer1 = event.getUser().getAsMention();
        if (event.getGuild().getId().equals(Config.QUACKTOPIA)) {
            jda.getGuildById(Config.SQAISHEY_DISCORD).ban(BannedPlayer, 7);
            jda.getTextChannelById(Config.SQAISHEY_DISCORD_LOG).sendMessage(BannedPlayer1 + " was banned in Quacktopia so has been banned here").queue();
            return;
        }
        if (event.getGuild().getId().equals(Config.SQAISHEY_DISCORD)) {
            jda.getGuildById(Config.QUACKTOPIA).ban(BannedPlayer, 7);
            jda.getTextChannelById(Config.QT_DISCORD_LOG).sendMessage(BannedPlayer1 + " was banned in Sqaishey's Discord so has been banned here").queue();
        }
    }
}

