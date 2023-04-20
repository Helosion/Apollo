package com.lunarclient.apollo.module.type;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.player.AbstractApolloPlayer;
import com.lunarclient.apollo.player.ApolloPlayer;
import com.lunarclient.apollo.player.ui.ModSetting;
import lunarclient.apollo.common.OptionOperation;
import lunarclient.apollo.modules.ModSettingMessage;

import static java.util.Objects.requireNonNull;

/**
 * Provides the mod settings module.
 *
 * @since 1.0.0
 */
public final class ModSettingsImpl extends ModSettings {

    public ModSettingsImpl() {
        super();
    }

    @Override
    public void sendSetting(ApolloPlayer player, ModSetting setting) {
        requireNonNull(player, "player");
        requireNonNull(setting, "setting");

        ((AbstractApolloPlayer) player).sendPacket(this, OptionOperation.ADD, this.to(setting));
    }

    @Override
    public void broadcastSetting(ModSetting setting) {
        requireNonNull(setting, "setting");

        for(ApolloPlayer player : Apollo.getPlayerManager().getPlayers()) {
            ((AbstractApolloPlayer) player).sendPacket(this, OptionOperation.ADD, this.to(setting));
        }
    }

    private ModSettingMessage to(ModSetting setting) {
        return ModSettingMessage.newBuilder()
            .setModId(setting.getModId())
            .setEnabled(setting.isEnabled())
            .putAllProperties(setting.getProperties())
            .build();
    }

    private ModSetting from(ModSettingMessage setting) {
        return ModSetting.of(
            setting.getModId(),
            setting.getEnabled(),
            setting.getPropertiesMap()
        );
    }
}
