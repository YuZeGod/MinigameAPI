package net.afyer.minigameapi.util;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder
{
    private ItemStack itemStack;

    public ItemBuilder(Material material)
    {
        this(material, 1);
    }

    public ItemBuilder(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material, int amount)
    {
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, int amount, byte durability)
    {
        this.itemStack = new ItemStack(material, amount, durability);
    }

    @Override public ItemBuilder clone()
    {
        return new ItemBuilder(itemStack);
    }

    public ItemBuilder setDurability(short durability)
    {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable)
    {
        ItemMeta meta = itemStack.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setDisplayName(String name)
    {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level)
    {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment)
    {
        itemStack.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner)
    {
        SkullMeta im = (SkullMeta) itemStack.getItemMeta();
        im.setOwner(owner);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level)
    {
        ItemMeta im = itemStack.getItemMeta();
        im.addEnchant(enchantment, level, true);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder setInfinityDurability()
    {
        itemStack.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder setLore(String... lore)
    {
        ItemMeta im = itemStack.getItemMeta();
        List<String> lores = new ArrayList<String>();
        for (String line : lore)
        {
            lores.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        im.setLore(lores);
        itemStack.setItemMeta(im);
        return this;
    }

    @SuppressWarnings("deprecation") public ItemBuilder setDyeColor(DyeColor color)
    {
        itemStack.setDurability(color.getData());
        return this;
    }

    public ItemStack build()
    {
        return itemStack;
    }
}
