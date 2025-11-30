package Src.Model;
public class Alpha extends Hero implements Item {
    public Alpha() { super("Alpha", 1000, 500, 100, 70); }

    @Override
    public String useSkill1(GameCharacter target) {
        if (!canCast("Skill1", 150, 3)) return "Alpha: CD/Mana!";
        double dmg = attack * 1.2;
        target.takeDamage(dmg);
        return "Alpha uses Slash (Lifesteal)!";
    }

    @Override
    public String useSkill2(GameCharacter target) {
        if (!canCast("Skill2", 175, 4)) return "Alpha: CD/Mana!";
        double dmg = attack * 1.5;
        target.takeDamage(dmg);
        return "Alpha uses Blood Strike!";
    }

    @Override
    public String useUltimate(GameCharacter target) {
        if (!canCast("Ultimate", 300, 7)) return "Alpha: CD/Mana!";
        double dmg = attack * 2.5;
        target.takeDamage(dmg);
        return "ALPHA ULTIMATE!";
    }
    @Override public void applyPassive() {  }

    // Item
    public void BloodthirstAxe(){};
    public void CrimsonArmor(){};
    public void VampiricBlade(){};
    public void EndlessWarboots(){};
    public void WingsOfTheFallen(){};
    public void DemonHunterSword(){};
}