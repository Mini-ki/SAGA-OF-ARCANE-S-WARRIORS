package Src.Model;
public class Omega extends Monster {
    public Omega() { super("Omega", 4000, 100, 55); }

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