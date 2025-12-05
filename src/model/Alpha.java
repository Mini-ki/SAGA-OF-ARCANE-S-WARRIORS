package src.model;
public class Alpha extends Hero {
    public Alpha() { super("H01", "Alpha", "assets/hero/alpa.png", 2000, 1000, 400, 250, 5, 10, 20); }

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
}