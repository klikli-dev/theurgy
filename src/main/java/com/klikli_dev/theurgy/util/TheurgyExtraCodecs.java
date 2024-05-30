// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableGraph;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class TheurgyExtraCodecs {
    private static final Map<String, Tiers> TIERS = Map.of(
            "wood", Tiers.WOOD,
            "stone", Tiers.STONE,
            "iron", Tiers.IRON,
            "diamond", Tiers.DIAMOND,
            "gold", Tiers.GOLD,
            "netherite", Tiers.NETHERITE
    );

    public static final Codec<Tiers> TIERS_CODEC = Codec.stringResolver(Tiers::name, TIERS::get);

    public static final Codec<FluidStack> SINGLE_FLUID_CODEC = BuiltInRegistries.FLUID.byNameCodec().xmap(fluid -> new FluidStack(fluid, 1), FluidStack::getFluid);

    @SuppressWarnings("UnstableApiUsage")
    public static <V> Codec<MutableGraph<V>> graph(Codec<V> elementCodec, Supplier<MutableGraph<V>> graphSupplier) {
        return internalGraph(elementCodec).xmap(
                internalGraph -> {
                    MutableGraph<V> g = graphSupplier.get();
                    internalGraph.nodes().forEach(g::addNode);
                    internalGraph.edges().forEach(g::putEdge);
                    return g;
                },
                graph -> {
                    List<V> nodes = new ArrayList<>(graph.nodes());
                    List<EndpointPair<V>> edges = new ArrayList<>(graph.edges());
                    return new InternalGraph<>(nodes, edges);
                }
        );
    }

    @SuppressWarnings("UnstableApiUsage")
    private static <V> Codec<EndpointPair<V>> unorderedEndpointPair(Codec<V> elementCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(
                elementCodec.fieldOf("nodeU").forGetter(EndpointPair::nodeU),
                elementCodec.fieldOf("nodeV").forGetter(EndpointPair::nodeV)
        ).apply(instance, EndpointPair::unordered));
    }

    @SuppressWarnings("UnstableApiUsage")
    private static <V> Codec<InternalGraph<V>> internalGraph(Codec<V> elementCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.list(elementCodec).fieldOf("nodes").forGetter(InternalGraph::nodes),
                Codec.list(unorderedEndpointPair(elementCodec)).fieldOf("edges").forGetter(InternalGraph::edges)
        ).apply(instance, InternalGraph::new));
    }

    @SuppressWarnings("UnstableApiUsage")
    private record InternalGraph<V>(List<V> nodes, List<EndpointPair<V>> edges) {

    }
}