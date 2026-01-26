package smarthomesimulator.presentation;

public final class AccessControl {
    private AccessControl() {}

    public static boolean can(Session session, Permission permission) {
        if (session == null) {
            return false;
        }
        return session.has(permission);
    }

    public static void require(Session session, Permission permission, String actionName) {
        if (!can(session, permission)) {
            throw new SecurityException("Доступ заборонено для дії: " + actionName);
        }
    }
}
