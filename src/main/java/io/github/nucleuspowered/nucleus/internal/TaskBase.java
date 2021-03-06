/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.internal;

import io.github.nucleuspowered.nucleus.internal.permissions.PermissionInformation;
import org.spongepowered.api.scheduler.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class TaskBase implements Consumer<Task> {

    public abstract boolean isAsync();

    public abstract int secondsPerRun();

    public Map<String, PermissionInformation> getPermissions() {
        return new HashMap<>();
    }
}
