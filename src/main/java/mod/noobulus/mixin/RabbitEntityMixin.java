package mod.noobulus.mixin;

import mod.noobulus.util.RabbitEntityTrust;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(value = RabbitEntity.class)
public abstract class RabbitEntityMixin extends AnimalEntity implements RabbitEntityTrust {

    @Unique
    private static TrackedData<Boolean> TRUSTING;
    @Nullable
    @Unique
    private FleeEntityGoal fleeGoal;
    @Unique
    private static final Predicate<Entity> NOTICEABLE_PLAYER_FILTER;

    protected RabbitEntityMixin(EntityType<? extends RabbitEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    public boolean isTrusting() {
        return ((RabbitEntity) (Object) this).getDataTracker().get(TRUSTING);
    }

    @Unique
    public boolean maddysmanyminiaturemodifications$isTrusting() {
        return isTrusting();
    }

    @Unique
    public final void setTrusting(boolean trusting) {
        ((RabbitEntity) (Object) this).getDataTracker().set(TRUSTING, trusting);
    }

    @Unique
    public void maddysmanyminiaturemodifications$setTrusting(boolean trusting) {
        setTrusting(trusting);
    }

    @Redirect(method = "initGoals()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 5))
    private void removePlayerFleeGoal(GoalSelector instance, int priority, Goal goal) {
        // send method call to GBJ and do nothing
    }

    @Inject(method = "initGoals()V", at = @At("TAIL"))
    private void injectTrustingFleeGoal(CallbackInfo ci) {
        RabbitEntity rabbit = ((RabbitEntity) (Object) this);
        rabbit.goalSelector.add(4, new FleeEntityGoal<>(rabbit, PlayerEntity.class, 8.0F, 2.2, 2.2, (entity)
                -> NOTICEABLE_PLAYER_FILTER.test(entity) && !this.isTrusting()));
    }

    @Inject(method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void addTrustingToNBT(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("Trusting", this.isTrusting());
    }

    @Inject(method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void readTrustingFromNBT(NbtCompound nbt, CallbackInfo ci) {
        this.setTrusting(nbt.getBoolean("Trusting"));
    }

    @Inject(method = "initDataTracker(Lnet/minecraft/entity/data/DataTracker$Builder;)V", at = @At("TAIL"))
    private void addTrustingToDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(TRUSTING, false);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerTrustingTracking(CallbackInfo ci) {
        TRUSTING = DataTracker.registerData(RabbitEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Unique
    private void showEmoteParticle(boolean positive) {
        ParticleEffect particleEffect = ParticleTypes.HEART;
        if (!positive) {
            particleEffect = ParticleTypes.SMOKE;
        }

        for (int i = 0; i < 7; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.getWorld().addParticle(particleEffect, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d, e, f);
        }
    }

    @Intrinsic
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return super.interactMob(player, hand);
    }

    @Inject(method = "Lnet/minecraft/entity/passive/RabbitEntity;interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void injectTrustOnFeeding(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        RabbitEntity rabbit = ((RabbitEntity) (Object) this);
        if (rabbit.isBreedingItem(itemStack) && !this.isTrusting()) {
            ((AnimalEntityInvoker) rabbit).invokeEat(player, hand, itemStack);
            if (!rabbit.getWorld().isClient) {
                this.setTrusting(true);
                this.showEmoteParticle(true);
                rabbit.getWorld().sendEntityStatus(this, (byte) 41);
            }

            cir.setReturnValue(ActionResult.success(rabbit.getWorld().isClient));
        }
    }

    @Inject(method = "handleStatus", at = @At("HEAD"))
    private void handleTrustStatus(byte status, CallbackInfo ci) {
        if (status == 41) {
            this.showEmoteParticle(true);
        }
    }

    @Inject(method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/RabbitEntity;", at = @At(value = "TAIL"), cancellable = true)
    void babyRabbitsTrustPlayers(ServerWorld serverWorld, PassiveEntity passiveEntity, CallbackInfoReturnable<RabbitEntity> cir) {
        RabbitEntity baby = EntityType.RABBIT.create(serverWorld);
        if (baby != null && ((RabbitEntityTrust) passiveEntity).maddysmanyminiaturemodifications$isTrusting() && ((RabbitEntityTrust) this).maddysmanyminiaturemodifications$isTrusting()) { // baby is real, both parents trust player
            ((RabbitEntityTrust) baby).maddysmanyminiaturemodifications$setTrusting(true);
        }
        cir.setReturnValue(baby);
    }

    @ModifyConstant(method = "createRabbitAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", constant = @Constant(doubleValue = 3.0, ordinal = 0))
    private static double buffRabbitHealth(double health) {
        return 6.0;
    }

    @ModifyConstant(method = "initGoals()V", constant = @Constant(doubleValue = 2.2, ordinal = 0))
    private double rabbitsEscapeDangerFaster(double constant) {
        return 3.3;
    }

    @Intrinsic
    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return super.computeFallDamage(fallDistance, damageMultiplier);
    }

    @Inject(method = "Lnet/minecraft/entity/passive/RabbitEntity;computeFallDamage(FF)I", at = @At("RETURN"), cancellable = true)
    private void injectFallResist(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() - 5);
    }

    @Inject(method = "initGoals()V", at = @At("TAIL"))
    private void fleeFromFoxesAndCats(CallbackInfo ci) {
        ((RabbitEntity) (Object) this).goalSelector.add(4, new RabbitEntity.FleeGoal<>(((RabbitEntity) (Object) this), FoxEntity.class, 10.0F, 3.3, 3.3));
        ((RabbitEntity) (Object) this).goalSelector.add(4, new RabbitEntity.FleeGoal<>(((RabbitEntity) (Object) this), CatEntity.class, 10.0F, 3.3, 3.3));
    }

    @Inject(method = "createRabbitAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", at = @At(value = "RETURN"), cancellable = true)
    private static void buffStepHeight(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue().add(EntityAttributes.GENERIC_STEP_HEIGHT, (double) 1.0f));
    }

    static {
        NOTICEABLE_PLAYER_FILTER = (entity) -> !entity.isSneaky() && EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(entity);
    }
}
