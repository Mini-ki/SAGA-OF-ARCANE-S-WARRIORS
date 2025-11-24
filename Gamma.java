public class Gamma extends Hero implements Item {
    public Gamma() { super("Gamma", 890, 470, 120, 50); }
    @Override public String useSkill1(GameCharacter t) { 
        if(!canCast("Skill1", 100, 4)) return "CD"; t.takeDamage(attack*1.5); return "Shadow Cut"; 
    }
    @Override public String useSkill2(GameCharacter t) {
        if(!canCast("Skill2", 120, 4)) return "CD"; t.takeDamage(attack + 50); return "Pierce";
    }
    @Override public String useUltimate(GameCharacter t) {
        if(!canCast("Ultimate", 135, 7)) return "CD"; t.takeDamage(attack*2.5); return "Ult Fatal Blow";
    }
    @Override public void applyPassive() { this.attack += 10; }

    // Item
    public void BloodthirstAxe(){};
    public void CrimsonArmor(){};
    public void VampiricBlade(){};
    public void EndlessWarboots(){};
    public void WingsOfTheFallen(){};
    public void DemonHunterSword(){};
}