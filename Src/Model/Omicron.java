package Src.Model;
public class Omicron extends Monster {
    public Omicron() { super("Omicron", 3000, 100, 50); }

    @Override
    public void performAI(Hero target) {
        if (turnCounter % 3 == 0) {
            System.out.println("Omicron uses Heavy Smash!");
            target.takeDamage(attack * 1.5);
        } else {
            attack(target);
        }
    }
}