package sistemaestoquefx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AreaTrabalhoController {

    @FXML
    private Label lblUsuarioLogado;

    public void exibirNomeUsuario(String nome) {
        lblUsuarioLogado.setText(nome);
    }

    // A MÁGICA: Evento que abre a tela de Clientes
    @FXML
    void abrirTelaClientes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormularioClientes.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Controle de Clientes");
            stage.setScene(new Scene(root));
            
            // Impede o usuário de clicar na Área de Trabalho enquanto não fechar o Cadastro
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
            
        } catch (Exception e) {
            System.out.println("Erro ao abrir a tela: " + e.getMessage());
        }
    }
    @FXML
    void abrirTelaFuncionarios(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormularioFuncionarios.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Controle de Funcionários");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir a tela de funcionários: " + e.getMessage());
        }
    }
    @FXML
    void abrirTelaFornecedores(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormularioFornecedores.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Controle de Fornecedores");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir a tela de fornecedores: " + e.getMessage());
        }
    }
}