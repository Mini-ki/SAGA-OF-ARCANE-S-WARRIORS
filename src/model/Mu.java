package src.model;
public class Mu extends Monster {
    public Mu() { super("M03","Mu", "assets/monster/Mu.png", 5000, 200, 200); }

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