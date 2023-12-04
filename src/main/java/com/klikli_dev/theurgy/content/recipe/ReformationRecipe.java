package com.klikli_dev.theurgy.content.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.klikli_dev.theurgy.content.recipe.wrapper.ReformationArrayRecipeWrapper;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.util.TheurgyExtraCodecs;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ReformationRecipe implements Recipe<ReformationArrayRecipeWrapper> {

    public static final int DEFAULT_REFORMATION_TIME = 100;

    public static final Codec<ReformationRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    IngedientWithCount.CODEC.listOf().fieldOf("sources").forGetter(r -> r.sources),
                    TheurgyExtraCodecs.INGREDIENT.fieldOf("target").forGetter(r -> r.target),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("reformationTime", DEFAULT_REFORMATION_TIME).forGetter(r -> r.reformationTime)
            ).apply(instance, ReformationRecipe::new)
    );
    protected final List<IngedientWithCount> sources;
    protected final Ingredient target;
    protected final ItemStack result;
    protected final int reformationTime;
    protected ResourceLocation id;

    public ReformationRecipe(List<IngedientWithCount> sources, Ingredient target, ItemStack result, int reformationTime) {
        this.sources = sources;
        this.target = target;
        this.result = result;
        this.reformationTime = reformationTime;
    }

    @Override
    public boolean matches(ReformationArrayRecipeWrapper pContainer, Level pLevel) {

        //if the target does not match we can exit early.
        if (this.target.test(pContainer.getTargetPedestalInv().getStackInSlot(0)))
            return false;

        //For the sources the tricky part is that the amount of source pedestals does not need to match the amount of sources in the recipe.
        //Specifically, one recipe source with a count > 1 can be satisfied by the combination of multiple source pedestals.
        //So we have to check for each source if it can be satisfied by the pedestals, while ensuring that pedestals contents are not double counted.

        var remainingSources = new ArrayList<>(this.sources);
        var pedestalsToCheck = pContainer.getSourcePedestalInvs().stream().map(p -> p.getStackInSlot(0).copy()).toList();
        for (var source : remainingSources) {

            var remainingCount = source.count;

            for (var sourceInputStack : pedestalsToCheck) {
                if (source.ingredient.test(sourceInputStack)) {
                    var delta = Math.min(sourceInputStack.getCount(), remainingCount);
                    remainingCount -= delta;
                    sourceInputStack.shrink(delta);

                    if (remainingCount == 0)
                        break;
                }
            }

            if (remainingCount > 0)
                return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(ReformationArrayRecipeWrapper pContainer, RegistryAccess pRegistryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.REFORMATION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.REFORMATION.get();
    }

    public record IngedientWithCount(Ingredient ingredient, int count) {
        public static final Codec<IngedientWithCount> CODEC = Codec.pair(
                Codec.INT.optionalFieldOf("amount", 1).codec(),
                TheurgyExtraCodecs.INGREDIENT
        ).xmap(s -> new IngedientWithCount(s.getSecond(), s.getFirst()), s -> Pair.of(s.count, s.ingredient));
    }

    public static class Serializer implements RecipeSerializer<ReformationRecipe> {

        @Override
        public ReformationRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            var recipe = CODEC.parse(JsonOps.INSTANCE, pJson).getOrThrow(false, s -> {
                throw new JsonParseException(s);
            });
            recipe.id = pRecipeId;
            return recipe;
        }

        @Override
        public ReformationRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var recipe = pBuffer.readJsonWithCodec(CODEC);
            recipe.id = pRecipeId;
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ReformationRecipe pRecipe) {
            pBuffer.writeJsonWithCodec(CODEC, pRecipe);
        }
    }
}
