package me.hicupalot.sqaisheybot;

import me.hicupalot.sqaisheybot.BanSyncers.BanSync;
import me.hicupalot.sqaisheybot.BanSyncers.UnBanSync;
import me.hicupalot.sqaisheybot.command.CommandManager;
import me.hicupalot.sqaisheybot.configs.Config;
import me.hicupalot.sqaisheybot.data.SQLHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {

    private static Main main;
    private final JDA jda;
    private CommandManager commandManager;
    private SQLHandler sqlHandler;

    public static void main(String[] args) {
        try {
            main = new Main();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(404);
        }
    }

    public Main() throws Exception {

        this.jda = JDABuilder.createDefault(Config.get("token")).setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("https://www.youtube.com/sqaishey"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                .setMemberCachePolicy(MemberCachePolicy.ALL).enableCache(CacheFlag.ACTIVITY).build().awaitReady();

        this.sqlHandler = new SQLHandler(Config.SQL_USERNAME, Config.SQL_PASSWORD, Config.SQL_DATABASE, Config.SQL_PORT);
        this.commandManager = new CommandManager(this);

        jda.addEventListener(commandManager);
        jda.addEventListener(new BanSync(jda));
        jda.addEventListener(new UnBanSync(jda));

    }

    public JDA getJda () {
        return jda;
    }

}


