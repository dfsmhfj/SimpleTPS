package com.dfsmhfj.simpletps;

import com.dfsmhfj.simpletps.commands.PingCommand;
import com.dfsmhfj.simpletps.commands.TPSCommand;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent;

@Mod("simpletps")
public class SimpleTPS {

    public SimpleTPS(IEventBus modEventBus) {
        // 注册 TPS 计算器到游戏事件总线
        NeoForge.EVENT_BUS.register(new TPSCalculator());
        // 注册命令监听
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        TPSCommand.register(event.getDispatcher());
        PingCommand.register(event.getDispatcher());
    }
}