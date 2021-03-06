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

public class ChannelUnban extends DiscordCommand {
    public ChannelUnban(Main main) {
        super(main, "channelunban", Permission.BAN_MEMBERS);

    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        JDA jda = main.getJda();
        User moderator = event.getUser(); //User Doing the Unbanning
        Guild guild = event.getGuild(); //Guild it Occured in
        Member user = event.getOption("user").getAsMember(); //User being Unbanned
        String user1 = event.getOption("user").getAsUser().getId(); //User Being Unbanned
        User user2 = event.getOption("user").getAsUser(); //User as user
        String channel = event.getOption("channel").getAsGuildChannel().getId(); //Channel ID
        assert user != null;
        assert guild != null;
        //-------------------------------------------------------------------------------------------//
        if (!event.getOption("channel").getChannelType().isMessage()) {
            event.reply("You must choose a TEXT CHANNEL").setEphemeral(true).queue();
            return;
        }
        if (guild.getId().equals(main.getConfig().getQuacktopiaDiscord())){
         if (user.getRoles().stream().filter(role -> role.getName().equalsIgnoreCase("Helper")).findAny().orElse(null) != null || user.isOwner()
                || user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("disc-admin")).findAny().orElse(null) != null || user.hasPermission(Permission.ADMINISTRATOR) || user2.isBot()) {
            event.reply("You can't check this user!").setEphemeral(true).queue();
        }
        } else if (guild.getId().equals(main.getConfig().getSqaisheyDiscord())) {
            if (user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("\uD83D\uDC23 | Helper")).findAny().orElse(null) != null || user.isOwner() || user.hasPermission(Permission.ADMINISTRATOR) || user2.isBot()) {
                event.reply("You can't check this user!").setEphemeral(true).queue();
                return;
            }
        }
        //-------------------------------------------------------------------------------------------//

        if (jda.getTextChannelById(channel).getMembers().contains(user)) {
            event.reply("They are already unbanned!").setEphemeral(true).queue();
            return;
        }
        if (jda.getTextChannelById(channel).getPermissionOverrides().isEmpty() && !jda.getTextChannelById(channel).getPermissionOverrides().contains(user)){
            event.reply("They aren't channel banned they just don't have access to this channel!").setEphemeral(true).queue();
            return;
        }
        if (jda.getTextChannelById(channel).getMembers().contains(user)){
            event.reply("They are already unbanned!").setEphemeral(true).queue();
            return;
        }
        jda.getTextChannelById(channel).getPermissionOverride(user).delete().queue();
        event.reply("You successfully unbanned " + user.getAsMention() + " from " + event.getOption("channel").getAsGuildChannel().getAsMention() + " for " + event.getOption("reason").getAsString()).setEphemeral(true).queue();
        if (guild.getId().equals(main.getConfig().getQuacktopiaDiscord())) {
            jda.getTextChannelById(main.getConfig().getQuacktopiaDiscordLog()).sendMessage(moderator.getAsMention() + " unbanned " + user.getAsMention() + " from " + event.getOption("channel").getAsGuildChannel().getAsMention() + " for " + event.getOption("reason").getAsString()).queue();
        }
        else if (guild.getId().equals(main.getConfig().getSqaisheyDiscord())) {
            jda.getTextChannelById(main.getConfig().getQuacktopiaDiscordLog()).sendMessage(moderator.getAsMention() + " unbanned " + user.getAsMention() + " from " + event.getOption("channel").getAsGuildChannel().getAsMention() + " for " + event.getOption("reason").getAsString()).queue();
        }
    }
    @Override
    public CommandData buildCommand() {
        return new CommandData("channelunban", "Unban Someone From a Specific Channel").addOption(OptionType.USER,"user","Who do you want to unban",true).addOption(OptionType.CHANNEL,"channel","The Channel You Wish To UnBan From",true)
                .addOption(OptionType.STRING,"reason","Reason for UnBan",true);
    }
}