package fr.maxlego08.head.api;

import org.bukkit.inventory.ItemStack;

public interface HeadSignature {

    String KEY = "HEAD-ITEM";

    ItemStack sign(ItemStack itemStack, Head head);

    String getHeadId(ItemStack itemStack);

}
