package fr.skyfighttv.ffarush.Utils;

public enum Files {
    Config("config"),
    Kits("kits"),
    Lang("lang"),
    PlayerFile("PlayerFile"),
    Spawn("spawn");

    private final String name;

    Files(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
