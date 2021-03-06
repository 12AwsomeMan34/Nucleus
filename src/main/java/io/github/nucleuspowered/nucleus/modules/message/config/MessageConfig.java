/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.message.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MessageConfig {

    @Setting(value = "helpop-prefix", comment = "loc:config.message.helpop.prefix")
    private String helpOpPrefix = "&7HelpOp: {{name}} &7> &r";

    @Setting(value = "msg-receiver-prefix", comment = "loc:config.message.receiver.prefix")
    private String messageReceiverPrefix = "&7[{{fromDisplay}}&7 -> me]: &r";

    @Setting(value = "msg-sender-prefix", comment = "loc:config.message.sender.prefix")
    private String messageSenderPrefix = "&7[me -> {{toDisplay}}&7]: &r";

    @Setting(value = "msg-socialspy-prefix", comment = "loc:config.message.socialspy.prefix")
    private String messageSocialSpyPrefix = "&7[SocialSpy] [{{fromDisplay}}&7 -> {{toDisplay}}&7]: &r";

    public String getHelpOpPrefix() {
        return helpOpPrefix;
    }

    public String getMessageReceiverPrefix() {
        return messageReceiverPrefix;
    }

    public String getMessageSenderPrefix() {
        return messageSenderPrefix;
    }

    public String getMessageSocialSpyPrefix() {
        return messageSocialSpyPrefix;
    }
}
