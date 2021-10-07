package tax.cute.mcinfoplugin;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.utils.ExternalResource;
import tax.cute.minecraftinfoapi.CommonException;
import tax.cute.minecraftinfoapi.Player;
import tax.cute.minecraftinfoapi.Profile;
import top.mrxiaom.miraiutils.CommandModel;
import top.mrxiaom.miraiutils.CommandSender;
import top.mrxiaom.miraiutils.CommandSenderGroup;

import java.io.IOException;

public class MCSkinFile extends CommandModel {
    Plugin plugin;

    public MCSkinFile(Plugin plugin) {
        super("mcSkinFile");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, SingleMessage[] args) {
        if (sender instanceof CommandSenderGroup) {
            CommandSenderGroup senderGroup = (CommandSenderGroup) sender;
            Group group = senderGroup.getGroup();

            if (args[0].contentToString().isEmpty()) {
                group.sendMessage("请输入有效的用户名");
                return;
            }

            if (args[0].contentToString().equalsIgnoreCase("/mcSkinFile")) {
                group.sendMessage("用法:/mcskinfile [用户名/uuid]");
                return;
            }

            try {
                send(group, args[0].contentToString());
            } catch (IOException e) {
                group.sendMessage("请求失败,请稍后再试");
            } catch (CommonException e) {
                group.sendMessage("查询失败:无法查找到这个玩家");
            } catch (Exception e) {
                group.sendMessage(String.valueOf(e));
                e.printStackTrace();
            }
        }
    }

    private void send(Group group, String name) throws IOException, CommonException {
        String uuid;
        if(Util.isUuid(name))
            uuid = name;
        else
            uuid = Player.getPlayer(name).getUuid();

        Profile profile = Profile.getProfile(uuid);

        if (profile.getSkinUrl() != null) {
            Image image = group.uploadImage(ExternalResource.create(profile.getSkinBytes()));
            group.sendMessage(image);
        } else {
            group.sendMessage("获取失败");
        }

        if (profile.getCapeUrl() != null) {
            Image image = group.uploadImage(ExternalResource.create(profile.getCapeBytes()));
            group.sendMessage(image);
        }
    }
}