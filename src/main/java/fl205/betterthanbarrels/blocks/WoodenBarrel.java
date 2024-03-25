package fl205.betterthanbarrels.blocks;

import fl205.betterthanbarrels.tileEntities.TileEntityWoodenBarrel;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class WoodenBarrel extends BlockTileEntityRotatable {

	public WoodenBarrel(String key, int id, Material material) {
		super(key, id, material);
	}


	@Override
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		dropBarrelContent(world, x, y, z);
		super.onBlockRemoved(world, x, y, z, data);
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		TileEntityWoodenBarrel barrel = (TileEntityWoodenBarrel)world.getBlockTileEntity(x, y, z);
		if (!world.isClientSide) {
			receiveItems(barrel, player);
		}
		return true;
	}

	public static void dropBarrelContent(World world, int x, int y, int z) {
		TileEntityWoodenBarrel tileEntityWoodenBarrel = (TileEntityWoodenBarrel)world.getBlockTileEntity(x, y, z);
		if (tileEntityWoodenBarrel == null) {
			System.out.println("Can't drop barrel items because tile entity is null at x: " + x + " y:" + y + " z: " + z);
		} else {
			ItemStack itemStack = tileEntityWoodenBarrel.getStackInSlot(0);
			if (itemStack != null) {
				EntityItem item = world.dropItem(x, y, z, itemStack);
				item.xd *= 0.5;
				item.yd *= 0.5;
				item.zd *= 0.5;
				item.delayBeforeCanPickup = 0;
			}
		}
	}

	@Override
	protected TileEntity getNewBlockEntity() {
		return new TileEntityWoodenBarrel();
	}


	private void receiveItems(TileEntityWoodenBarrel barrel, EntityPlayer player) {
		if (player.getHeldItem() != null) {
			barrel.setInventorySlotContents(0, player.getHeldItem().copy());
			player.inventory.mainInventory[player.inventory.currentItem] = null;
		}
	}
}
