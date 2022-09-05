package me.anemoi.rendertools;

import me.anemoi.rendertools.command.ExampleCommand;
import me.anemoi.rendertools.config.MainConfig;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import me.anemoi.rendertools.modules.ChinaHat;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

@net.minecraftforge.fml.common.Mod(modid = RenderTools.MODID, name = RenderTools.NAME, version = RenderTools.VERSION)
public class RenderTools {
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    @net.minecraftforge.fml.common.Mod.Instance(MODID)
    public static RenderTools INSTANCE;
    public static Minecraft mc = Minecraft.getMinecraft();
    public MainConfig config;

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void onFMLInitialization(net.minecraftforge.fml.common.event.FMLInitializationEvent event) {
        config = new MainConfig();
        CommandManager.INSTANCE.registerCommand(ExampleCommand.class);

        MinecraftForge.EVENT_BUS.register(new ChinaHat());
    }
}