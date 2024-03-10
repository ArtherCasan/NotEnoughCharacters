package net.moecraft.nechar;

import static net.moecraft.nechar.NotEnoughCharacters.CONTEXT;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import codechicken.nei.api.ItemFilter;
import codechicken.nei.api.ItemInfo;
import cpw.mods.fml.common.registry.GameRegistry;

public class NecharSearchFilter implements ItemFilter {

    protected boolean invalid = false;

    protected String mod = null;
    protected String id = null;
    protected String dict = null;

    protected List<String> keywords = new LinkedList<>();

    public NecharSearchFilter(String searchText) {
        String[] pieces = searchText.split("\\s+");

        for (String piece : pieces) {
            if (piece.isEmpty()) continue;

            switch (piece.charAt(0)) {
                case '@':
                    if (mod == null) {
                        mod = piece.substring(1)
                            .toLowerCase();
                        continue;
                    } else {
                        invalid = true;
                        return;
                    }

                case '&':
                    if (id == null) {
                        id = piece.substring(1)
                            .toLowerCase();
                        continue;
                    } else {
                        invalid = true;
                        return;
                    }

                case '$':
                    if (dict == null) {
                        dict = piece.substring(1)
                            .toLowerCase();
                        continue;
                    } else {
                        invalid = true;
                        return;
                    }
            }

            keywords.add(piece);
        }
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        return !invalid && matchesMod(itemStack)
            && matchesId(itemStack)
            && matchesDict(itemStack)
            && matchesKeywords(itemStack);
    }

    protected boolean matchesMod(ItemStack itemStack) {
        if (mod == null || mod.isEmpty()) {
            return true;
        }

        GameRegistry.UniqueIdentifier itemId = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());

        return itemId != null && itemId.modId.toLowerCase()
            .contains(mod);
    }

    protected boolean matchesId(ItemStack itemStack) {
        if (id == null || id.isEmpty()) {
            return true;
        }

        String itemId = Item.itemRegistry.getNameForObject(itemStack.getItem());

        return itemId.toLowerCase()
            .contains(id);
    }

    protected boolean matchesDict(ItemStack itemStack) {
        if (dict == null || dict.isEmpty()) {
            return true;
        }

        int[] oreIds = OreDictionary.getOreIDs(itemStack);

        for (int id : oreIds) {
            if (OreDictionary.getOreName(id)
                .toLowerCase()
                .contains(dict)) return true;
        }

        return false;
    }

    protected boolean matchesKeywords(ItemStack itemStack) {
        if (keywords == null || keywords.isEmpty()) {
            return true;
        }

        String name = ItemInfo.getSearchName(itemStack);
        for (String keyword : keywords) {
            if (!CONTEXT.contains(name, keyword)) {
                return false;
            }
        }

        return true;
    }

}
