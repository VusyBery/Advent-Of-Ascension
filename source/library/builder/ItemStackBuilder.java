package net.tslat.aoa3.library.builder;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.util.RegistryUtil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class ItemStackBuilder {
	private final Item item;
	private int count = 1;
	private int damage = 0;
	private List<Pair<DataComponentType<?>, Object>> components = null;

	public ItemStackBuilder(ItemLike item) {
		this.item = item.asItem();
	}

	public ItemStackBuilder(ResourceLocation itemId) {
		this.item = AoARegistries.ITEMS.getEntry(itemId);
	}

	public ItemStackBuilder count(int count) {
		this.count = count;

		return this;
	}

	public ItemStackBuilder damage(int damage) {
		if (this.item.components().has(DataComponents.UNBREAKABLE))
			throw new IllegalArgumentException("Can't set damage for undamageable item " + RegistryUtil.getId(this.item));

		this.damage = damage;

		return this;
	}

	public ItemStackBuilder withRandomDamage() {
		return damage(ThreadLocalRandom.current().nextInt(0, this.item.components().getOrDefault(DataComponents.MAX_DAMAGE, 0)));
	}

	public ItemStackBuilder usesRemaining(int uses) {
		return damage(this.item.components().getOrDefault(DataComponents.MAX_DAMAGE, 0) - uses);
	}

	public <T> ItemStackBuilder component(DataComponentType<T> component, T value) {
		if (this.components == null)
			this.components = new ObjectArrayList<>();

		this.components.add(Pair.of(component, value));

		return this;
	}

	public ItemStack build() {
		ItemStack stack = new ItemStack(this.item, this.count);

		if (this.damage > 0)
			stack.setDamageValue(this.damage);

		if (this.components != null) {
			for (Pair<DataComponentType<?>, Object> component : this.components) {
				stack.set((DataComponentType)component.left(), component.right());
			}
		}

		return stack;
	}
}
