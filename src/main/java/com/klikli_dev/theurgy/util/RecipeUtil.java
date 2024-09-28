package com.klikli_dev.theurgy.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;

public class RecipeUtil {

    public static RecipeManager getRecipeManager() {
        if (FMLEnvironment.dist.isClient()) {
            return DistHelper.getRecipeManager();
        }
        if(ServerLifecycleHooks.getCurrentServer() == null)
            return null;
        return ServerLifecycleHooks.getCurrentServer().getRecipeManager();
    }

    public static class DistHelper {
        public static RecipeManager getRecipeManager() {
            if(Minecraft.getInstance().level == null)
                return null;
            return Minecraft.getInstance().level.getRecipeManager();
        }
    }
}
