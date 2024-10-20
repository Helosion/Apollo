/*
 * This file is part of Apollo, licensed under the MIT License.
 *
 * Copyright (c) 2023 Moonsworth
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.lunarclient.apollo;

import com.lunarclient.apollo.listener.ApolloPlayerListener;
import com.lunarclient.apollo.listener.ApolloWorldListener;
import com.lunarclient.apollo.loader.PlatformPlugin;
import com.lunarclient.apollo.module.ApolloModuleManagerImpl;
import com.lunarclient.apollo.module.richpresence.RichPresenceModule;
import com.lunarclient.apollo.module.richpresence.RichPresenceModuleImpl;
import com.lunarclient.apollo.option.Options;
import com.lunarclient.apollo.option.OptionsImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

/**
 * The Bukkit platform plugin.
 *
 * @since 1.0.0
 */
@RequiredArgsConstructor
public final class ApolloBukkitPlatform implements PlatformPlugin, ApolloPlatform {

  @Getter private static ApolloBukkitPlatform instance;

  @Getter private final Options options = new OptionsImpl(null);
  @Getter private final JavaPlugin plugin;

  @Override
  public void onEnable() {
    ApolloBukkitPlatform.instance = this;

    ApolloManager.bootstrap(this);

    new ApolloPlayerListener(this.plugin);
    new ApolloWorldListener(this.plugin);

    ((ApolloModuleManagerImpl) Apollo.getModuleManager())
        .addModule(RichPresenceModule.class, new RichPresenceModuleImpl());

    try {
      ApolloManager.setConfigPath(this.plugin.getDataFolder().toPath());
      ApolloManager.loadConfiguration();
      ((ApolloModuleManagerImpl) Apollo.getModuleManager()).enableModules();
      ApolloManager.saveConfiguration();
    } catch (Throwable throwable) {
      this.getPlatformLogger()
          .log(Level.SEVERE, "Unable to load Apollo configuration and modules!", throwable);
    }

    Messenger messenger = this.plugin.getServer().getMessenger();
    messenger.registerOutgoingPluginChannel(this.plugin, ApolloManager.PLUGIN_MESSAGE_CHANNEL);
    messenger.registerIncomingPluginChannel(
        this.plugin,
        ApolloManager.PLUGIN_MESSAGE_CHANNEL,
        (channel, player, bytes) ->
            ApolloManager.getNetworkManager().receivePacket(player.getUniqueId(), bytes));

    if (Bukkit.getPluginManager().getPlugin("LunarClient-API") != null) {
      this.getPlatformLogger()
          .log(
              Level.WARNING,
              "Please remove the legacy API to prevent compatibility issues with Apollo!");
    }
  }

  @Override
  public void onDisable() {
    ((ApolloModuleManagerImpl) Apollo.getModuleManager()).disableModules();
  }

  @Override
  public Kind getKind() {
    return Kind.SERVER;
  }

  @Override
  public String getApolloVersion() {
    return this.plugin.getDescription().getVersion();
  }

  @Override
  public Logger getPlatformLogger() {
    return Bukkit.getServer().getLogger();
  }
}
