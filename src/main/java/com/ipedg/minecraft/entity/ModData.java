package com.ipedg.minecraft.entity;

import java.util.Objects;

public class ModData {
    private String Name;
    private String Version;

    @Override
    public boolean equals(Object o) {
        if (o==null)return false;
        if(!(o instanceof ModData))return false;
        ModData modData = (ModData) o;
        return modData.getName().equalsIgnoreCase(Name) &&
                modData.getVersion().equalsIgnoreCase(Version);
    }

    @Override
    public int hashCode() {
        return Name.hashCode()+Version.hashCode();
    }

    @Override
    public String toString() {
        return "ModData{" +
                "Name='" + Name + '\'' +
                ", Version='" + Version + '\'' +
                '}';
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public ModData(String name, String version) {
        Name = name;
        Version = version;
    }
}