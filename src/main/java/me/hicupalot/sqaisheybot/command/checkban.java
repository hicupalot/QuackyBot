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

public class checkban extends DiscordCommand {
    @SuppressWarnings("unused")
    public checkban(Main main) {
        super(main, "checkban", Permission.BAN_MEMBERS);

    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        JDA jda = main.getJda();
        User moderator = event.getUser(); //User Doing the Checking
        Guild guild = event.getGuild(); //Guild it Occured in
        Member user = event.getOption("user").getAsMember(); //User being Checked
        User user2 = event.getOption("user").getAsUser(); //User as user
        String user1 = event.getOption("user").getAsUser().getId(); //User Being Checked
        String channel = event.getOption("channel").getAsGuildChannel().getId(); //Channel ID
        assert user != null;
        assert guild != null;
        if (guild.getId().equals(main.getConfig().getQuacktopiaDiscord())) {
            if (user.getRoles().stream().filter(role -> role.getName().equalsIgnoreCase("Helper")).findAny().orElse(null) != null || user.isOwner()
                    || user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("disc-admin")).findAny().orElse(null) != null || user.hasPermission(Permission.ADMINISTRATOR) || user2.isBot()) {
                event.reply("You can't check a Moderator!").setEphemeral(true).queue();
            } else if (guild.getId().equals(main.getConfig().getSqaisheyDiscord())) {
                if (user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("\uD83D\uDC23 | Helper")).findAny().orElse(null) != null || user.isOwner() || user.hasPermission(Permission.ADMINISTRATOR) || user2.isBot()) {
                    event.reply("You can't check a Moderator!").setEphemeral(true).queue();
                    return;
                }
                if (jda.getTextChannelById(channel).getMembers().contains(user)) {
                    event.reply("This user isn't banned!").setEphemeral(true).queue();
                    return;
                }
                if (jda.getTextChannelById(channel).getPermissionOverrides().isEmpty() && !jda.getTextChannelById(channel).getPermissionOverrides().contains(user)) {
                    event.reply("They aren't channel banned they just don't have access to this channel!").setEphemeral(true).queue();
                    return;
                }
                if (jda.getTextChannelById(channel).getPermissionOverride(user).getDenied().contains(Permission.VIEW_CHANNEL)) {
                    event.reply(user.getAsMention() + " is banned from " + event.getOption("channel").getAsGuildChannel().getAsMention()).setEphemeral(true).queue();

                } else if (!jda.getTextChannelById(channel).getPermissionOverride(user).getDenied().contains(Permission.VIEW_CHANNEL)) {
                    event.reply(user.getAsMention() + " isn't banned from " + event.getOption("channel").getAsGuildChannel().getAsMention()).setEphemeral(true).queue();

                }
            }
        }
    }

    @Override
    public CommandData buildCommand() {
        return new CommandData("checkban", "Check if Someone is banned From a Specific Channel").addOption(OptionType.USER, "user", "Who do you want to check", true).addOption(OptionType.CHANNEL, "channel", "The Channel You Wish To Check", true);
    }
}