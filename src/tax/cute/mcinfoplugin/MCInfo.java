package tax.cute.mcinfoplugin;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.utils.ExternalResource;
import tax.cute.minecraftinfoapi.PlayerInfo;
import tax.cute.minecraftinfoapi.UUID;
import top.mrxiaom.miraiutils.CommandModel;
import top.mrxiaom.miraiutils.CommandSender;
import top.mrxiaom.miraiutils.CommandSenderGroup;

import java.io.IOException;

public class MCInfo extends CommandModel {
    public MCInfo() {
        super("mcinfo");
    }

    @Override
    public void onCommand(CommandSender sender, SingleMessage[] args) {
        if (sender instanceof CommandSenderGroup) {
            CommandSenderGroup senderGroup = (CommandSenderGroup) sender;
            Group group = senderGroup.getGroup();

            if (args[0].contentToString().equalsIgnoreCase("/mcInfo")) {
                group.sendMessage("用法:/mcinfo [名称/UUID]");
                return;
            }

            if (args[0].contentToString().isEmpty()) {
                group.sendMessage("请输入有效用户名");
                return;
            }
            try {
                sendMcInfo(group, args[0].contentToString());
            } catch (Exception e) {
                group.sendMessage("获取失败,请稍后重试");
                e.printStackTrace();
            }
        }
    }

    private void sendMcInfo(Group group, String name) throws IOException {
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

        PlayerInfo info = PlayerInfo.getPlayerInfo(id);
        if (info == null) {
            group.sendMessage("查询失败");
            return;
        }
        String text =
                "==MC档案查询==" +
                        "\n[ 用户名 ] " + info.getNowName() +
                        "\n[ UUID ] " + id;

        byte[] avatar_bytes = Util.getWebBytes(ApiUrl.MC_HEAD_SKIN + id);
        if (avatar_bytes == null) {
            group.sendMessage("查询失败");
            return;
        }

        Image avatar = group.uploadImage(ExternalResource.create(avatar_bytes));
        group.sendMessage(avatar.plus(text));

        ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
        builder.add(group.getBot().getId(), group.getBot().getNick(), new PlainText("曾用名"));

        for (int i = 0; i < info.getData().size(); i++) {
            String time;
            if (info.getData().get(i).getTimestamp() == -1) {
                time = "初始用户名";
            } else {
                time = info.getData().get(i).getTime();
            }
            builder.add(group.getBot().getId(), group.getBot().getNick(), new PlainText(
                    "名称:" + info.getData().get(i).getName() +
                            "\n修改时间:" + time
            ));
        }

        group.sendMessage(builder.build());
    }
}
