package Src.Model;
public class Delta extends Hero implements Item {
    public Delta() { super("Delta", 975, 430, 80, 45); }
    @Override public String useSkill1(GameCharacter t) {
        if(!canCast("Skill1", 95, 4)) return "CD"; t.takeDamage(attack); return "Spark";
    }
    @Override public String useSkill2(GameCharacter t) {
        if(!canCast("Skill2", 203, 6)) return "CD"; t.takeDamage(attack); t.setStunned(true); return "Lightning";
    }
    @Override public String useUltimate(GameCharacter t) {
        if(!canCast("Ultimate", 300, 8)) return "CD"; t.takeDamage(attack*2); return "Ult Storm";
    }
    @Override public void applyPassive() { this.currentMp += 30; }

    // Item
    public void BloodthirstAxe(){};
    public void CrimsonArmor(){};
    public void VampiricBlade(){};
    public void EndlessWarboots(){};
    public void WingsOfTheFallen(){};
    public void DemonHunterSword(){};
}