package fl205.betterthanbarrels.blocks.metastates;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;

import java.util.HashMap;

public class WoodenBarrelMetaState extends MetaStateInterpreter {
	@Override
	public HashMap<String, String> getStateMap(WorldSource worldSource, int x, int y, int z, Block block, int meta) {
		int hRotation = meta & 0b11;
		HashMap<String, String> result = new HashMap<>();
		result.put("facing", new String[]{"west", "east", "north", "south"}[hRotation]);
		return result;
	}
}
