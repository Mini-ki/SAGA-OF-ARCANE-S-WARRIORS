package Src.Model;
public class Beta extends Hero implements Item {
    public Beta() { super("Beta", 2000, 750, 60, 85); }

    @Override
    public String useSkill1(GameCharacter target) {
        if (!canCast("Skill1", 50, 3)) return "Beta: CD/Mana!";
        target.takeDamage(attack);
        target.setStunned(true);
        return "Beta uses Bash (Stun)!";
    }

    @Override
    public String useSkill2(GameCharacter target) {
        if (!canCast("Skill2", 150, 5)) return "Beta: CD/Mana!";
        this.defence += 20;
        return "Beta uses Shield Up!";
    }

    @Override
    public String useUltimate(GameCharacter target) {
        if (!canCast("Ultimate", 200, 8)) return "Beta: CD/Mana!";
        target.takeDamage(attack * 2);
        return "BETA ULTIMATE!";
    }
    @Override public void applyPassive() { this.defence += 5; }

    // Item
    public void BloodthirstAxe(){};
    public void CrimsonArmor(){};
    public void VampiricBlade(){};
    public void EndlessWarboots(){};
    public void WingsOfTheFallen(){};
    public void DemonHunterSword(){};
}