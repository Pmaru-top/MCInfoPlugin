package tax.cute.mcinfoplugin;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.utils.ExternalResource;
import tax.cute.minecraftinfoapi.UUID;
import top.mrxiaom.miraiutils.CommandModel;
import top.mrxiaom.miraiutils.CommandSender;
import top.mrxiaom.miraiutils.CommandSenderGroup;

import java.io.IOException;

public class MCSkin extends CommandModel {
    public MCSkin() {
        super("mcskin");
    }

    @Override
    public void onCommand(CommandSender sender, SingleMessage[] args) {
        if (sender instanceof CommandSenderGroup) {
            CommandSenderGroup senderGroup = (CommandSenderGroup)sender;
            Group group = senderGroup.getGroup();

            if (args[0].contentToString().equalsIgnoreCase("/mcskin")) {
                group.sendMessage("用法:/mcskin [名称/UUID]");
                return;
            }

            if (args[0].contentToString().isEmpty()) {
                group.sendMessage("请输入有效用户名");
                return;
            }

            try {
                send(group, args[0].contentToString());
            } catch (Exception e) {
                group.sendMessage("获取失败,请稍后重试");
                e.printStackTrace();
            }
        }
    }

    private void send(Group group,String name) throws IOException {
        String id;
        if (name.replace("-", "").length() == 32) {
            id = name;
        } else {
            UUID uuid = UUID.getId(name);
            if (uuid == null) {
                group.sendMessage("无法找到这个用户");
                return;
            }
            id = uuid.getId();
        }

        byte[] skin_bytes = Util.getWebBytes(ApiUrl.MC_BODY_SKIN + id);
        if (skin_bytes == null) {
            group.sendMessage("查询失败");
            return;
        }
        Image image = group.uploadImage(ExternalResource.create(skin_bytes));
        group.sendMessage(image);
    }
}
