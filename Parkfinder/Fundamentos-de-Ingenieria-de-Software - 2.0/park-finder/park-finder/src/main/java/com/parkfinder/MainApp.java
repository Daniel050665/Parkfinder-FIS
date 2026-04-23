package com.parkfinder;

import com.parkfinder.util.ViewLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class MainApp extends Application {

    public static ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        springContext = SpringApplication.run(ParkFinderApplication.class);
    }

    @Override
    public void start(Stage stage) {
        ViewLoader.navegar(stage, "Login.fxml");
        stage.setTitle("ParkFinder");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}