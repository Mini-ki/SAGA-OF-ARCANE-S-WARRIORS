package Src.Model;
import java.util.ArrayList;
import java.util.List;

public abstract class GameCharacter {
    protected String name;
    protected double currentHp;
    protected double maxHp;
    protected double attack;
    protected double defence;
    protected boolean isStunned;
    protected boolean isAlive;
    protected List<String> activeEffects;

    public GameCharacter(String name, double maxHp, double attack, double defence) {
        this.name = name;
        this.maxHp = this.currentHp = maxHp;
        this.attack = attack;
        this.defence = defence;
        this.isStunned = false;
        this.isAlive = true;
        this.activeEffects = new ArrayList<>();
    }

    public void attack(GameCharacter target) {
        System.out.println(this.name + " attacks " + target.getName());
        target.takeDamage(this.attack);
    }

    public void takeDamage(double damage) {
        double finalDmg = Math.max(1, damage - (this.defence * 0.3)); // Simple mitigation
        this.currentHp -= finalDmg;
        if (this.currentHp <= 0) {
            this.currentHp = 0;
            this.isAlive = false;
        }
    }


    public void updateTurn() {
        if (isStunned) isStunned = false;
    }

    // Getters & Setters
    public String getName() { return name; }
    public double getCurrentHp() { return currentHp; }
    public double getMaxHp() { return maxHp; }
    public boolean isAlive() { return isAlive; }
    public void setStunned(boolean s) { isStunned = s; }
}