package net.tslat.aoa3.content.item.tool.artifice;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.library.object.CachedEntity;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.PlayerUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.effectslib.api.particle.transitionworker.PositionParticleTransition;
import net.tslat.effectslib.networking.packet.TELParticlePacket;
import net.tslat.smartbrainlib.util.RandomUtil;

import java.util.List;

public class StasisCapsule extends ArtificeItem {
    public StasisCapsule() {
        super(new Properties().stacksTo(1).component(AoADataComponents.STORED_ENTITY, CachedEntity.EMPTY));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (!stack.get(AoADataComponents.STORED_ENTITY).isEmpty() || !canCapture(player, interactionTarget))
            return InteractionResult.FAIL;

        if (!player.level().isClientSide) {
            TELParticlePacket packet = new TELParticlePacket();

            packet.particle(ParticleBuilder.forRandomPosAtBoundsEdge(ParticleTypes.END_ROD, interactionTarget.getBoundingBox().inflate(Math.max(interactionTarget.getBbHeight(), interactionTarget.getBbWidth()) / 2f))
                    .spawnNTimes(200)
                    .addTransition(PositionParticleTransition.create(interactionTarget.position().add(0, interactionTarget.getBbHeight() * 0.5f, 0), 5))
                    .colourOverride(0.65f, 0, 0.15f, 1f)
                    .lifespan(10));
            packet.particle(ParticleBuilder.forRandomPosAtBoundsEdge(ParticleTypes.ENCHANTED_HIT, interactionTarget.getBoundingBox().inflate(Math.max(interactionTarget.getBbHeight(), interactionTarget.getBbWidth()) / 2f))
                    .spawnNTimes(200)
                    .lifespan(10)
                    .colourOverride(1f, 1f, 1f, 1f)
                    .addTransition(PositionParticleTransition.create(interactionTarget.position().add(0, interactionTarget.getBbHeight() * 0.5f, 0), 5)));

            for (int i = 0; i < 10; i++) {
                packet.particle(ParticleBuilder.forPositions(ParticleTypes.ELECTRIC_SPARK, interactionTarget.position().add(0, interactionTarget.getBbHeight() * 0.5f, 0))
                        .spawnNTimes(10)
                        .velocity(RandomUtil.randomScaledGaussianValue(0.05f), RandomUtil.randomScaledGaussianValue(0.05f), RandomUtil.randomScaledGaussianValue(0.05f))
                        .scaleMod(0.25f)
                        .lifespan(RandomUtil.randomNumberBetween(20, 35)));
            }

            packet.sendToAllPlayersTrackingEntity((ServerLevel)player.level(), interactionTarget);

            player.level().playSound(null, interactionTarget.getX(), interactionTarget.getY(), interactionTarget.getZ(), SoundEvents.BREEZE_WIND_CHARGE_BURST, SoundSource.PLAYERS, 0.5f, (float)RandomUtil.randomValueBetween(0.9f, 1.1f));
            player.level().playSound(null, interactionTarget.getX(), interactionTarget.getY(), interactionTarget.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5f, (float)RandomUtil.randomValueBetween(0.9f, 1.1f));
            stack.set(AoADataComponents.STORED_ENTITY, CachedEntity.store(interactionTarget));
            player.setItemInHand(usedHand, stack);
            interactionTarget.discard();
        }

        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        CachedEntity<?> cachedEntity = context.getItemInHand().get(AoADataComponents.STORED_ENTITY);

        if (cachedEntity.isEmpty())
            return InteractionResult.FAIL;

        Player player = context.getPlayer();
        Level level = context.getLevel();
        Vec3 pos = context.getClickLocation();

        if (!level.noCollision(cachedEntity.entityType().getSpawnAABB(pos.x, pos.y, pos.z))) {
            if (!level.isClientSide())
                PlayerUtil.notifyPlayer(player, LocaleUtil.getLocaleMessage(LocaleUtil.createFeedbackLocaleKey("spawnEntity.noSpace"), ChatFormatting.RED));

            return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            Entity entity = cachedEntity.createEntity(level);

            if (entity == null) {
                PlayerUtil.notifyPlayer(player, LocaleUtil.getLocaleMessage(LocaleUtil.createFeedbackLocaleKey("spawnEntity.fail"), ChatFormatting.RED));

                return InteractionResult.FAIL;
            }

            entity.setPos(pos);
            level.addFreshEntity(entity);
            context.getItemInHand().set(AoADataComponents.STORED_ENTITY, CachedEntity.EMPTY);

            TELParticlePacket packet = new TELParticlePacket();

            for (int i = 0; i < 50; i++) {
                packet.particle(ParticleBuilder.forRandomPosInSphere(ParticleTypes.FALLING_SPORE_BLOSSOM, entity.position().add(0, entity.getBbHeight() * 0.5f, 0), 0.25f)
                        .colourOverride(0.8f, 0.3f, 0.3f, 1f)
                        .velocity(RandomUtil.randomScaledGaussianValue(0.05f), RandomUtil.randomScaledGaussianValue(0.05f), RandomUtil.randomScaledGaussianValue(0.05f))
                        .lifespan(RandomUtil.randomNumberBetween(20, 35)));
            }

            packet.sendToAllPlayersTrackingEntity((ServerLevel)level, player);
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.8f, 1.8f);
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1, 1.8f);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static boolean canCapture(Player player, LivingEntity entity) {
        if (entity instanceof OwnableEntity ownable && (player.getUUID().equals(ownable.getOwnerUUID()) || ownable.getOwnerUUID() == null))
            return true;

        if (entity instanceof Animal && (!(entity instanceof NeutralMob neutral) || !neutral.isAngryAt(player)))
            return true;

        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        CachedEntity<?> cachedEntity = stack.get(AoADataComponents.STORED_ENTITY);

        if (cachedEntity.isEmpty()) {
            tooltipComponents.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.ITEM_TYPE_INFO, 1));
        }
        else {
            tooltipComponents.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.SPECIAL, 2, cachedEntity.getName(context.registries())));
        }
    }
}