package moze_intel.projecte.emc.json;

import java.util.function.Consumer;
import javax.annotation.Nonnull;
import moze_intel.projecte.PECore;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class NSSItem implements NSSTag {

	//TODO: Make private
	@Nonnull
	public final ResourceLocation itemName;
	private final boolean isTag;

	private NSSItem(@Nonnull ResourceLocation tagId, boolean isTag) {
		itemName = tagId;
		this.isTag = isTag;
	}

	@Nonnull
	public static NSSItem createItem(@Nonnull Block block) {
		//TODO: Should we check for air
		//This should never be null or it would have crashed on being registered
		return createItem(block.getRegistryName());
	}

	@Nonnull
	public static NSSItem createItem(@Nonnull Item item) {
		//TODO: Should we check for air
		//This should never be null or it would have crashed on being registered
		return createItem(item.getRegistryName());
	}

	@Nonnull
	public static NSSItem createItem(@Nonnull ItemStack stack) {
		if (stack.isEmpty()) {
			throw new IllegalArgumentException("Can't make NSSItem with empty stack");
		}
		return createItem(stack.getItem());
	}

	@Nonnull
	public static NSSItem createItem(@Nonnull ResourceLocation tagId) {
		return new NSSItem(tagId, false);
	}

	@Nonnull
	public static NSSItem createTag(@Nonnull ResourceLocation tagId) {
		return new NSSItem(tagId, true);
	}

	@Nonnull
	public static NSSItem createTag(@Nonnull Tag<Item> tag) {
		//TODO: Decide NSSItem we want to store it as a tag or the resource location of the tag
		return createTag(tag.getId());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof NSSItem) {
			NSSItem other = (NSSItem) o;
			return isTag == other.isTag && itemName.equals(other.itemName);
		}
		return false;
	}

	@Override
	public String json() {
		if (isTag) {
			return "#" + itemName;
		}
		return itemName.toString();
	}

	@Override
	public int hashCode() {
		if (isTag) {
			return 31 + itemName.hashCode();
		}
		return itemName.hashCode();
	}

	@Override
	public String toString() {
		if (isTag) {
			return "Item Tag: " + itemName;
		}
		return "Item: " + itemName;
	}

	@Override
	public void forEachElement(Consumer<NormalizedSimpleStack> consumer) {
		if (isTag) {
			Tag<Item> tag = ItemTags.getCollection().get(itemName);
			if (tag == null) {
				PECore.LOGGER.warn("Couldn't find item tag {}", itemName);
			} else {
				tag.getAllElements().stream().map(NSSItem::createItem).forEach(consumer);
			}
		}
	}
}
