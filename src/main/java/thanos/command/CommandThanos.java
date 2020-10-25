package thanos.command;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import thanos.entity.effect.EntityShip;

public class CommandThanos extends CommandBase {
	@Override
	public String getName() {
		return "thanos";
	}
	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.thanos.usage";
	}
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length < 3) {
            throw new WrongUsageException("commands.thanos.usage", new Object[0]);
		}
		CommandBase.CoordinateArg xdest = parseCoordinate(sender.getPosition().getX(), args[0], true);
		CommandBase.CoordinateArg ydest = parseCoordinate(sender.getPosition().getY(), args[1], 0, 4096, false);
		CommandBase.CoordinateArg zdest = parseCoordinate(sender.getPosition().getZ(), args[2], true);
		
		EntityShip ship = new EntityShip(server.getEntityWorld());
		ship.setPosition(sender.getCommandSenderEntity().posX + 128, 255, sender.getCommandSenderEntity().posZ + 128);
		ship.setStart(sender.getCommandSenderEntity().posX + 128, 255, sender.getCommandSenderEntity().posZ + 128);
		ship.setEnd(xdest.getResult(), ydest.getResult(), zdest.getResult());
		server.getEntityWorld().spawnEntity(ship);
	}
	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return args.length > 0 && args.length <= 4 ? getTabCompletionCoordinate(args, 1, targetPos) : Collections.emptyList();
    }
}