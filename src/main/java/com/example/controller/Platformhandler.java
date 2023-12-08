package com.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

public class Platformhandler {

    private static class Platform {
        private final Rectangle pillar;
        private Rectangle perfectionredblob;
        private final double idealstickdistance;
        private final double width;

        public Platform() {
            this.idealstickdistance = randomDistanceGenerator();
            this.width = randomWidthGenerator();
            this.pillar = new Rectangle(width, 250, Color.rgb(1, 1, 1));
            this.perfectionredblob = new Rectangle(15, 5, Color.rgb(255, 0, 0));
            this.pillar.setLayoutY(500);
            this.pillar.setLayoutX(250+idealstickdistance-width/2);
            this.perfectionredblob.setLayoutX(250+idealstickdistance-perfectionredblob.getWidth()/2);
            this.perfectionredblob.setLayoutY(500);
            maxpane.getChildren().add(pillar);
            maxpane.getChildren().add(perfectionredblob);
            platforms.add(this);
        }
        public Platform(double width) {
            this.width = randomWidthGenerator();
            this.idealstickdistance = -width/2;
            this.pillar = new Rectangle(width, 250, Color.rgb(1, 1, 1));
            this.pillar.setLayoutY(500);
            this.pillar.setLayoutX(250+idealstickdistance-width/2);
            maxpane.getChildren().add(pillar);
            platforms.add(this);
        }
    }
    private static AnchorPane maxpane;
    private static final Random rand = new Random();
    private static final ArrayList<Platform> platforms = new ArrayList<>();
    private static boolean initialized = false;

    public static int getideallength(){
        return (int) platforms.get(1).idealstickdistance;
    }
    public static void makePlatforms(Player Stickhero)
    {
        if (!initialized)
        {
            initialized=true;
            maxpane = ((AnchorPane) Player.getPlayerSprite().getParent());
            Platformhandlerinit();
        }
        else
        {
            moveGroup(Stickhero);
        }
    }



    private static void Platformhandlerinit() {
        new Platform(200);
        new Platform();
    }




    private static void moveGroup(Player stickhero) {
        //make a grouping of stick playersprite and platforms
        //move the grouping
        Pane originpain = ((Pane) Player.getPlayerSprite().getParent());
        Pane group = new Pane();
        ArrayList<Node> nodes = new ArrayList<>();
        for (Platform platform : platforms) {
            System.out.println(platform.pillar.getTranslateX());
            nodes.add(platform.pillar);
            if(platform.perfectionredblob!=null){
                nodes.add(platform.perfectionredblob);
            }
        }
        nodes.add(Player.getPlayerSprite());
        group.getChildren().addAll(nodes);
        originpain.getChildren().add(group);

        KeyValue kv = new KeyValue(group.translateXProperty(), group.getTranslateX()-stickhero.getlength());
        KeyFrame kf = new KeyFrame(Duration.millis(100), kv);
        Timeline timeline = new Timeline(kf);

        KeyValue kv2 = new KeyValue(Player.getPlayerSprite().translateXProperty(), Player.getPlayerSprite().getTranslateX()-25);
        KeyFrame kf2 = new KeyFrame(Duration.millis(10), kv2);
        Timeline timeline2 = new Timeline(kf2);

        timeline.play();

        timeline.setOnFinished(event -> {
            System.out.println("spawning new platform");
            new Platform();
            timeline2.play();
            pillareliminator(stickhero);
            stickhero.noneanimationplaying();
        });
    }



    public static void pillareliminator(Player stickhero){
        ((Pane) Player.getPlayerSprite().getParent()).getChildren().remove(platforms.get(0).pillar);
        ((Pane) Player.getPlayerSprite().getParent()).getChildren().remove(platforms.get(0).perfectionredblob);
        platforms.remove(0);
        System.out.println(platforms);
        //for iterator that prints layout X translateX and width of all pillars of platforms in the arraylist
        //print similiar stats for player.getsprite
        System.out.println(stickhero.getlength());
        System.out.println("player position? "+(Player.getPlayerSprite().getLayoutX()+Player.getPlayerSprite().getTranslateX()));
        for (Platform platform : platforms) {
            System.out.println("\n");
            System.out.println(platform.pillar.getLayoutX() + "       " + platform.pillar.getTranslateX() + "       " + platform.pillar.getWidth());
            System.out.println("\n");
        }
    }










    private static double randomDistanceGenerator(){return 150+rand.nextInt(350);}
    private static double randomWidthGenerator(){return 40+rand.nextInt(80);}
}
