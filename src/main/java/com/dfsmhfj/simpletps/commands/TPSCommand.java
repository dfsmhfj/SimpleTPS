package com.dfsmhfj.simpletps.commands;

import com.dfsmhfj.simpletps.TPSCalculator;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.literal;

public class TPSCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("tps")
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    CommandSourceStack source = ctx.getSource();
                    double tps = TPSCalculator.getServerTPS();
                    double mspt = TPSCalculator.getServerMSPT();

                    // 根据 TPS 值动态决定颜色
                    ChatFormatting color;
                    if (tps >= 16.0) {
                        color = ChatFormatting.GREEN;
                    } else if (tps >= 12.0) {
                        color = ChatFormatting.YELLOW;
                    } else if (tps >= 8.0) {
                        color = ChatFormatting.GOLD;
                    } else {
                        color = ChatFormatting.RED;
                    }

                    source.sendSuccess(() -> Component.translatable(
                            "simpletps.tps.response",
                            String.format("%.2f", tps),
                            String.format("%.2f", mspt)
                    ).withStyle(color), false);
                    return 1;
                })
        );
    }
}