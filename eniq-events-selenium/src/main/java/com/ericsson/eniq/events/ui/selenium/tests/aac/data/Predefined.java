package com.ericsson.eniq.events.ui.selenium.tests.aac.data;

public abstract class Predefined {

    public static final PermissionGroup ALLPERMISSIONS_GROUP = new PermissionGroup("allpermissions", "All Permissions",
            "This permission group contains all UI permissions", PermissionGroup.Permission.NETWORK.getPermissionName()
                    + "," + PermissionGroup.Permission.RANKING.getPermissionName() + ","
                    + PermissionGroup.Permission.SUBSCRIBER.getPermissionName() + ","
                    + PermissionGroup.Permission.TERMINAL.getPermissionName());

    public static final PermissionGroup NETWORK_PERMISSION_GROUP = new PermissionGroup("networkgroup", "Network Group",
            "This permission group contains all UI network permissions",
            PermissionGroup.Permission.NETWORK.getPermissionName());

    public static final PermissionGroup RANKING_PERMISSION_GROUP = new PermissionGroup("rankinggroup", "Ranking Group",
            "This permission group contains all UI ranking permissions",
            PermissionGroup.Permission.RANKING.getPermissionName());

    public static final PermissionGroup SUSCRIBER_PERMISSION_GROUP = new PermissionGroup("subscribergroup",
            "Subscriber Group", "This permission group contains all UI subscriber permissions",
            PermissionGroup.Permission.SUBSCRIBER.getPermissionName());

    public static final PermissionGroup TERMINAL_PERMISSION_GROUP = new PermissionGroup("terminalgroup",
            "Terminal Group", "This permission group contains all UI terminal permissions",
            PermissionGroup.Permission.TERMINAL.getPermissionName());

    public static final Role CUSTOMER_CARE_ROLE = new Role("customercare", "customercare",
            "This is the ENIQ Events marketing role", SUSCRIBER_PERMISSION_GROUP.getName());

    public static final Role MARKETING_ROLE = new Role("marketing", "Marketing",
            "This is the ENIQ Events marketing role", SUSCRIBER_PERMISSION_GROUP.getName() + ","
                    + TERMINAL_PERMISSION_GROUP.getName());

    public static final Role NETWORK_MONITORING_ROLE = new Role("networkmonitoring", "Network Monitoring",
            "This is the ENIQ Events network monitoring role", RANKING_PERMISSION_GROUP.getName());

    public static final Role NETWORK_TROUBLESHOOTING_ROLE = new Role("networktroubleshooting",
            "Network Troubleshooting", "This is the ENIQ Events network trouble shooting role",
            NETWORK_PERMISSION_GROUP.getName());

    public static final Role POWER_USER_ROLE = new Role("poweruser", "Power User Role",
            "This is the ENIQ Power User which can access all ENIQ Events UI functions", ALLPERMISSIONS_GROUP.getName());

    public static final Role SYSADMIN_ROLE = new Role("sysadmin", "System Administrator Role",
            "This is the ENIQ System Administrator which can access all ENIQ Events UI functions",
            ALLPERMISSIONS_GROUP.getName());

    public static final Role TERMINAL_SPECIALIST_ROLE = new Role("terminalspecialist", "Terminal Specialist",
            "This is the ENIQ Events terminal specialist role", TERMINAL_PERMISSION_GROUP.getName());

    public static final User SYSADMIN_USER = new User("admin", "admin", "App", "Admin", "admin@ericsson.com", "", "",
            SYSADMIN_ROLE.getName());
    //public static final User SYS_ADMIN= null;

}
