package mod.noobulus.mixin;

import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item {

    // this is probably hacky but i don't need to override anything about recipe remainders existing because buckets already have 'em

    public BucketItemMixin(Settings settings) {
        super(settings);
    }

    @Nullable
    @Intrinsic
    @Override
    public Item getRecipeRemainder() {
        return super.getRecipeRemainder();
    }

    @Inject(method = "getRecipeRemainder()Lnet/minecraft/item/Item;", at = @At("RETURN"), cancellable = true)
    private void remainderIsWaterBucket(CallbackInfoReturnable<Item> cir) {
        if (((BucketItem) (Object) this).fluid == Fluids.WATER) {
            cir.setReturnValue(Items.WATER_BUCKET);
        }
    }
}
