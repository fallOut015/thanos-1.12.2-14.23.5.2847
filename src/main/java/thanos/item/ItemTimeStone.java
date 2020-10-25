package thanos.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import thanos.entity.effect.EntityMandala;

public class ItemTimeStone extends Item {
	public ItemTimeStone() {
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote) {
			EntityMandala mandala = new EntityMandala(worldIn, playerIn);
			mandala.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
			worldIn.spawnEntity(mandala);
			System.out.println("right clicked time stone");			
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
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