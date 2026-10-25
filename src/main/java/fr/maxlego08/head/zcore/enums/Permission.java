package fr.maxlego08.head.zcore.enums;

public enum Permission {

    HEAD_USE, HEAD_RELOAD, HEAD_HELP, HEAD_GIVE, HEAD_SEARCH, HEAD_INFO, HEAD_RANDOM, HEAD_SAVE, HEAD_MISCELLANEOUS, HEAD_PLANTS, HEAD_MONSTERS, HEAD_HUMANOID, HEAD_HUMANS, HEAD_FOOD_DRINKS, HEAD_DECORATION, HEAD_BLOCKS, HEAD_ANIMALS, HEAD_ALPHABET, HEAD_REFRESH, HEAD_PLAYER_HEAD, HEAD_PLAYER_HEAD_STATIC, HEAD_PLAYERS;

    private final String permission;

    Permission() {
        this.permission = this.name().toLowerCase().replace("_", ".");
    }

    public String getPermission() {
        return permission;
    }

}
