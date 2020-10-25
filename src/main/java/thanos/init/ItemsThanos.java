package thanos.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import thanos.Main;
import thanos.item.ItemMindStone;
import thanos.item.ItemPowerStone;
import thanos.item.ItemRealityStone;
import thanos.item.ItemSoulStone;
import thanos.item.ItemSpaceStone;
import thanos.item.ItemTimeStone;

@ObjectHolder(Main.MODID)
public class ItemsThanos {
	@ObjectHolder("mind_stone")
	public static final Item MIND_STONE = new ItemMindStone().setCreativeTab(CreativeTabs.MISC).setMaxStackSize(1).setRegistryName(Main.MODID, "mind_stone").setUnlocalizedName("mind_stone");
	@ObjectHolder("power_stone")
	public static final Item POWER_STONE = new ItemPowerStone().setCreativeTab(CreativeTabs.MISC).setMaxStackSize(1).setRegistryName(Main.MODID, "power_stone").setUnlocalizedName("power_stone");
	@ObjectHolder("reality_stone")
	public static final Item REALITY_STONE = new ItemRealityStone().setCreativeTab(CreativeTabs.MISC).setMaxStackSize(1).setRegistryName(Main.MODID, "reality_stone").setUnlocalizedName("reality_stone");
	@ObjectHolder("soul_stone")
	public static final Item SOUL_STONE = new ItemSoulStone().setCreativeTab(CreativeTabs.MISC).setMaxStackSize(1).setRegistryName(Main.MODID, "soul_stone").setUnlocalizedName("soul_stone");
	@ObjectHolder("space_stone")
	public static final Item SPACE_STONE = new ItemSpaceStone().setCreativeTab(CreativeTabs.MISC).setMaxStackSize(1).setRegistryName(Main.MODID, "space_stone").setUnlocalizedName("space_stone");
	@ObjectHolder("time_stone")
	public static final Item TIME_STONE = new ItemTimeStone().setCreativeTab(CreativeTabs.MISC).setMaxStackSize(1).setRegistryName(Main.MODID, "time_stone").setUnlocalizedName("time_stone");

	public static void onItemsRegistry(RegistryEvent.Register<Item> itemRegistryEvent) {
		itemRegistryEvent.getRegistry().register(MIND_STONE);
		itemRegistryEvent.getRegistry().register(POWER_STONE);
		itemRegistryEvent.getRegistry().register(REALITY_STONE);
		itemRegistryEvent.getRegistry().register(SOUL_STONE);
		itemRegistryEvent.getRegistry().register(SPACE_STONE);
		itemRegistryEvent.getRegistry().register(TIME_STONE);
	}
}