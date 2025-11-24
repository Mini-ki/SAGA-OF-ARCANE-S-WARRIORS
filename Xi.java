public class Xi extends Hero implements Item {
    public Xi() { super("Xi", 1111, 666, 99, 33); }
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
    public void BloodthirstAxe(){};
    public void CrimsonArmor(){};
    public void VampiricBlade(){};
    public void EndlessWarboots(){};
    public void WingsOfTheFallen(){};
    public void DemonHunterSword(){};
}