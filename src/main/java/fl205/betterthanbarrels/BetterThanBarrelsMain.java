package fl205.betterthanbarrels;

import fl205.betterthanbarrels.blocks.WoodenBarrel;
import fl205.betterthanbarrels.blocks.metastates.WoodenBarrelMetaState;
import fl205.betterthanbarrels.tileEntities.TileEntityWoodenBarrel;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.sound.block.BlockSounds;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.util.RecipeEntrypoint;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.BlockModelDragonFly;


public class BetterThanBarrelsMain implements ModInitializer, RecipeEntrypoint {
    public static final String MOD_ID = "betterthanbarrels";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Blocks

	public static final Block woodenBarrel = new BlockBuilder(MOD_ID)
		.setHardness(1.5F)
		.setBlockSound(BlockSounds.WOOD)
		//.setTopBottomTexture("woodenbarrelside.png")
		//.setSideTextures("woodenbarrelside.png")
		//.setNorthTexture("woodenbarrelfront.png")
		//.setSouthTexture("woodenbarrelback.png")
		.setTags(BlockTags.MINEABLE_BY_AXE)
		.setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID, "block/wooden_barrel.json"),
			ModelHelper.getOrCreateBlockState(MOD_ID, "wooden_barrel.json"), new WoodenBarrelMetaState(), true))
		.build(new WoodenBarrel("barrel.wooden", 664, Material.wood));


	@Override
	public void onRecipesReady() {

	}

	@Override
	public void onInitialize() {
		EntityHelper.Core.createTileEntity(TileEntityWoodenBarrel.class, "Wooden Barrel");
		LOGGER.info("Better Than Barrels! initialized.");
	}
}
