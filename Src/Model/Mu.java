package Src.Model;
public class Mu extends Monster {
    public Mu() { super("Mu", 5000, 100, 60); }

    @Override
    public void performAI(Hero target) {
        if (turnCounter % 3 == 0) {
            System.out.println("Mu uses Life Drain!");
            target.takeDamage(attack * 1.2);
        } else {
            attack(target);
        }
    }
}