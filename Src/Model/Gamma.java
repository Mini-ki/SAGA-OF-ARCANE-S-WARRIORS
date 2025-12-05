package src.model;
public class Gamma extends Hero{
    public Gamma() { super("H02", "Gamma", "assets/hero/gamma.png", 1200, 10000, 500, 50, 1,2,3); }
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
    @Override public void BloodthirstAxe(){};
    @Override public void CrimsonArmor(){};

    @Override
    public void VampiricBlade() {
        this.attack += 40; // Attack tinggi untuk burst
        System.out.println(this.getName() + " bought Vampiric Blade! (Attack +40 & Lifesteal Up)");
    }

    @Override public void EndlessWarboots(){};
    @Override public void WingsOfTheFallen(){};
    @Override public void DemonHunterSword(){};
}