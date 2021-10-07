package tax.cute.mcinfoplugin;

import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import top.mrxiaom.miraiutils.CommandListener;

public class Plugin extends JavaPlugin {
    MCSkin mcSkin;
    MCInfo mcInfo;
    MCSkinFile mcSkinFile;

    public Plugin() {
        super(new JvmPluginDescriptionBuilder(
                        "tax.cute.mcinfoplugin", // id
                        "1.1.0" // version
                )
                        .name("MCInfoPlugin")
                        .author("CuteStar")
                        // .info("...")
                        .build()
        );
    }

    @Override
    public void onLoad(PluginComponentStorage pcs) {
        getLogger().info("MCInfoPluginLoading");
        getLogger().info("Author: CuteStar");
        getLogger().info("Version: 1.1");
        getLogger().info("Github: https://github.com/MX233/MCInfoPlugin");
    }

    private void register() {
        mcSkin = new MCSkin(this);
        mcInfo = new MCInfo(this);
        mcSkinFile = new MCSkinFile(this);
    }

    @Override
    public void onEnable() {
        register();
        CommandListener listener = new CommandListener("/");
        listener.registerCommand(mcSkin);
        listener.registerCommand(mcInfo);
        listener.registerCommand(mcSkinFile);

        GlobalEventChannel.INSTANCE.registerListenerHost(listener);
    }
}
