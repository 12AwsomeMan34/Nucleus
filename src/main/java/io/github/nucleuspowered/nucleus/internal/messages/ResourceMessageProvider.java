/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.internal.messages;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class ResourceMessageProvider extends MessageProvider {

    protected final ResourceBundle rb = ResourceBundle.getBundle("assets.nucleus.messages", Locale.getDefault());

    @Override
    public Optional<String> getMessageFromKey(String key) {
        if (rb.containsKey(key)) {
            return Optional.of(rb.getString(key));
        }

        return Optional.empty();
    }

    public Set<String> getKeys() {
        return rb.keySet();
    }
}
