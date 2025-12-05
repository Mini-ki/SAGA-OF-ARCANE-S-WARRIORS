package src.model;
public class Delta extends Hero{
    public Delta() { super("H05","Delta", "assets/hero/delta.png", 1500, 5000, 500, 45, 7, 10, 20); }

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
    @Override public void BloodthirstAxe(){};
    @Override public void CrimsonArmor(){};
    @Override public void VampiricBlade(){};

    @Override
    public void EndlessWarboots() {
        this.maxMp += 300; // Mana besar untuk spam skill
        this.defence += 5; // Sedikit movement/defense
        System.out.println(this.getName() + " bought Endless Warboots! (Max MP +300)");
    }
    
    @Override public void WingsOfTheFallen(){};
    @Override public void DemonHunterSword(){};
}