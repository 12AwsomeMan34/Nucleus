/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.info.config;

import io.github.nucleuspowered.nucleus.Util;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class InfoConfig {

    @Setting(value = "show-motd-on-join", comment = "loc:config.motd.onjoin")
    private boolean showMotdOnJoin = true;

    @Setting(value = "motd-title", comment = "loc:config.motd.title")
    private String motdTitle = Util.getMessageWithFormat("motd.title");

    public boolean isShowMotdOnJoin() {
        return showMotdOnJoin;
    }

    public String getMotdTitle() {
        return motdTitle;
    }
}
