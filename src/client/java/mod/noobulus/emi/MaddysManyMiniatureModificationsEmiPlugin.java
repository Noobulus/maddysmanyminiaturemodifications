package mod.noobulus.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import mod.noobulus.MaddysManyMiniatureModifications;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class MaddysManyMiniatureModificationsEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addRecipe(new MaddysManyMiniatureModificationsEmiAnvilRecipe(EmiStack.of(Items.BOW),
                EmiIngredient.of(Ingredient.fromTag(ItemTags.PLANKS)), Identifier.of(MaddysManyMiniatureModifications.MOD_ID, "/bow_planks_repair")));
        registry.addRecipe(new MaddysManyMiniatureModificationsEmiAnvilRecipe(EmiStack.of(Items.CROSSBOW),
                EmiIngredient.of(Ingredient.fromTag(ItemTags.PLANKS)), Identifier.of(MaddysManyMiniatureModifications.MOD_ID, "/crossbow_planks_repair")));
        registry.addRecipe(new MaddysManyMiniatureModificationsEmiAnvilRecipe(EmiStack.of(Items.BRUSH),
                EmiIngredient.of(Ingredient.ofItems(Items.COPPER_INGOT)), Identifier.of(MaddysManyMiniatureModifications.MOD_ID, "/brush_ingot_repair")));
        registry.addRecipe(new MaddysManyMiniatureModificationsEmiAnvilRecipe(EmiStack.of(Items.FISHING_ROD),
                EmiIngredient.of(Ingredient.ofItems(Items.STRING)), Identifier.of(MaddysManyMiniatureModifications.MOD_ID, "/fishing_rod_string_repair")));
        registry.addRecipe(new MaddysManyMiniatureModificationsEmiAnvilRecipe(EmiStack.of(Items.FLINT_AND_STEEL),
                EmiIngredient.of(Ingredient.ofItems(Items.IRON_INGOT)), Identifier.of(MaddysManyMiniatureModifications.MOD_ID, "/flint_and_steel_ingot_repair")));
        registry.addRecipe(new MaddysManyMiniatureModificationsEmiAnvilRecipe(EmiStack.of(Items.CARROT_ON_A_STICK),
                EmiIngredient.of(Ingredient.ofItems(Items.CARROT)), Identifier.of(MaddysManyMiniatureModifications.MOD_ID, "/carrot_on_a_stick_repair")));
        registry.addRecipe(new MaddysManyMiniatureModificationsEmiAnvilRecipe(EmiStack.of(Items.WARPED_FUNGUS_ON_A_STICK),
                EmiIngredient.of(Ingredient.ofItems(Items.WARPED_FUNGUS)), Identifier.of(MaddysManyMiniatureModifications.MOD_ID, "/wungus_on_a_stick_repair")));
        registry.addRecipe(new MaddysManyMiniatureModificationsEmiAnvilRecipe(EmiStack.of(Items.SHEARS),
                EmiIngredient.of(Ingredient.ofItems(Items.IRON_INGOT)), Identifier.of(MaddysManyMiniatureModifications.MOD_ID, "/shears_ingot_repair")));
        registry.addRecipe(new MaddysManyMiniatureModificationsEmiAnvilRecipe(EmiStack.of(Items.TRIDENT),
                EmiIngredient.of(Ingredient.ofItems(Items.PRISMARINE_CRYSTALS)), Identifier.of(MaddysManyMiniatureModifications.MOD_ID, "/trident_crystals_repair")));
    }
}
