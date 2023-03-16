package com.moonsworth.apollo.world;

import lombok.Value;

/**
 * Represents a block location in the world.
 *
 * @since 1.0.0
 */
@Value(staticConstructor = "of")
public class ApolloBlockLocation {

    /**
     * Returns the world name for this location.
     *
     * @return the world name
     * @since 1.0.0
     */
    String world;

    /**
     * Returns the {@code int} X coordinate for this location.
     *
     * @return the x coordinate
     * @since 1.0.0
     */
    int x;

    /**
     * Returns the {@code int} Y coordinate for this location.
     *
     * @return the y coordinate
     * @since 1.0.0
     */
    int y;

    /**
     * Returns the {@code int} Z coordinate for this location.
     *
     * @return the z coordinate
     * @since 1.0.0
     */
    int z;

}
