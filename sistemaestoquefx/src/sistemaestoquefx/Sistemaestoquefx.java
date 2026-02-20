package sistemaestoquefx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author ERIVELTON BRASIL
 */
public class Sistemaestoquefx extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        // Manda o JavaFX ler o seu desenho feito no Scene Builder (Login.fxml)
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(root);
        
        // Título que vai aparecer lá em cima na barra do Windows
        stage.setTitle("Autenticação - Sistema de Estoque");
        
        // Trava o tamanho da tela para o usuário não estragar o seu design!
        stage.setResizable(false); 
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}