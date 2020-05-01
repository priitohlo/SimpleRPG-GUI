public abstract class Character implements ICharacter {
    protected String name;
    protected Integer hitPoints;
    protected Weapon equippedWeapon;

    @Override
    public Integer rollInitiative() {
        return DiceRoller.roll(1, Dice.D20);
    }

    @Override
    public void receiveDamage(int damage) {
        this.hitPoints -= damage;
    }

    @Override
    public Integer rollAttack() {
        Integer diceRoll = DiceRoller.roll(equippedWeapon.attackDiceMultiplier, equippedWeapon.attackDice);
        return diceRoll + equippedWeapon.attackBonus;
    }

    public void setHitPoints(Integer hitPoints) {
        this.hitPoints = hitPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHitPoints() {
        return hitPoints;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public void setEquippedWeapon(Weapon equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }
}
