import java.util.Random;

public class DiceRoller {
    public static Integer roll(Integer times, Dice dice) {
        Random random = new Random();
        Integer diceSides = dice.getSides();
        int result = 0;
        for (int i = 0; i < times; i++) {
            result += random.nextInt(diceSides) + 1;
        }
        return result;
    }
}
