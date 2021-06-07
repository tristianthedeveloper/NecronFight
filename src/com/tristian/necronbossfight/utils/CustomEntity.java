package com.tristian.necronbossfight.utils;

import com.tristian.necronbossfight.mobs.WitherBoss;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum CustomEntity {
    WITHER("Wither", 64, EntityType.WITHER, (Class<? extends Entity>) EntityWither.class, WitherBoss.class),
    ;
    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends Entity> nmsClass;
    private Class<? extends EntityInsentient> customClass;

    private CustomEntity(final String name, final int id, final EntityType entityType, final Class<? extends Entity> nmsClass, final Class<? extends EntityInsentient> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Class<? extends Entity> getNMSClass() {
        return this.nmsClass;
    }

    public Class<? extends EntityInsentient> getCustomClass() {
        return this.customClass;
    }

    public static void registerEntities() {
        for (final CustomEntity entity : values()) {
            a(entity.getCustomClass(), entity.getName(), entity.getID());
        }
    }

    public void registerEntity() {
        a(this.getCustomClass(), this.getName(), this.getID());
    }

    public void unregisterEntity() {
        try {
            ((Map) getPrivateStatic(EntityTypes.class, "d")).remove(this.getCustomClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ((Map) getPrivateStatic(EntityTypes.class, "f")).remove(this.getCustomClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        a(this.getNMSClass(), this.getName(), this.getID());
    }

    public static void unregisterEntities() {
        for (final CustomEntity entity : values()) {
            try {
                ((Map) getPrivateStatic(EntityTypes.class, "d")).remove(entity.getCustomClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ((Map) getPrivateStatic(EntityTypes.class, "f")).remove(entity.getCustomClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (final CustomEntity entity : values()) {
            try {
                a(entity.getNMSClass(), entity.getName(), entity.getID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BiomeBase[] biomes;
        try {
            biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
        } catch (Exception exc) {
            return;
        }
        for (final BiomeBase biomeBase : biomes) {
            if (biomeBase == null) {
                break;
            }
            for (final String field : new String[]{"as", "at", "au", "av"}) {
                try {
                    final Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    final List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
                    for (final BiomeMeta meta : mobList) {
                        for (final CustomEntity entity2 : values()) {
                            if (entity2.getCustomClass().equals(meta.b)) {
                                meta.b = entity2.getNMSClass();
                            }
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private static Object getPrivateStatic(final Class<? extends Object> clazz, final String f) throws Exception {
        final Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }

    @Deprecated
    private static void a(final Class clazz, final String name, final int id) {
        try {
            ((Map) getPrivateStatic(EntityTypes.class, "c")).put(name, clazz);
            ((Map) getPrivateStatic(EntityTypes.class, "d")).put(clazz, name);
            ((Map) getPrivateStatic(EntityTypes.class, "e")).put(id, clazz);
            ((Map) getPrivateStatic(EntityTypes.class, "f")).put(clazz, id);
            ((Map) getPrivateStatic(EntityTypes.class, "g")).put(name, id);
        } catch (Exception ex) {
        }
    }

    public static void registerEntityNEW(final String name, final int id, final Class<? extends EntityInsentient> customClass) {
        try {
            final List<Map<?, ?>> dataMaps = new ArrayList<Map<?, ?>>();
            for (final Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMaps.add((Map<?, ?>) f.get(null));
                }
            }
            if (dataMaps.get(2).containsKey(id)) {
                dataMaps.get(0).remove(name);
                dataMaps.get(2).remove(id);
            }
            final Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, Integer.TYPE);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
