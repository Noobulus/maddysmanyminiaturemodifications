package mod.noobulus.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.OnAStickItem;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OnAStickItem.class)
public abstract class OnAStickItemMixin extends Item {
    public OnAStickItemMixin(Settings settings) {
        super(settings);
    }

    @Intrinsic
    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return super.canRepair(stack, ingredient);
    }

    @Inject(method = "canRepair(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
    private void canRepairWithString(ItemStack stack, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (((OnAStickItem) (Object) this).target == EntityType.PIG) {
            cir.setReturnValue(ingredient.isOf(Items.CARROT) || cir.getReturnValue());
        } else if (((OnAStickItem) (Object) this).target == EntityType.STRIDER) {
            cir.setReturnValue(ingredient.isOf(Items.WARPED_FUNGUS) || cir.getReturnValue());
        }
    }
}
