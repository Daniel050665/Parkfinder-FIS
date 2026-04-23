package com.parkfinder.util;

import com.parkfinder.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class ViewLoader {

    public static void navegar(Stage stage, String fxmlName) {
        try {
            URL url = encontrarFxml(fxmlName);

            if (url == null) {
                throw new IllegalStateException(
                        "FXML no encontrado: " + fxmlName +
                                "\nRutas intentadas:" +
                                "\n  Views/" + fxmlName +
                                "\n  com/parkfinder/Views/" + fxmlName
                );
            }

            FXMLLoader loader = new FXMLLoader(url);
            loader.setControllerFactory(MainApp.springContext::getBean);
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static URL encontrarFxml(String fxmlName) {
        ClassLoader cl = MainApp.class.getClassLoader();

        // Intenta todas las rutas posibles en orden
        String[] rutas = {
                "Views/" + fxmlName,
                "com/parkfinder/Views/" + fxmlName,
                "static/Views/" + fxmlName,
        };

        for (String ruta : rutas) {
            URL url = cl.getResource(ruta);
            if (url != null) {
                System.out.println("[ViewLoader] Encontrado en: " + ruta);
                return url;
            }
        }
        return null;
    }
}