package mod.noobulus.mixin;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToolMaterials.class)
public class ToolMaterialsMixin {

    @Inject(method = "getRepairIngredient()Lnet/minecraft/recipe/Ingredient;", at = @At("RETURN"), cancellable = true)
    private void repairNetheriteToolsWithDiamonds(CallbackInfoReturnable<Ingredient> cir) {
        if (cir.getReturnValue().test(Items.NETHERITE_INGOT.getDefaultStack())) {
           cir.setReturnValue(Ingredient.ofItems(Items.DIAMOND));
        }
    }
}
