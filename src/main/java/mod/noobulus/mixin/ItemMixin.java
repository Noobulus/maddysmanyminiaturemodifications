package mod.noobulus.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract boolean canRepair(ItemStack stack, ItemStack ingredient);

    @Inject(method = "canRepair(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
    private void diamondRepairsIfNetheriteDoes(ItemStack stack, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (ingredient.isOf(Items.DIAMOND) && !cir.getReturnValue() && canRepair(stack, Items.NETHERITE_INGOT.getDefaultStack())) {
            cir.setReturnValue(true);
        }
    }
}
