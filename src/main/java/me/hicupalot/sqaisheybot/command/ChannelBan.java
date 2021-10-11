package me.hicupalot.sqaisheybot.command;

import me.hicupalot.sqaisheybot.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Objects;

public class ChannelBan extends DiscordCommand {
    public ChannelBan(Main main) {
        super(main, "channelban", Permission.BAN_MEMBERS);

    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        JDA jda = main.getJda();
        User moderator = event.getUser(); //User Doing the Banning
        Guild guild = event.getGuild(); //Guild it Occured In
        Member user = event.getOption("user").getAsMember(); //User To Ban
        String user1 = event.getOption("user").getAsUser().getId(); //User To Ban
        String channel = event.getOption("channel").getAsGuildChannel().getId();
        assert user != null;
        assert guild != null;

        if (guild.getId().equals(main.getConfig().getQuacktopiaDiscord())) {
            if (user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("298178020806492161")).findAny().orElse(null) != null || user.isOwner()
                    || user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("759818242726035466")).findAny().orElse(null) != null || user.hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("You can't ban a Moderator!").setEphemeral(true).queue();
                return;
            } else if (guild.getId().equals(main.getConfig().getSqaisheyDiscord())) {
                if (user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("894564336599711774")).findAny().orElse(null) != null || user.isOwner() || user.hasPermission(Permission.ADMINISTRATOR)) {
                    event.reply("You can't ban a Moderator!").setEphemeral(true).queue();
                    return;
                }
            }
            if (jda.getTextChannelById(channel).getPermissionOverride(user).getDenied().contains(Permission.VIEW_CHANNEL)) {
                event.reply("They are already banned!").setEphemeral(true).queue();
                return;
            }
            if (jda.getTextChannelById(channel).getPermissionOverrides().contains(user)) {
                jda.getTextChannelById(channel).putPermissionOverride(user).setDeny(Permission.VIEW_CHANNEL).queue();
            } else {
                jda.getTextChannelById(channel).createPermissionOverride(user).setDeny(Permission.VIEW_CHANNEL).queue();
            }
            event.reply("You successfully banned " + user.getAsMention() + " from " + event.getOption("channel").getAsMessageChannel().getName() + " for " + event.getOption("reason").getAsString()).setEphemeral(true).queue();
            if (guild.getId().equals(main.getConfig().getQuacktopiaDiscord())) {
                jda.getTextChannelById(main.getConfig().getQuacktopiaDiscordLog()).sendMessage(moderator.getAsMention() + " unbanned " + user.getAsMention() + " from " + event.getOption("channel").getAsGuildChannel().getAsMention() + " for " + event.getOption("reason").getAsString()).queue();
            } else if (guild.getId().equals(main.getConfig().getSqaisheyDiscord())) {
                jda.getTextChannelById(main.getConfig().getQuacktopiaDiscordLog()).sendMessage(moderator.getAsMention() + " banned " + user.getAsMention() + " from " + event.getOption("channel").getAsGuildChannel().getAsMention() + " for " + event.getOption("reason").getAsString()).queue();
            }
        }
    }

    @Override
    public CommandData buildCommand() {
        return new CommandData("channelban", "Ban Someone From a Specific Channel").addOption(OptionType.USER,"user","Who do you want to ban",true).addOption(OptionType.CHANNEL,"channel","The Channel You Wish To Ban From",true)
                .addOption(OptionType.STRING,"reason","Reason for Ban",true);
    }
}
