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
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {

    private static Main main;
    private final JDA jda;
    private Config config;
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

        this.config = Config.loadConfig("config.json", Config.class);

        this.jda = JDABuilder.createDefault(config.getToken()).setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("https://www.youtube.com/sqaishey"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_WEBHOOKS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.ACTIVITY)
                .setChunkingFilter(ChunkingFilter.ALL)
                .build().awaitReady();

        this.sqlHandler = new SQLHandler(
                config.getSqlUsername(),
                config.getSqlPassword(),
                config.getSqlDatabase(),
                config.getSqlPort()
        );

        this.commandManager = new CommandManager(this);

        jda.addEventListener(commandManager);
        jda.addEventListener(new BanSync(this, jda));
        jda.addEventListener(new UnBanSync(this, jda));

    }

    public Config getConfig() {
        return config;
    }

    public JDA getJda () {
        return jda;
    }

    public SQLHandler getSQLHandler() {
        return sqlHandler;
    }

}


