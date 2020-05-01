import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class RandomMonster {
    public static Monster generate() {
        List<Supplier<Monster>> monsters = Arrays.asList(Orc::new, Ogre::new, Merfolk::new, Goblin::new);
        int random = new Random().nextInt(monsters.size());
        return monsters.get(random).get();
    }
}
