package src.model;
import java.util.HashMap;
import java.util.Map;

public abstract class Hero extends GameCharacter implements Item {
    protected double currentMp;
    protected double maxMp;
    protected double regenMp;
    protected boolean awakeningMode;
    public Map<String, Integer> baseCooldowns;
    public Map<String, Integer> skillCooldowns;

    public Hero(String idCharacter, String name, String imageCharacter, double maxHp, double maxMp, double attack, double defence, int cd1, int cd2, int cdulti) {
        super(idCharacter, name, imageCharacter, maxHp, attack, defence);
        this.maxMp = this.currentMp = maxMp;
        this.regenMp = 200;
        this.awakeningMode = false;
        // Base Skill
        this.baseCooldowns = new HashMap<>();
        this.baseCooldowns.put("Skill1", cd1);
        this.baseCooldowns.put("Skill2", cd2);
        this.baseCooldowns.put("Ultimate", cdulti);
        this.baseCooldowns.put("RegenHp", 5);
        this.baseCooldowns.put("RegenMp", 5);
        this.baseCooldowns.put("Awakening", 10);

        // Current Skill
        this.skillCooldowns = new HashMap<>();
        this.skillCooldowns.put("Skill1", 0);
        this.skillCooldowns.put("Skill2", 0);
        this.skillCooldowns.put("Ultimate", 0);
        this.skillCooldowns.put("RegenHp", 0);
        this.skillCooldowns.put("RegenMp", 0);
        this.skillCooldowns.put("Awakening", 0);
    }

    public abstract String useSkill1(GameCharacter target);
    public abstract String useSkill2(GameCharacter target);
    public abstract String useUltimate(GameCharacter target);
    public abstract void applyPassive();

    public void activateAwakening() {
        if (!awakeningMode && currentHp <= maxHp * 0.3) {
            awakeningMode = true;
            this.attack *= 1.5;
            this.defence *= 1.5;
        }
    }

    public void deactivateAwakening() {
        if (awakeningMode) {
            awakeningMode = false;
            this.attack /= 1.5;
            this.defence /= 1.5;
        }
    }

    @Override
    public void updateTurn() {
        super.updateTurn();
        this.currentMp = Math.min(maxMp, currentMp + regenMp);
        skillCooldowns.replaceAll((k, v) -> Math.max(0, v - 1));
    }

    protected boolean canCast(String skill, double cost, int cd) {
        if (isStunned || currentMp < cost || skillCooldowns.get(skill) > 0) return false;
        currentMp -= cost;
        skillCooldowns.put(skill, cd);
        return true;
    }

    public static Hero createHero(String id) {
        switch (id) {
            case "H01":
                return new Alpha();
            case "H02":
                return new Gamma();
            case "H03":
                return new Zeta();
            case "H04":
                return new Beta();
            case "H05":
                return new Delta();
            case "H06":
                return new Xi();    
            default:
                throw new IllegalArgumentException("Unknown hero id: " + id);
        }

    }

    public double getCurrentMp(){
        return currentMp;
    }

    public double getMaxMp() {
        return maxMp;
    }

    public void setCurrentMp(double currentMp){
        this.currentMp = currentMp;
    }

    @Override public void BloodthirstAxe() {}
    @Override public void CrimsonArmor(){};
    @Override public void VampiricBlade(){};
    @Override public void EndlessWarboots(){};
    @Override public void WingsOfTheFallen(){};
    @Override public void DemonHunterSword(){};

}