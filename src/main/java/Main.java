import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.util.Optional;

public class Main extends Application {

    String playerName = "Legolas";
    String cName;

    public void checkName(String name) {
        if (name.equals("Legolas")) {
            throw new RuntimeException();
        }
    }

    @Override
    public void start(Stage peaLava) {
        Stage mainStage = new Stage();
        mainStage.setMinHeight(800f);
        mainStage.setMinWidth(500f);

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(1000));

        while (this.playerName.equals("Legolas")) {
            try {
                TextInputDialog dName = new TextInputDialog("Kangelane");
                dName.setTitle("Sinu nimi");
                dName.setContentText("Palun sisesta oma nimi");
                Optional<String> rName = dName.showAndWait();
                rName.ifPresent(name -> this.cName = name);
                checkName(this.cName);
                this.playerName = this.cName;
            } catch (RuntimeException e) {
                Alert nameAlert = new Alert(Alert.AlertType.ERROR);
                nameAlert.setTitle("Vale nimi");
                nameAlert.setContentText("Sai ju öeldud, et ei tohi Legolas olla, proovi uuesti");
                nameAlert.showAndWait();
            }
        }

        BorderPane mainPane = new BorderPane();
        BorderPane playPane = new BorderPane();

        Label lTopText = new Label("nimi");
        mainPane.setTop(lTopText);

        mainPane.setCenter(playPane);

        GridPane combatPane = new GridPane();
        combatPane.setHgap(6f);
        combatPane.setVgap(2f);

        playPane.setCenter(combatPane);

        BorderPane.setMargin(lTopText, new Insets(12f));
        BorderPane.setMargin(combatPane, new Insets(12f));

        Button bNewMonster = new Button("uus koletis");
        Button bLoad = new Button("laadi oma tegelane");
        Button bSave = new Button("salvesta oma tegelane");
        Button bNo = new Button("ära siia vajuta, muidu tekib viga");

        HBox buttonrow = new HBox(8);

        buttonrow.getChildren().addAll(bNewMonster, bLoad, bSave);

        mainPane.setBottom(buttonrow);

        Player player = new Player(playerName);
        lTopText.setText("Sinu nimi on " + player.name + ", sul on " + player.hitPoints + " elupunkti.");

        bNewMonster.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Monster monster = RandomMonster.generate();
                Integer playerInitiative = player.rollInitiative();
                Integer monsterInitiative = monster.rollInitiative();
                boolean playerAttack = false;

                if (playerInitiative >= monsterInitiative) {
                    playerAttack = true;
                    lTopText.setText("Sina alustad");
                } else {
                    lTopText.setText("Koletis alustab");
                }

                combatPane.getChildren().clear();
                int row = 0;
                combatPane.add(new Label("Veeretaja"), 0, row);
                combatPane.add(new Label("Veeretus"), 1, row);
                combatPane.add(new Label("tulemus"), 2, 0);

                while (true) {
                    row += 1;
                    Character attacker;
                    Character defender;

                    if (playerAttack) {
                        attacker = player;
                        defender = monster;
                    } else {
                        attacker = monster;
                        defender = player;
                    }

                    combatPane.add(new Label(attacker.name), 0, row);

                    Integer damage = attacker.rollAttack();
                    combatPane.add(new Label(String.valueOf(damage)), 1, row);
                    defender.receiveDamage(damage);
                    combatPane.add(new Label("Võitlejal " + defender.name + " on nüüd " + defender.hitPoints + " elupunkti."), 2, row);

                    if (defender.hitPoints <= 0) {
                        break;
                    }

                    playerAttack = !playerAttack;
                }

                if (player.hitPoints > 0) {
                    lTopText.setText("Sinu nimi on " + player.name + ", sul on " + player.hitPoints + " elupunkti.");
                } else {
                    Alert gameOver = new Alert(Alert.AlertType.CONFIRMATION);
                    gameOver.setContentText("Mäng läbi, teeme uue?");
                    ButtonType bYes = new ButtonType("Jah");
                    gameOver.getButtonTypes().setAll(bYes);
                    Optional<ButtonType> answer = gameOver.showAndWait();
                    if (answer.get() == bYes) {
                        player.setHitPoints(100);
                        lTopText.setText("Sinu nimi on " + player.name + ", sul on " + player.hitPoints + " elupunkti.");
                    }
                }

            }
        });

        bSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("game.dat"))) {
                    dos.writeUTF(player.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        bLoad.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try (DataInputStream dis = new DataInputStream(new FileInputStream("game.dat"))) {
                    player.name = dis.readUTF();
                    lTopText.setText("Sinu nimi on " + player.name + ", sul on " + player.hitPoints + " elupunkti.");
                 } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Scene mainScene = new Scene(mainPane);
        mainStage.setScene(mainScene);
        mainStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
