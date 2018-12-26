package moze_intel.projecte.gameObjs.items.rings;

import moze_intel.projecte.PECore;
import moze_intel.projecte.api.PESounds;
import moze_intel.projecte.api.item.IModeChanger;
import moze_intel.projecte.gameObjs.items.ItemPE;
import moze_intel.projecte.impl.MultiModeProvider;
import moze_intel.projecte.utils.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class RingToggle extends ItemPE
{
	public RingToggle(String unlocalName)
	{
		this.setTranslationKey(unlocalName);
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
		this.addPropertyOverride(ACTIVE_NAME, ACTIVE_GETTER);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return false;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		IModeChanger capImpl = new IModeChanger() {
			@Override
			public byte getMode()
			{
				return ItemHelper.getOrCreateCompound(stack).getBoolean(TAG_ACTIVE) ? (byte) 1 : 0;
			}

			@Override
			public boolean changeMode(@Nonnull EntityPlayer player, @Nullable EnumHand hand)
			{
				if (!ItemHelper.getOrCreateCompound(stack).getBoolean(TAG_ACTIVE))
				{
					player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, PESounds.HEAL, SoundCategory.PLAYERS, 1.0F, 1.0F);
					stack.getTagCompound().setBoolean(TAG_ACTIVE, true);
				}
				else
				{
					player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, PESounds.UNCHARGE, SoundCategory.PLAYERS, 1.0F, 1.0F);
					stack.getTagCompound().setBoolean(TAG_ACTIVE, false);
				}
				return true;
			}
		};

		return new MultiModeProvider(capImpl);
	}

	boolean isActive(ItemStack stack) {
		return stack.getCapability(PECore.MULTIMODE_CAP, null).getMode() == 1;
	}
}
