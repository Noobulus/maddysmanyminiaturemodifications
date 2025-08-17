package mod.noobulus.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import mod.noobulus.util.MaddysManyMiniatureModificationsTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LlamaEntity.class)
public class LlamaEntityMixin {
    @ModifyVariable(method = "setStrength(I)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public int alwaysMaxLlamaStrength(int strength) {
        return 5;
    }

    @Inject(method = "receiveFood(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/LlamaEntity;getHealth()F"))
    public void overrideFoodBehavior(PlayerEntity player, ItemStack item, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) LocalBooleanRef bl, @Local(ordinal = 0) LocalFloatRef f, @Local(ordinal = 0) LocalIntRef i, @Local(ordinal = 1) LocalIntRef j) {
        bl.set(false);
        if (item.isIn(ItemTags.LLAMA_TEMPT_ITEMS) || item.isIn(ItemTags.LLAMA_FOOD)) {
            LlamaEntity llama = ((LlamaEntity) (Object) this);
            FoodComponent foodComponent = item.getOrDefault(DataComponentTypes.FOOD, (new FoodComponent.Builder()).nutrition(2).saturationModifier(1.2F).build()); // default food component
            int nutrition = foodComponent.nutrition();
            float saturation = nutrition * foodComponent.saturation();
            f.set(nutrition); // heal by nutrition like with horses
            j.set((int) Math.max(Math.ceil(saturation), 3));
            i.set(nutrition * 5); // llamas have different growth speed scaling than horses
            if (item.isIn(ItemTags.LLAMA_TEMPT_ITEMS) && !llama.getWorld().isClient && llama.isTame() && llama.getBreedingAge() == 0 && llama.canEat()) {
                i.set(i.get() * 10);
                f.set(f.get() * 5);
                j.set(j.get() * 2); // scaling is different on llamas for some reason so, hey
                bl.set(true);
                llama.lovePlayer(player);
            }
        }
    }

    @Redirect(method = "receiveFood(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/LlamaEntity;lovePlayer(Lnet/minecraft/entity/player/PlayerEntity;)V"))
    public void redirectLove(LlamaEntity instance, PlayerEntity playerEntity) {} // same shit as the llama mixin
}
