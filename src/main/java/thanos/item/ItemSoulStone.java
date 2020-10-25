package thanos.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;

public class ItemSoulStone extends Item {
	public ItemSoulStone() {
		super();
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		if(entityItem.world.isRemote) {
			if(entityItem.ticksExisted % 2 == 0) {
				entityItem.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, entityItem.posX, entityItem.posY + 0.5f, entityItem.posZ, (itemRand.nextInt(2) - 0.5f) / 4, (itemRand.nextInt(2) - 0.5f) / 4, (itemRand.nextInt(2) - 0.5f) / 4);
			}
		}
		
		return super.onEntityItemUpdate(entityItem);
	}
}