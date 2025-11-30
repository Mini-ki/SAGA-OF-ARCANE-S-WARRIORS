package Src.Model;
import java.util.HashMap;
import java.util.Map;

public abstract class Hero extends GameCharacter {
    protected double currentMp;
    protected double maxMp;
    protected double regenMp;
    protected boolean awakeningMode;
    protected Map<String, Integer> skillCooldowns;

    public Hero(String name, double maxHp, double maxMp, double attack, double defence) {
        super(name, maxHp, attack, defence);
        this.maxMp = this.currentMp = maxMp;
        this.regenMp = 200;
        this.awakeningMode = false;
        this.skillCooldowns = new HashMap<>();
        this.skillCooldowns.put("Skill1", 0);
        this.skillCooldowns.put("Skill2", 0);
        this.skillCooldowns.put("Ultimate", 0);
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
}