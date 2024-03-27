package fl205.betterthanbarrels.blocks;

import fl205.betterthanbarrels.tileEntities.TileEntityWoodenBarrel;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.lwjgl.util.vector.Vector2f;

public class WoodenBarrel extends BlockTileEntityRotatable {

	public WoodenBarrel(String key, int id, Material material) {
		super(key, id, material);
	}


	@Override
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		dropBarrelContent(world, x, y, z);
		super.onBlockRemoved(world, x, y, z, data);
	}

	// Left Click
	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		int itemCount;
		if (!player.isSneaking())
			itemCount = 1;
		else
			itemCount = 64;

		TileEntityWoodenBarrel barrel = (TileEntityWoodenBarrel)world.getBlockTileEntity(x, y, z);

		Vector2f barrelPos = new Vector2f((float) (barrel.x + 0.5), (float) (barrel.z + 0.5));
		Vector2f playerPos = new Vector2f((float) player.x, (float) player.z);
		Vector2f itemPos = Vector2f.sub(playerPos, barrelPos, null);
		itemPos.normalise();
		itemPos.scale(0.8F / Math.max(Math.abs(itemPos.x), Math.abs(itemPos.y)));
		Vector2f.add(barrelPos, itemPos, itemPos);

		if (barrel.getStackInSlot(0) != null) {
			EntityItem item = world.dropItem((int) itemPos.x, (int) (barrel.y + 0.5), (int) itemPos.y, barrel.decrStackSize(0, itemCount));
			item.x = itemPos.x;
			item.y = (barrel.y + 0.5);
			item.z = itemPos.y;
			item.xd = (itemPos.x - barrelPos.x) / 2;
			item.yd = 0;
			item.zd = (itemPos.y - barrelPos.y) / 2;
			item.delayBeforeCanPickup = 0;
		}
	}

	// Right Click
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

			transferItemStack(barrel, player, player.inventory.currentItem);
			barrel.setTimer(20);

		} else {

			if (barrel.readTimer() <= 0)
				return;

			for (int i = 0; i < player.inventory.mainInventory.length; i++) {
				if (player.inventory.mainInventory[i] == null)
					continue;

				if (player.inventory.mainInventory[i].itemID == barrel.getStackInSlot(0).itemID) {
					if (!transferItemStack(barrel, player, i))
						return;
				}
			}
		}
	}

	private boolean transferItemStack(TileEntityWoodenBarrel barrel, EntityPlayer player, int inventoryIndex) {
		ItemStack playersItemStack = player.inventory.mainInventory[inventoryIndex];
		ItemStack barrelsItemStack = barrel.getStackInSlot(0);
		int maxCanFit = barrel.getInventoryStackLimit();

		if (barrelsItemStack != null) {
			if (playersItemStack.itemID != barrelsItemStack.itemID)
				return false;

			maxCanFit -= barrelsItemStack.stackSize;
		}

		int itemCountToReceive = playersItemStack.stackSize;
		if (itemCountToReceive > maxCanFit)
			itemCountToReceive = maxCanFit;

		if (itemCountToReceive == 0)
			return false;

		ItemStack newPlayersItemStack = null;

		if (itemCountToReceive < playersItemStack.stackSize) {
			newPlayersItemStack = playersItemStack;
			newPlayersItemStack.stackSize = playersItemStack.stackSize - itemCountToReceive;
		}

		ItemStack newBarrelsItemStack = player.inventory.mainInventory[inventoryIndex].copy();
		newBarrelsItemStack.stackSize = itemCountToReceive;
		if (barrelsItemStack != null)
			newBarrelsItemStack.stackSize += barrelsItemStack.stackSize;

		barrel.setInventorySlotContents(0, newBarrelsItemStack);
		player.inventory.mainInventory[inventoryIndex] = newPlayersItemStack;

		return true;
	}
}
