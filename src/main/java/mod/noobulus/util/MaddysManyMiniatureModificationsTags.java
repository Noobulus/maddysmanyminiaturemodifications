package mod.noobulus.util;

import mod.noobulus.MaddysManyMiniatureModifications;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class MaddysManyMiniatureModificationsTags {
    public static class Items {
        public static final TagKey<Item> HORSE_BULK_FOOD = create("horse_bulk_food");

        private static TagKey<Item> create(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(MaddysManyMiniatureModifications.MOD_ID, name));
        }
    }
}
