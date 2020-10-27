package thanos;

import com.commodorethrawn.revivemod.common.handler.AltarHandler;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thanos.client.renderer.entity.RenderMandala;
import thanos.client.renderer.entity.RenderPortal;
import thanos.client.renderer.entity.RenderShip;
import thanos.client.renderer.entity.RenderThanos;
import thanos.command.CommandSnap;
import thanos.command.CommandThanos;
import thanos.entity.boss.EntityThanos;
import thanos.entity.effect.EntityMandala;
import thanos.entity.effect.EntityPortal;
import thanos.entity.effect.EntityShip;
import thanos.init.EntityEntries;
import thanos.init.ItemsThanos;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, dependencies = Main.DEPENDENCIES)
public class Main {
    public static final String MODID = "thanos";
    public static final String NAME = "Thanos";
    public static final String VERSION = "1.0";
    public static final String DEPENDENCIES = "required-after:worldedit;required-after:teamsmod;required-after:revivemod";

    @ObjectHolder("thanos:snap")
    public static final SoundEvent SNAP = new SoundEvent(new ResourceLocation(MODID, "snap")).setRegistryName(new ResourceLocation(MODID, "snap"));
    @ObjectHolder("thanos:land")
    public static final SoundEvent LAND = new SoundEvent(new ResourceLocation(MODID, "land")).setRegistryName(new ResourceLocation(MODID, "land"));

	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            sideClient(event);
        } catch(NoSuchMethodError error) {
        	event.getModLog().error(error + " THIS IS SUPPOSED TO HAPPEN");
        }
    }
	@SideOnly(Side.CLIENT)
	void sideClient(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityMandala.class, RenderMandala::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPortal.class, RenderPortal::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityShip.class, RenderShip::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityThanos.class, RenderThanos::new);
	}
	
    @EventHandler
    public static void serverInit(FMLServerStartingEvent event) {
    	event.registerServerCommand(new CommandThanos());
    	event.registerServerCommand(new CommandSnap());
    }
    
    static void registerRender(Item item) {
    	ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
    
    @EventBusSubscriber
    public static class RegsistryEvents {
    	@SubscribeEvent
    	public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
    		ItemsThanos.onItemsRegistry(itemRegistryEvent);
    	}
    	@SubscribeEvent
    	public static void onEntityEntriesRegistry(final RegistryEvent.Register<EntityEntry> entityEntryRegistryEvent) {
    		EntityEntries.onEntityEntriesRegistry(entityEntryRegistryEvent);
    	}
    	@SubscribeEvent
    	public static void onSoundEventsRegistry(final RegistryEvent.Register<SoundEvent> soundEventRegistryEvent) {
    		soundEventRegistryEvent.getRegistry().register(SNAP);
    		soundEventRegistryEvent.getRegistry().register(LAND);
    	}
    	@SubscribeEvent
        public static void registerRenders(ModelRegistryEvent event) {
        	registerRender(ItemsThanos.MIND_STONE);
        	registerRender(ItemsThanos.POWER_STONE);
        	registerRender(ItemsThanos.REALITY_STONE);
        	registerRender(ItemsThanos.SOUL_STONE);
        	registerRender(ItemsThanos.SPACE_STONE);
        	registerRender(ItemsThanos.TIME_STONE);
        }
    	@SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    		if(event.getModID().equals(MODID)) {
                ConfigManager.sync(MODID, Config.Type.INSTANCE);
            }
        }
    }
    
    public static void tryDoRespawn(World world) {
    	for(EntityPlayer playerIn : world.playerEntities) {
    		if(playerIn.isSpectator() && playerIn instanceof EntityPlayerMP) {
    			AltarHandler.doRevive(world, (EntityPlayerMP) playerIn);
    		}
    	}			
    }
}

// TODO sync lightning hits
// TODO thanos attack speed faster
// TODO mandala particles
// TODO remove debugs