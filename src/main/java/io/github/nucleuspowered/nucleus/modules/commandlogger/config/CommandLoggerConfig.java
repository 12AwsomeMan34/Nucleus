/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.commandlogger.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class CommandLoggerConfig {

    @Setting(value = "log-command-source", comment = "loc:config.commandlogger.source.base")
    private LoggerTargetConfig loggerTarget = new LoggerTargetConfig();

    @Setting(value = "whitelist", comment = "loc:config.commandlogger.whitelist")
    private boolean isWhitelist = false;

    @Setting(value = "command-filter", comment = "loc:config.commandlogger.list")
    private List<String> commandsToFilter = new ArrayList<>();

    @Setting(value = "separate-file", comment = "loc:config.commandlogger.separatefile")
    private boolean separateFile = false;

    public LoggerTargetConfig getLoggerTarget() {
        return loggerTarget;
    }

    public boolean isWhitelist() {
        return isWhitelist;
    }

    public List<String> getCommandsToFilter() {
        return commandsToFilter;
    }

    public boolean separateFile() {
        return separateFile;
    }
}
