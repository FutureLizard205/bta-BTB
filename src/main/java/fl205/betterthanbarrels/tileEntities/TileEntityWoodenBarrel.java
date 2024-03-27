package fl205.betterthanbarrels.tileEntities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;

public class TileEntityWoodenBarrel extends TileEntity implements IInventory {
	private ItemStack barrelContents;

	private int timer;

	public TileEntityWoodenBarrel() {
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i == 0)
			return this.barrelContents;
		else
			return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.barrelContents = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	public String getInvName() {
		return "Wooden Barrel";
	}


	public ItemStack decrStackSize(int i, int j) {
		if (this.barrelContents != null) {
			ItemStack itemstack1;
			if (this.barrelContents.stackSize <= j) {
				itemstack1 = this.barrelContents;
				this.barrelContents = null;
				this.onInventoryChanged();
				return itemstack1;
			} else {
				itemstack1 = this.barrelContents.splitStack(j);
				if (this.barrelContents.stackSize <= 0) {
					this.barrelContents = null;
				}

				this.onInventoryChanged();
				return itemstack1;
			}
		} else {
			return null;
		}
	}



	public void readFromNBT(CompoundTag nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		ListTag nbttaglist = nbttagcompound.getList("Items");
		this.barrelContents = null;

		for(int i = 0; i < nbttaglist.tagCount(); ++i) {
			this.barrelContents = ItemStack.readItemStackFromNbt((CompoundTag)nbttaglist.tagAt(i));
		}
	}

	public void writeToNBT(CompoundTag nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		ListTag nbttaglist = new ListTag();

		if (this.barrelContents != null) {
			CompoundTag nbttagcompound1 = new CompoundTag();
			nbttagcompound1.putByte("Slot", (byte)0);
			this.barrelContents.writeToNBT(nbttagcompound1);
			nbttaglist.addTag(nbttagcompound1);
		}

		nbttagcompound.put("Items", nbttaglist);
	}

	public int getInventoryStackLimit() {
		return 64*64;
	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		if (this.worldObj.getBlockTileEntity(this.x, this.y, this.z) != this) {
			return false;
		} else {
			return entityplayer.distanceToSqr((double)this.x + 0.5, (double)this.y + 0.5, (double)this.z + 0.5) <= 64.0;
		}
	}

	@Override
	public void sortInventory() {}

	@Override
	public void tick() {
		if (timer > 0)
			timer--;
	}

	public void setTimer(int value) {
		timer = value;
	}

	public int readTimer() {
		return timer;
	}
}
