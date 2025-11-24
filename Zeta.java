public class Zeta extends Hero implements Item {
    public Zeta() { super("Zeta", 1350, 640, 100, 59); }
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
    public void BloodthirstAxe(){};
    public void CrimsonArmor(){};
    public void VampiricBlade(){};
    public void EndlessWarboots(){};
    public void WingsOfTheFallen(){};
    public void DemonHunterSword(){};
}