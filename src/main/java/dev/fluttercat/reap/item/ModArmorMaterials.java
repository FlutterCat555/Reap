package dev.fluttercat.reap.item;

import dev.fluttercat.reap.Reap;

import java.util.EnumMap;
import net.minecraft.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;

public class ModArmorMaterials {
    static ResourceKey<? extends Registry<EquipmentAsset>> REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));
    public static final ResourceKey<EquipmentAsset> HOOD_KEY = ResourceKey.create(REGISTRY_KEY, Identifier.fromNamespaceAndPath(Reap.MOD_ID, "hood"));

    public static final ArmorMaterial HOOD_MATERIAL = new ArmorMaterial(500, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS, 2);
        map.put(ArmorType.LEGGINGS, 4);
        map.put(ArmorType.CHESTPLATE, 6);
        map.put(ArmorType.HELMET, 2);
        map.put(ArmorType.BODY, 4);
    }), 20, SoundEvents.ARMOR_EQUIP_LEATHER,0,0, ModTags.Items.HOOD_REPAIR, HOOD_KEY);
}