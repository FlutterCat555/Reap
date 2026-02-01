package uwu.fluttercat.reap.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.minecraft.world.waypoint.Waypoint;
import uwu.fluttercat.reap.Reap;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ModItems {

    public static final Item SCYTHE = registerItem("scythe", (settings) -> new ScytheItem(ToolMaterial.IRON, 2.5F, -2.7F, settings){
        @Override
        public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
            textConsumer.accept(Text.translatable("tooltip.reap.scythe.tooltip.ln1"));
            super.appendTooltip(stack, context, displayComponent, textConsumer, type);
            textConsumer.accept(Text.translatable("tooltip.reap.scythe.tooltip.ln2"));
            super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        }
    });
    public static final Item NETHERITE_SCYTHE = registerItem("netherite_scythe", (settings) -> new ScytheItem(ToolMaterial.NETHERITE, 1.5F, -3.0F, settings.fireproof()){
        @Override
        public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
            textConsumer.accept(Text.translatable("tooltip.reap.scythe.tooltip.ln1"));
            super.appendTooltip(stack, context, displayComponent, textConsumer, type);
            textConsumer.accept(Text.translatable("tooltip.reap.scythe.tooltip.ln2"));
            super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        }
    });

    public static final Item HOOD = registerItem("hood", (settings) -> new Item(Waypoint.disableTracking(settings.armor(ModArmorMaterials.HOOD_MATERIAL, EquipmentType.HELMET))){
        @Override
        public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
            textConsumer.accept(Text.translatable("tooltip.reap.hood.tooltip.ln1"));
            super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        }
    });

    private static Item registerItem(String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(Reap.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Reap.MOD_ID, name)))));
    }
    public static void registerModItems() {
        Reap.LOGGER.info("Registering Mod Items for " + Reap.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(SCYTHE);
            entries.add(NETHERITE_SCYTHE);
            entries.add(HOOD);
        });
    }
}
