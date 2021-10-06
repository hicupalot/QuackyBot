import BanSyncers.BanSync;
import BanSyncers.UnBanSync;
import configs.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static Main main;
    private final JDA jda;

    public static void main(String[] args) {
        try {
            main = new Main();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(404);
        }
    }

    @SuppressWarnings("unused")
    public Main() throws Exception {
        this.jda = JDABuilder.createDefault(Config.get("token")).setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("https://www.youtube.com/sqaishey"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                .setMemberCachePolicy(MemberCachePolicy.ALL).enableCache(CacheFlag.ACTIVITY).build().awaitReady();
        jda.getGuildById(Config.TESTING_SERVER).upsertCommand("forcesync","Force Sync Two Servers").queue();
        jda.addEventListener(new BanSync(jda));
        jda.addEventListener(new UnBanSync(jda));

    }
        public JDA getJda () {
            return jda;
        }

    public void forceSync(SlashCommandEvent event) {
       if (!event.getName().equals("forcesync")) {
           return;
       }
      if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) || !event.getMember().getId().equals("298483614260396033") ||
              !event.getMember().isOwner()){
               event.reply("You don't have permission to do this!").setEphemeral(true).queue();
                return;
     }
        List<User> bans = jda.getGuildById(Config.QUACKTOPIA).retrieveBanList().map(banList -> banList.stream(Collectors.toList())).queue(userlist -> {
                });
        Iterator<User> it = bans.iterator();
        jda.getGuildById(Config.SQAISHEY_DISCORD).ban(it.next(),7).queue();
        while(it.hasNext()) {
            jda.getGuildById(Config.SQAISHEY_DISCORD).ban(it.next(), 7).queue();
        }
        event.reply("Successfully banned "+ bans.toArray().length+ " users").setEphemeral(true).queue();
        jda.getTextChannelById(Config.SQAISHEY_DISCORD_LOG).sendMessage("Successfully synced! "+ bans.toArray().length+ " users were banned").queue();
        }
    }


