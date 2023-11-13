package dev.stormwatch.elite.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class EliteKeyMappings {

    public static final String KEY_NAMESPACE = "key.elite.";
    public static final String KEY_CATEGORY = "key.categories.elite";

    public static final Lazy<KeyMapping> CHARM_ABILITY_1 = Lazy.of(() -> new KeyMapping(
            KEY_NAMESPACE + "charm_ability_1",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            KEY_CATEGORY
    ));

    public static final Lazy<KeyMapping> CHARM_ABILITY_2 = Lazy.of(() -> new KeyMapping(
            KEY_NAMESPACE + "charm_ability_2",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            KEY_CATEGORY
    ));

    public static final Lazy<KeyMapping> CHARM_ABILITY_3 = Lazy.of(() -> new KeyMapping(
            KEY_NAMESPACE + "charm_ability_3",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            KEY_CATEGORY
    ));

}
