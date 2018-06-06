package net.afyer.minigameapi.tellraw;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation") public abstract class ItemSerialize
{
    static ItemSerialize itemSerialize;

    static
    {
        try
        {
            itemSerialize = new Automatic();
        }
        catch (IllegalStateException e)
        {
            itemSerialize = new Manual();
        }
    }

    public static String $(ItemStack item)
    {
        return itemSerialize.parse(item);
    }

    public abstract String parse(ItemStack paramItemStack);

    public abstract String getName();

    static class Automatic extends ItemSerialize
    {
        private static boolean inited = false;
        private static Method asNMSCopyMethod;
        private static Method nmsSaveNBTMethod;
        private static Class<?> nmsNBTTagCompound;
        private static String ver = org.bukkit.Bukkit.getServer().getClass().getPackage()
                .getName().replace(".", ",").split(",")[3];

        static
        {
            try
            {
                Class<?> cis = getOBCClass("inventory.CraftItemStack");
                asNMSCopyMethod = cis
                        .getMethod("asNMSCopy", new Class[] { ItemStack.class });
                Class<?> nmsItemStack = asNMSCopyMethod.getReturnType();
                for (Method method : nmsItemStack.getMethods())
                {
                    Class<?> rt = method.getReturnType();
                    if ((method.getParameterTypes().length == 0) && ("NBTTagCompound"
                            .equals(rt.getSimpleName())))
                    {
                        nmsNBTTagCompound = rt;
                    }
                }
                for (Method method : nmsItemStack.getMethods())
                {
                    Class<?>[] paras = method.getParameterTypes();
                    Class<?> rt = method.getReturnType();
                    if ((paras.length == 1) && ("NBTTagCompound"
                            .equals(paras[0].getSimpleName())) && ("NBTTagCompound"
                            .equals(rt.getSimpleName())))
                    {
                        nmsSaveNBTMethod = method;
                    }
                }
                inited = true;
            }
            catch (ClassNotFoundException | NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }

        public Automatic()
        {
            if (!inited)
            {
                throw new IllegalStateException("无法初始化自动处理类!");
            }
        }

        @SuppressWarnings("rawtypes") private static Class getOBCClass(String cname)
                throws ClassNotFoundException
        {
            return Class.forName("org.bukkit.craftbukkit." + ver + "." + cname);
        }

        @Override public String getName()
        {
            return "Automatic";
        }

        @Override public String parse(ItemStack item)
        {
            try
            {
                return nmsSaveNBTMethod
                        .invoke(asNMSCopyMethod.invoke(null, new Object[] { item }),
                                new Object[] { nmsNBTTagCompound.newInstance() })
                        .toString();
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e)
            {
                itemSerialize = new Manual();
            }
            return itemSerialize.parse(item);
        }
    }

    static class Manual extends ItemSerialize
    {
        @Override public String getName()
        {
            return "Manual";
        }

        @Override public String parse(ItemStack item)
        {
            return serialize(item);
        }

        private String getDisplay(ItemMeta im)
        {
            StringBuilder display = new StringBuilder();
            display.append("{");
            if (im.hasDisplayName())
            {
                display.append(String.format("Name:\"%s\",",
                        new Object[] { im.getDisplayName() }));
            }
            if (im.hasLore())
            {
                display.append("Lore:[");
                int i = 0;
                for (String line : im.getLore())
                {
                    display.append(String.format("%s:\"%s\",",
                            new Object[] { Integer.valueOf(i),
                                    new JsonBuilder(line).toString() }));
                    i++;
                }
                display.deleteCharAt(display.length() - 1);
                display.append("],");
            }
            display.deleteCharAt(display.length() - 1);
            display.append("}");
            return display.toString();
        }

        private String getEnch(Set<Map.Entry<Enchantment, Integer>> set)
        {
            StringBuilder enchs = new StringBuilder();
            for (Map.Entry<Enchantment, Integer> ench : set)
            {
                enchs.append(String.format("{id:%s,lvl:%s},",
                        new Object[] { Integer.valueOf(ench.getKey().getId()),
                                ench.getValue() }));
            }
            enchs.deleteCharAt(enchs.length() - 1);
            return enchs.toString();
        }

        private String getTag(ItemMeta im)
        {
            StringBuilder meta = new StringBuilder("{");
            if (im.hasEnchants())
            {
                meta.append(String.format("ench:[%s],",
                        new Object[] { getEnch(im.getEnchants().entrySet()) }));
            }
            if ((im.hasDisplayName()) || (im.hasLore()))
            {
                meta.append(
                        String.format("display:%s,", new Object[] { getDisplay(im) }));
            }
            meta.deleteCharAt(meta.length() - 1);
            meta.append("}");
            return meta.toString();
        }

        private String serialize(ItemStack item)
        {
            StringBuilder json = new StringBuilder("{");
            json.append(String.format("id:\"%s\",Damage:\"%s\"",
                    new Object[] { Integer.valueOf(item.getTypeId()),
                            Short.valueOf(item.getDurability()) }));
            if (item.getAmount() > 1)
            {
                json.append(String.format(",Count:%s",
                        new Object[] { Integer.valueOf(item.getAmount()) }));
            }
            if (item.hasItemMeta())
            {
                json.append(String.format(",tag:%s",
                        new Object[] { getTag(item.getItemMeta()) }));
            }
            json.append("}");
            return json.toString();
        }
    }
}
