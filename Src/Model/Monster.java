package src.model;
public abstract class Monster extends GameCharacter {
    protected int turnCounter;

    public Monster(String idCharacter, String name, String imageCharacter, double hp, double attack, double defence) {
        super(idCharacter, name, imageCharacter, hp, attack, defence);
        this.turnCounter = 0;
    }

    public abstract void performAI(Hero target);

    @Override
    public void updateTurn() {
        super.updateTurn();
        turnCounter++;
    }

    public static Monster createMonster(int level) {
        switch(level) {
            case 1: return new Omega();
            case 2: return new Omicron();
            case 3: return new Mu();
            default: return new Omega();
        }
    }

    
}