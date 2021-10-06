package me.hicupalot.sqaisheybot.command;

import me.hicupalot.sqaisheybot.Main;
import me.hicupalot.sqaisheybot.configs.Config;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.ArrayList;

public class CommandManager extends ListenerAdapter {

    private final Main main;
    private final ArrayList<DiscordCommand> commands;

    public CommandManager(Main main) {

        this.main = main;
        this.commands = new ArrayList<>();
        registerCommands();

    }

    private void registerCommands() {

        commands.add(new CommandForceSync(main));

        CommandListUpdateAction quacktopiaCommandUpdateAction = main.getJda().getGuildById(Config.QUACKTOPIA).updateCommands();
        CommandListUpdateAction sqaisheyCommandUpdateAction = main.getJda().getGuildById(Config.SQAISHEY_DISCORD).updateCommands();
        for (DiscordCommand discordCommand : commands) {
            quacktopiaCommandUpdateAction.addCommands(discordCommand.buildCommand());
            sqaisheyCommandUpdateAction.addCommands(discordCommand.buildCommand());
        }
        quacktopiaCommandUpdateAction.queue();
        sqaisheyCommandUpdateAction.queue();

    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        if (event.getGuild() == null || (!event.getGuild().getId().equals(Config.QUACKTOPIA) && !event.getGuild().getId().equals(Config.SQAISHEY_DISCORD))) {
            return;
        }

        DiscordCommand discordCommand = null;

        for (DiscordCommand d : commands) {
            if (d.getName().equalsIgnoreCase(event.getName())) {
                discordCommand = d;
            }
        }

        if (discordCommand == null || event.getMember() == null) {
            return;
        }


        if (discordCommand.getPermission() != null && !event.getMember().hasPermission(discordCommand.getPermission())) {
            event.reply("Invalid permissions!").setEphemeral(true).queue();
            return;
        }

        discordCommand.onCommand(event);

    }

}
