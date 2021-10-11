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
        String channel = event.getOption("channel").getAsGuildChannel().getId();
        assert user != null;
        assert guild !=null;
        //-------------------------------------------------------------------------------------------//
        if (guild.getId().equals(main.getConfig().getQuacktopiaDiscord())) {
            if (user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("298178020806492161")).findAny().orElse(null) == null || user.isOwner()
                    || user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("759818242726035466")).findAny().orElse(null) == null || user.hasPermission(Permission.ADMINISTRATOR)){
                event.reply("You can't unban a Moderator!").setEphemeral(true).queue();
            }
            else if (guild.getId().equals(main.getConfig().getSqaisheyDiscord())) {
                if (user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("894564336599711774")).findAny().orElse(null) == null || user.isOwner() || user.hasPermission(Permission.ADMINISTRATOR)){
                    event.reply("You can't unban a Moderator!").setEphemeral(true).queue();
                    return;
                 }

             }
         }
        //-------------------------------------------------------------------------------------------//
        jda.getTextChannelById(channel).getPermissionOverride(user).delete().queue();
        event.reply("You successfully unbanned" + user + " from " + channel + " for " + event.getOption("reason")).setEphemeral(true).queue();
        if (guild.getId().equals(main.getConfig().getQuacktopiaDiscord())) {
            jda.getTextChannelById(main.getConfig().getQuacktopiaDiscordLog()).sendMessage(moderator + " unbanned " + user + " from " + channel + " for " + event.getOption("reason")).queue();
        }
        else if (guild.getId().equals(main.getConfig().getSqaisheyDiscord())) {
            jda.getTextChannelById(main.getConfig().getSqaisheyDiscordLog()).sendMessage(moderator + " unbanned " + user + " from " + channel + " for " + event.getOption("reason")).queue();
        }
    }
    @Override
    public CommandData buildCommand() {
        return new CommandData("channelunban", "Unban Someone From a Specific Channel").addOption(OptionType.USER,"user","Who do you want to unban",true).addOption(OptionType.CHANNEL,"channel","The Channel You Wish To UnBan From",true)
                .addOption(OptionType.STRING,"reason","Reason for UnBan",false);
    }
}

