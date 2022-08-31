package com.moonsworth.apollo.api.module.impl;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.protobuf.ByteString;
import com.moonsworth.apollo.api.ApolloPlatform;
import com.moonsworth.apollo.api.events.impl.player.EventApolloPlayerRegister;
import com.moonsworth.apollo.api.module.ApolloModule;
import com.moonsworth.apollo.api.options.ApolloOption;
import com.moonsworth.apollo.api.protocol.AddWaypointMessage;
import com.moonsworth.apollo.api.protocol.Waypoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WaypointModule extends ApolloModule {

    private final List<AddWaypointMessage> waypoints = new ArrayList<>();

    public WaypointModule() {
        super("WaypointModule");
        handle(EventApolloPlayerRegister.class, event -> {
            for (AddWaypointMessage waypoint : waypoints) {
                event.getPlayer().sendPacket(waypoint);
            }
        });
    }

    @Override
    public List<ApolloOption> options() {
        return new ArrayList<>();
    }

    @Override
    public boolean notifyPlayers() {
        return false;
    }

    @Override
    public List<ApolloPlatform.Kind> runsOn() {
        return ImmutableList.of(ApolloPlatform.Kind.SERVER);
    }


    @Override
    public void loadConfiguration(Map<String, Object> configuration) {
        List<Map<?, ?>> maps = (List<Map<?, ?>>) configuration.get(getName() + ".waypoints");
        for (Map<?, ?> map : maps) {
            // Create the waypoint.
            // This is super brittle, and could be done better most likely.
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                JsonObject object = new JsonParser().parse(String.valueOf(entry.getValue())).getAsJsonObject();
                var waypoint = Waypoint.newBuilder().setName(ByteString.copyFromUtf8(object.get("name").getAsString())).setWorld(ByteString.copyFromUtf8(object.get("world").getAsString())).build();
                waypoints.add(AddWaypointMessage.newBuilder()
                        .setWaypoint(waypoint)
                        .setX(object.get("x").getAsInt())
                        .setY(object.get("y").getAsInt())
                        .setZ(object.get("z").getAsInt())
                        .setColor(object.get("color").getAsInt())
                        .setForced(object.get("forced").getAsBoolean())
                        .setVisible(object.get("visible").getAsBoolean())
                        .build());
            }
        }
    }
}
