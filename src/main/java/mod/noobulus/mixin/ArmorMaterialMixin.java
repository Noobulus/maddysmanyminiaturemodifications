package mod.noobulus.mixin;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ArmorMaterial.class)
public class ArmorMaterialMixin {

    @Inject(method = "repairIngredient()Ljava/util/function/Supplier;", at = @At("RETURN"), cancellable = true)
    private void repairNetheriteArmorWithDiamonds(CallbackInfoReturnable<Supplier<Ingredient>> cir) {
        if (cir.getReturnValue().get().test(Items.NETHERITE_INGOT.getDefaultStack())) {
            cir.setReturnValue(() -> Ingredient.ofItems(Items.DIAMOND));
        }
    }
}
