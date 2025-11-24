public abstract class Monster extends GameCharacter {
    protected int turnCounter;

    public Monster(String name, double hp, double attack, double defence) {
        super(name, hp, attack, defence);
        this.turnCounter = 0;
    }

    public abstract void performAI(Hero target);

    @Override
    public void updateTurn() {
        super.updateTurn();
        turnCounter++;
    }
}