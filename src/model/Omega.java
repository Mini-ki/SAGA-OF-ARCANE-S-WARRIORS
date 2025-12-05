package src.model;
public class Omega extends Monster {
    public Omega() { super("M01","Omega", "assets/monster/omega.png", 4000, 100, 100); }

    @Override
    public void performAI(Hero target) {
        if (turnCounter % 4 == 0) {
            System.out.println("Omega uses Stun Blast!");
            target.takeDamage(attack);
            target.setStunned(true);
        } else {
            attack(target);
        }
    }
}