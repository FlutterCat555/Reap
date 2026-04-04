package dev.fluttercat.reap.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.waypoints.Waypoint;
import dev.fluttercat.reap.Reap;

import java.util.function.Consumer;
import java.util.function.Function;

public class ModItems {

//    public static final Item NAIL = registerItem("nail", (settings) -> new NailItem(settings.sword(ToolMaterial.STONE, 0F, -1F)));

    public static final Item SCYTHE = registerItem("scythe", (settings) -> new ScytheItem(ToolMaterial.IRON, 2.5F, -2.7F, settings){
        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
            textConsumer.accept(Component.translatable("tooltip.reap.scythe.tooltip.ln1"));
            super.appendHoverText(stack, context, displayComponent, textConsumer, type);
            textConsumer.accept(Component.translatable("tooltip.reap.scythe.tooltip.ln2"));
            super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        }
    });
    public static final Item NETHERITE_SCYTHE = registerItem("netherite_scythe", (settings) -> new ScytheItem(ToolMaterial.NETHERITE, 1.5F, -3.0F, settings.fireResistant()){
        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
            textConsumer.accept(Component.translatable("tooltip.reap.scythe.tooltip.ln1"));
            super.appendHoverText(stack, context, displayComponent, textConsumer, type);
            textConsumer.accept(Component.translatable("tooltip.reap.scythe.tooltip.ln2"));
            super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        }
    });

    public static final Item HOOD = registerItem("hood", (settings) -> new Item(Waypoint.addHideAttribute(settings.humanoidArmor(ModArmorMaterials.HOOD_MATERIAL, ArmorType.HELMET))){
        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
            textConsumer.accept(Component.translatable("tooltip.reap.hood.tooltip.ln1"));
            super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        }
    });

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Reap.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Reap.MOD_ID, name)))));
    }
    public static void registerModItems() {
        Reap.LOGGER.info("Registering Mod Items for " + Reap.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(entries -> {
//            entries.insertAfter(Items.NETHERITE_SWORD,NAIL);
            entries.insertAfter(Items.NETHERITE_AXE,SCYTHE);
            entries.insertAfter(SCYTHE,NETHERITE_SCYTHE);
            entries.insertAfter(Items.SHIELD,HOOD);
        });
    }
}
