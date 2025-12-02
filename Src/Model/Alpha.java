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
    @Override
    public void BloodthirstAxe() {
        this.attack += 30; // Menambah damage skill
        System.out.println(this.getName() + " bought Bloodthirst Axe! (Attack +30 & Skill Lifesteal Up)");
    }

    @Override public void CrimsonArmor(){};
    @Override public void VampiricBlade(){};
    @Override public void EndlessWarboots(){};
    @Override public void WingsOfTheFallen(){};
    @Override public void DemonHunterSword(){};
}