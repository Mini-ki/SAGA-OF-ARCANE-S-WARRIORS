package src.model;
public class Zeta extends Hero{
    public Zeta() { super("H03", "Zeta", "assets/hero/zeta.png",1350, 640, 100, 59, 5, 15, 30); }
    @Override public String useSkill1(GameCharacter t) {
        if(!canCast("Skill1", 167, 3)) return "CD"; t.takeDamage(attack * 2); return "Crit Shot";
    }
    @Override public String useSkill2(GameCharacter t) {
        if(!canCast("Skill2", 249, 6)) return "CD"; t.takeDamage(attack * 2.5); return "Snipe";
    }
    @Override public String useUltimate(GameCharacter t) {
        if(!canCast("Ultimate", 331, 9)) return "CD"; t.takeDamage(attack * 3); return "Ult Headshot";
    }
    @Override public void applyPassive() { this.attack += 5; }

    // Item
    @Override public void BloodthirstAxe(){};
    @Override public void CrimsonArmor(){};
    @Override public void VampiricBlade(){};
    @Override public void EndlessWarboots(){};
    @Override public void WingsOfTheFallen(){};
    @Override
    public void DemonHunterSword() {
        this.attack += 50; // Damage masif untuk sniper
        System.out.println(this.getName() + " bought Demon Hunter Sword! (Attack +50 & Tank Killer)");
    }
}