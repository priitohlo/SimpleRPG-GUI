public class Player extends Character {
    public Player(String name) {
        this.name = name;
        this.hitPoints = 100;
        this.equippedWeapon = new Greatsword();
    }
}
