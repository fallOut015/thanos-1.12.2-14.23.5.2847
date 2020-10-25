package thanos.item;

import java.util.LinkedList;
import java.util.List;

import com.commodorethrawn.revivemod.common.handler.AltarHandler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import thanos.Main;
import thanos.init.ItemsThanos;

public class ItemMindStone extends Item {
	public ItemMindStone() {
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
		
		List<EntityItem> itemStackList = entityItem.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(AltarHandler.skullPos.add(0, -1, 0)).grow(3.5F));
		List<Item> itemsList = new LinkedList<Item>();
		itemStackList.forEach(itemStack -> itemsList.add(itemStack.getItem().getItem()));
		
		if(
			itemsList.contains(ItemsThanos.MIND_STONE) &&
			itemsList.contains(ItemsThanos.POWER_STONE) &&
			itemsList.contains(ItemsThanos.REALITY_STONE) &&
			itemsList.contains(ItemsThanos.SOUL_STONE) &&
			itemsList.contains(ItemsThanos.SPACE_STONE) &&
			itemsList.contains(ItemsThanos.TIME_STONE)
		) {
			Main.tryDoRespawn(entityItem.world);
			itemStackList.forEach(itemStack -> itemStack.setDead());
		}
		
		return super.onEntityItemUpdate(entityItem);
	}
}