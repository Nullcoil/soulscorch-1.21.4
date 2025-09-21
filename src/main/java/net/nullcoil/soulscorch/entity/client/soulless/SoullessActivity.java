package net.nullcoil.soulscorch.entity.client.soulless;

public enum SoullessActivity {
    PASSIVE(0), // Does nothing, except play certain animations randomly
    NEUTRAL(1), // Stares at Player that agitated them.
    HOSTILE(2); // Attacks with regular Zombie logic, but dealing my custom Soulscorch effect (already done)

    private final int id;
    SoullessActivity(int id) {this.id = id;}
    public int getId() { return id; }

    public static SoullessActivity fromId(int id) {
        for (SoullessActivity a : values()) if (a.id == id) return a;
        return PASSIVE;
    }
}
