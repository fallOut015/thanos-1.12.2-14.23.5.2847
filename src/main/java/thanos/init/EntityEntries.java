package thanos.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import thanos.Main;
import thanos.entity.boss.EntityThanos;
import thanos.entity.effect.EntityMandala;
import thanos.entity.effect.EntityPortal;
import thanos.entity.effect.EntityShip;

@ObjectHolder(Main.MODID)
public class EntityEntries {
	@ObjectHolder("mandala")
	public static final EntityEntry MANDALA = EntityEntryBuilder.create().entity(EntityMandala.class).factory(EntityMandala::new).id(new ResourceLocation(Main.MODID, "mandala"), 1).name("mandala").tracker(128, 1, true).build();
	@ObjectHolder("portal")
	public static final EntityEntry PORTAL = EntityEntryBuilder.create().entity(EntityPortal.class).factory(EntityPortal::new).id(new ResourceLocation(Main.MODID, "portal"), 3).name("portal").tracker(128, 1, true).build();
	@ObjectHolder("ship")
	public static final EntityEntry SHIP = EntityEntryBuilder.create().entity(EntityShip.class).factory(EntityShip::new).id(new ResourceLocation(Main.MODID, "ship"), 4).name("ship").tracker(256, 1, true).build();
	@ObjectHolder("thanos")
	public static final EntityEntry THANOS = EntityEntryBuilder.create().entity(EntityThanos.class).factory(EntityThanos::new).id(new ResourceLocation(Main.MODID, "thanos"), 5).name("thanos").tracker(128, 1, true).build();
	
	public static void onEntityEntriesRegistry(final RegistryEvent.Register<EntityEntry> entityEntryRegistryEvent) {
		entityEntryRegistryEvent.getRegistry().register(MANDALA);
		entityEntryRegistryEvent.getRegistry().register(PORTAL);
		entityEntryRegistryEvent.getRegistry().register(SHIP);
		entityEntryRegistryEvent.getRegistry().register(THANOS);
	}
}