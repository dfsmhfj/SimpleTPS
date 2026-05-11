package com.dfsmhfj.simpletps.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.lang.reflect.Field;

import static net.minecraft.commands.Commands.literal;

public class PingCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("ping")
                .executes(ctx -> {
                    CommandSourceStack source = ctx.getSource();
                    ServerPlayer player = source.getPlayerOrException();
                    int ping;
                    try {
                        var latencyField = player.connection.getClass().getDeclaredField("latency");
                        latencyField.setAccessible(true);
                        ping = latencyField.getInt(player.connection);
                    } catch (Exception e) {
                        ping = -1;
                    }

                    // 根据延迟值动态决定颜色
                    ChatFormatting color;
                    if (ping < 0) {
                        color = ChatFormatting.RED;          // 获取失败
                    } else if (ping < 100) {
                        color = ChatFormatting.GREEN;
                    } else if (ping < 250) {
                        color = ChatFormatting.GOLD;
                    } else {
                        color = ChatFormatting.RED;
                    }

                    int finalPing = ping;
                    source.sendSuccess(() -> Component.translatable(
                            "simpletps.ping.response", finalPing
                    ).withStyle(color), false);
                    return 1;
                })
        );
    }
}