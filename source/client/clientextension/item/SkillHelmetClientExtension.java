package net.tslat.aoa3.client.clientextension.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.tslat.aoa3.client.model.armor.AoAMiscModels;
import net.tslat.aoa3.client.player.ClientPlayerDataManager;
import net.tslat.aoa3.content.item.armour.SkillHelmet;
import net.tslat.aoa3.player.skill.AoASkill;
import org.jetbrains.annotations.NotNull;

public class SkillHelmetClientExtension implements IClientItemExtensions {
    @NotNull
    @Override
    public Model getGenericArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> defaultModel) {
        AoASkill skill = ((SkillHelmet)stack.getItem()).getSkill();

        return AoAMiscModels.getSkillHelmetModel(skill, entity instanceof Player && ClientPlayerDataManager.get().getSkill(skill).hasLevel(1000), defaultModel);
    }
}
