package thanos.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import thanos.entity.boss.EntityThanos;

public class CommandSnap extends CommandBase {
	@Override
	public String getName() {
		return "tsnap";
	}
	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.tsnap.usage";
	}
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0) {
            throw new WrongUsageException("commands.tsnap.usage", new Object[0]);
		}

		if(!sender.getEntityWorld().isRemote) {
			if(EntityThanos.THANI.size() > 0) {
				EntityThanos.THANI.sort((entity1, entity2) -> (int) (entity1.getDistance(sender.getCommandSenderEntity()) - entity2.getDistance(sender.getCommandSenderEntity())));
				EntityThanos.THANI.get(0).snap();
			}	
		}
	}
}