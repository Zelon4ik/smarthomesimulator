package smarthomesimulator.presentation;

import java.util.EnumSet;
import java.util.Set;

public enum Role {
    ADMIN(EnumSet.allOf(Permission.class)),

    OPERATOR(EnumSet.of(
        Permission.USERS_READ,
        Permission.DEVICES_READ, Permission.DEVICES_CREATE, Permission.DEVICES_UPDATE, Permission.DEVICES_DELETE, Permission.DEVICES_TOGGLE,
        Permission.ROOMS_READ, Permission.ROOMS_CREATE, Permission.ROOMS_UPDATE, Permission.ROOMS_DELETE,
        Permission.SCENES_READ, Permission.SCENES_CREATE, Permission.SCENES_UPDATE, Permission.SCENES_DELETE, Permission.SCENES_ACTIVATE,
        Permission.TRIGGERS_READ, Permission.TRIGGERS_CREATE, Permission.TRIGGERS_UPDATE, Permission.TRIGGERS_DELETE,
        Permission.ENERGYLOGS_READ, Permission.ENERGYLOGS_CREATE, Permission.ENERGYLOGS_UPDATE, Permission.ENERGYLOGS_DELETE,
        Permission.DOCUMENTS_EXPORT
    )),

    VIEWER(EnumSet.of(
        Permission.USERS_READ,
        Permission.DEVICES_READ,
        Permission.ROOMS_READ,
        Permission.SCENES_READ,
        Permission.TRIGGERS_READ,
        Permission.ENERGYLOGS_READ
    )),

    GUEST(EnumSet.noneOf(Permission.class));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean has(Permission permission) {
        return permissions.contains(permission);
    }

    @Override
    public String toString() {
        switch (this) {
            case ADMIN:
                return "Адміністратор";
            case OPERATOR:
                return "Оператор";
            case VIEWER:
                return "Переглядач";
            case GUEST:
                return "Гість";
            default:
                return name();
        }
    }
}
