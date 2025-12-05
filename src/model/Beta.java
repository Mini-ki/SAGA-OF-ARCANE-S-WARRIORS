package src.model;
public class Beta extends Hero {
    public Beta() { super("H04","Beta", "assets/hero/beta.png", 3000, 1500, 100, 300, 2, 10, 30); }

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
    @Override public void BloodthirstAxe(){};

    @Override
    public void CrimsonArmor() {
        this.defence += 50; // Defense masif
        this.maxHp += 500;  // HP tambahan
        System.out.println(this.getName() + " bought Crimson Armor! (Def +50 & HP +500)");
    }
    
    @Override public void VampiricBlade(){};
    @Override public void EndlessWarboots(){};
    @Override public void WingsOfTheFallen(){};
    @Override public void DemonHunterSword(){};
}