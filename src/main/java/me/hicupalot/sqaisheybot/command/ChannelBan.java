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
        assert guild !=null;
        if (guild.getId().equals(main.getConfig().getQuacktopiaDiscord())) {
            if (user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("298178020806492161")).findAny().orElse(null) == null || user.isOwner()
            || user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("759818242726035466")).findAny().orElse(null) == null || user.hasPermission(Permission.ADMINISTRATOR)){
                event.reply("You can't ban a Moderator!").setEphemeral(true).queue();
            }
             else if (guild.getId().equals(main.getConfig().getSqaisheyDiscord())) {
                if (user.getRoles().stream().filter(role -> role.getId().equalsIgnoreCase("894564336599711774")).findAny().orElse(null) == null || user.isOwner() || user.hasPermission(Permission.ADMINISTRATOR)){
                    event.reply("You can't ban a Moderator!").setEphemeral(true).queue();
                    return;
                }
            }
        }
        if (Objects.requireNonNull(jda.getTextChannelById(channel).getPermissionOverride(user)).isMemberOverride()) {
            jda.getTextChannelById(channel).putPermissionOverride(user).setDeny(Permission.VIEW_CHANNEL).queue();
        } else {
            jda.getTextChannelById(channel).createPermissionOverride(user).setDeny(Permission.VIEW_CHANNEL).queue();
        }
        event.reply("You successfully banned" + user + " from " + channel + " for " + event.getOption("reason")).setEphemeral(true).queue();
        if (guild.getId().equals(main.getConfig().getQuacktopiaDiscord())) {
            jda.getTextChannelById(main.getConfig().getQuacktopiaDiscordLog()).sendMessage(moderator + " banned " + user + " from " + channel + " for " + event.getOption("reason")).queue();
        }
            else if (guild.getId().equals(main.getConfig().getSqaisheyDiscord())) {
            jda.getTextChannelById(main.getConfig().getSqaisheyDiscordLog()).sendMessage(moderator + " banned " + user + " from " + channel + " for " + event.getOption("reason")).queue();
        }
    }
    @Override
    public CommandData buildCommand() {
        return new CommandData("channelban", "Ban Someone From a Specific Channel").addOption(OptionType.USER,"user","Who do you want to ban",true).addOption(OptionType.CHANNEL,"channel","The Channel You Wish To Ban From",true)
                .addOption(OptionType.STRING,"reason","Reason for Ban",false);
    }
}


