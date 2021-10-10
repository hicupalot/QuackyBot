package me.hicupalot.sqaisheybot.command;

import me.hicupalot.sqaisheybot.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class DiscordCommand {

    protected final Main main;
    private final String name;
    private final Permission permission;

    public DiscordCommand(Main main, String name, Permission permission) {

        this.main = main;
        this.name = name;
        this.permission = permission;
    }

    public abstract void onCommand(SlashCommandEvent event);
    public abstract CommandData buildCommand();

    public String getName() {
        return name;
    }

    public Permission getPermission() {
        return permission;
    }

}
