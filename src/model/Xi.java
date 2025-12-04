package src.model;
public class Xi extends Hero implements Item {
    public Xi() { super("Xi", "assets/hero/xi.png",1111, 666, 99, 33, 10, 20, 30); }
    @Override public String useSkill1(GameCharacter t) {
        if(!canCast("Skill1", 178, 2)) return "CD"; return "Heal";
    }
    @Override public String useSkill2(GameCharacter t) {
        if(!canCast("Skill2", 211, 4)) return "CD"; this.defence += 50; return "Barrier";
    }
    @Override public String useUltimate(GameCharacter t) {
        if(!canCast("Ultimate", 400, 9)) return "CD"; return "Ult Blessing";
    }
    @Override public void applyPassive() { }

    // Item
    @Override public void BloodthirstAxe(){};
    @Override public void CrimsonArmor(){};
    @Override public void VampiricBlade(){};
    @Override public void EndlessWarboots(){};

    @Override
    public void WingsOfTheFallen() {
        this.defence += 40; // Defense tinggi
        this.maxHp += 200;  // HP tambahan
        System.out.println(this.getName() + " bought Wings of The Fallen! (Survival Up)");
    }

    @Override public void DemonHunterSword(){};
}