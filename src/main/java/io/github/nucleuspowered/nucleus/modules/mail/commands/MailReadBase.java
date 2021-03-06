/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.mail.commands;

import io.github.nucleuspowered.nucleus.NameUtil;
import io.github.nucleuspowered.nucleus.Util;
import io.github.nucleuspowered.nucleus.api.data.mail.MailData;
import io.github.nucleuspowered.nucleus.api.data.mail.MailFilter;
import io.github.nucleuspowered.nucleus.modules.mail.handlers.MailHandler;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MailReadBase {

    private final MailHandler handler;
    private final Game game;
    static final String filters = "filters";

    MailReadBase(Game game, MailHandler handler) {
        this.game = game;
        this.handler = handler;
    }

    public CommandResult executeCommand(CommandSource src, final User target, Collection<MailFilter> lmf) {
        List<MailData> lmd;
        if (!lmf.isEmpty()) {
            lmd = handler.getMail(target, lmf.toArray(new MailFilter[lmf.size()]));
        } else {
            lmd = handler.getMail(target);
        }

        if (lmd.isEmpty()) {
            if (src instanceof Player && target.getUniqueId().equals(((Player) src).getUniqueId())) {
                src.sendMessage(Util.getTextMessageWithFormat(!lmf.isEmpty() ? "command.mail.none.filter" : "command.mail.none.normal.self"));
            } else {
                src.sendMessage(Util.getTextMessageWithFormat(!lmf.isEmpty() ? "command.mail.none.filter" : "command.mail.none.normal.other", target.getName()));
            }

            return CommandResult.success();
        }

        List<Text> mails = lmd.stream().sorted((a, b) -> a.getDate().compareTo(b.getDate())).map(x -> createMessage(x, target)).collect(Collectors.toList());

        // Paginate the mail.
        PaginationService ps = game.getServiceManager().provideUnchecked(PaginationService.class);
        PaginationList.Builder b = ps.builder().padding(Text.of(TextColors.GREEN, "-")).title(getHeader(src, target, !lmf.isEmpty())).contents(mails);
        if (!(src instanceof Player)) {
            b.linesPerPage(-1);
        } else {
            b.header(Util.getTextMessageWithFormat("mail.header"));
        }

        b.sendTo(src);
        return CommandResult.success();
    }

    private Text getHeader(CommandSource src, User user, boolean isFiltered) {
        if (src instanceof Player && user.getUniqueId().equals(((Player) src).getUniqueId())) {
            return Util.getTextMessageWithFormat(isFiltered ? "mail.title.filter.self" : "mail.title.nofilter.self");
        }

        return Util.getTextMessageWithFormat(isFiltered ? "mail.title.filter.other" : "mail.title.nofilter.other", user.getName());
    }

    private Text createMessage(final MailData md, final User user) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy").withZone(ZoneId.systemDefault());
        String name = NameUtil.getNameFromUUID(md.getUuid());
        return Text.builder()
                .append(Text.builder(name).color(TextColors.GREEN).style(TextStyles.UNDERLINE)
                        .onHover(TextActions.showText(Util.getTextMessageWithFormat("command.mail.hover")))
                        .onClick(TextActions.executeCallback(src -> {
                            src.sendMessage(Text.builder().append(Util.getTextMessageWithFormat("command.mail.date"))
                                    .append(Text.of(" ", TextColors.WHITE, dtf.format(md.getDate()))).build());
                            Text.Builder tb = Text.builder().append(Util.getTextMessageWithFormat("command.mail.sender"))
                                    .append(Text.of(" ", TextColors.WHITE, NameUtil.getNameFromUUID(md.getUuid())))
                                    .append(Text.of(TextColors.YELLOW, " - "));

                            // If the sender is not the server, allow right of reply.
                            if (!md.getUuid().equals(Util.consoleFakeUUID)) {
                                tb.append(Text.builder(Util.getMessageWithFormat("standard.reply")).color(TextColors.GREEN)
                                        .onHover(TextActions.showText(Util.getTextMessageWithFormat("command.mail.reply.label", name)))
                                        .onClick(TextActions.suggestCommand("/mail send " + name + " ")).build())
                                        .append(Text.of(TextColors.YELLOW, " - "));
                            }

                            src.sendMessage(tb.append(Text.builder(Util.getMessageWithFormat("standard.delete")).color(TextColors.RED)
                                .onHover(TextActions.showText(Util.getTextMessageWithFormat("command.mail.delete.label")))
                                .onClick(TextActions.executeCallback(s -> {
                                    if (handler.removeMail(user, md)) {
                                        src.sendMessage(Util.getTextMessageWithFormat("command.mail.delete.success"));
                                    } else {
                                        src.sendMessage(Util.getTextMessageWithFormat("command.mail.delete.fail"));
                                    }
                                })).build()).build());
                            src.sendMessage(Util.getTextMessageWithFormat("command.mail.message"));
                            src.sendMessage(Text.of(TextColors.WHITE, md.getMessage()));
                        })).build())
                .append(Text.of(": " + md.getMessage())).build();
    }
}
