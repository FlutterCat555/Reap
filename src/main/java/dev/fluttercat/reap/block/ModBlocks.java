package dev.fluttercat.reap.block;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import dev.fluttercat.reap.Reap;

import java.util.function.Function;

public class ModBlocks {
    public static final Block LEAF_LAYER = registerBlock("leaf_layer",
            properties -> new LeafLayerBlock(properties
                    .mapColor(MapColor.COLOR_BROWN)
                    .forceSolidOn()
                    .noCollision()
                    .strength(0.0F)
                    .sound(SoundType.LEAF_LITTER)
                    .isViewBlocking((state, world, pos) -> (Integer)state.getValue(LeafLayerBlock.LAYERS) >= 8)
                    .pushReaction(PushReaction.DESTROY)
            )
    );



    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        Block toRegister = function.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Reap.MOD_ID, name))));
        registerBlockItem(name, toRegister);
        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(Reap.MOD_ID, name), toRegister);
    }

    private static Block registerBlockWithoutBlockItem(String name, Function<BlockBehaviour.Properties, Block> function) {
        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(Reap.MOD_ID, name),
                function.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Reap.MOD_ID, name)))));
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Reap.MOD_ID, name),
                new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix()
                        .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Reap.MOD_ID, name)))));
    }
    
    public static void registerModBlocks() {
        Reap.LOGGER.info("Registering Mod Blocks for " + Reap.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.accept(ModBlocks.LEAF_LAYER);
        });
    }
}
