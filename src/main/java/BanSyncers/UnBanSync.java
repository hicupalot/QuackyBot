package BanSyncers;

import configs.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UnBanSync extends ListenerAdapter {
    private final JDA jda;

    public UnBanSync(JDA jda) {
        this.jda = jda;
    }

    public void onBan(GuildBanEvent event){
        String BannedPlayer = event.getUser().getId();
        String BannedPlayer1 = event.getUser().getAsMention();
        if (event.getGuild().getId().equals(Config.QUACKTOPIA)) {
            jda.getGuildById(Config.SQAISHEY_DISCORD).unban(BannedPlayer).queue();
            jda.getTextChannelById(Config.SQAISHEY_DISCORD_LOG).sendMessage(BannedPlayer1+" was unbanned in Quacktopia so has been banned here").queue();
            return;
        }
        if (event.getGuild().getId().equals(Config.SQAISHEY_DISCORD)){
            jda.getGuildById(Config.QUACKTOPIA).unban(BannedPlayer).queue();
            jda.getTextChannelById(Config.QT_DISCORD_LOG).sendMessage(BannedPlayer1+" was unbanned in Sqaishey's Discord so has been banned here").queue();
        }
    }
}


