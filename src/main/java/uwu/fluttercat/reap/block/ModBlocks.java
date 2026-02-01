package uwu.fluttercat.reap.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import uwu.fluttercat.reap.Reap;

import java.util.function.Function;

public class ModBlocks {
    public static final Block LEAF_LAYER = registerBlock("leaf_layer",
            properties -> new LeafLayerBlock(properties
                    .mapColor(MapColor.BROWN)
                    .solid()
                    .noCollision()
                    .strength(0.0F)
                    .sounds(BlockSoundGroup.LEAF_LITTER)
                    .blockVision((state, world, pos) -> (Integer)state.get(LeafLayerBlock.LAYERS) >= 8)
                    .pistonBehavior(PistonBehavior.DESTROY)
            )
    );



    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> function) {
        Block toRegister = function.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Reap.MOD_ID, name))));
        registerBlockItem(name, toRegister);
        return Registry.register(Registries.BLOCK, Identifier.of(Reap.MOD_ID, name), toRegister);
    }
    
    private static Block registerBlockWithoutBlockItem(String name, Function<AbstractBlock.Settings, Block> function) {
        return Registry.register(Registries.BLOCK, Identifier.of(Reap.MOD_ID, name),
                function.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Reap.MOD_ID, name)))));
    }
    
    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Reap.MOD_ID, name),
                new BlockItem(block, new Item.Settings().useBlockPrefixedTranslationKey()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Reap.MOD_ID, name)))));
    }
    
    public static void registerModBlocks() {
        Reap.LOGGER.info("Registering Mod Blocks for " + Reap.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.LEAF_LAYER);
        });
    }
}
