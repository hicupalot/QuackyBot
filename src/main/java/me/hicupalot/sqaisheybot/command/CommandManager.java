package me.hicupalot.sqaisheybot.command;

import me.hicupalot.sqaisheybot.Main;
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

        commands.add(new CommandInitialSync(main));
        commands.add(new ChannelBan(main));
        commands.add(new ChannelUnban(main));
        commands.add(new checkban(main));

        CommandListUpdateAction quacktopiaCommandUpdateAction = main.getJda().getGuildById(main.getConfig().getQuacktopiaDiscord()).updateCommands();
        CommandListUpdateAction sqaisheyCommandUpdateAction = main.getJda().getGuildById(main.getConfig().getSqaisheyDiscord()).updateCommands();
        for (DiscordCommand discordCommand : commands) {
            quacktopiaCommandUpdateAction.addCommands(discordCommand.buildCommand());
            sqaisheyCommandUpdateAction.addCommands(discordCommand.buildCommand());
        }
        quacktopiaCommandUpdateAction.queue();
        sqaisheyCommandUpdateAction.queue();

    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        if (event.getGuild() == null || (!event.getGuild().getId().equals(main.getConfig().getQuacktopiaDiscord()) && !event.getGuild().getId().equals(main.getConfig().getSqaisheyDiscord()))) {
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
