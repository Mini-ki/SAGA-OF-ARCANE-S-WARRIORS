package src.model;
import java.util.ArrayList;
import java.util.List;

public abstract class GameCharacter {
    protected String name;
    protected String idCharacter;
    protected double currentHp;
    protected double maxHp;
    protected double attack;
    protected double defence;
    protected boolean isStunned;
    protected boolean isAlive;
    protected String imageCharacter;
    protected List<String> activeEffects;

    public GameCharacter(String idCharacter, String name, String imageCharacter, double maxHp, double attack, double defence) {
        this.idCharacter = idCharacter;
        this.name = name;
        this.imageCharacter = imageCharacter;
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
    public String getIdCharacter(){return idCharacter;}
    public String getName() { return name; }
    public double getCurrentHp() { return currentHp; }
    public double getMaxHp() { return maxHp; }
    public double getAttack() {return attack;}
    public double getDefense() {return defence;}
    public boolean isAlive() { return isAlive; }
    public String getImageUrl(){ return imageCharacter;}
    
    public void setStunned(boolean s) { isStunned = s; }
    public void setCurrentHp(double currentHp){this.currentHp = currentHp;}
}