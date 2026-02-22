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

    @FXML
    void abrirTelaClientes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormularioClientes.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Controle de Clientes");
            stage.setScene(new Scene(root));
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

    // --- NOVOS MÉTODOS DO MENU DE PRODUTOS ---
    // Devolvemos o método antigo para o JavaFX não dar erro ao carregar a tela
    @FXML
    void abrirTelaProdutos(ActionEvent event) {
        abrirJanelaProdutos(0); 
    }

    @FXML
    void abrirFormularioProdutos(ActionEvent event) {
        abrirJanelaProdutos(0); // Abre na Aba 0 (Dados do Produto)
    }

    @FXML
    void abrirConsultaProdutos(ActionEvent event) {
        abrirJanelaProdutos(1); // Abre na Aba 1 (Consulta de Produtos)
    }
    
    @FXML
    void abrirControleEstoque(ActionEvent event) {
        // Deixei preparado para quando criarmos a tela de Controle de Estoque!
        System.out.println("Abrir tela de controle de estoque...");
    }

    // Método auxiliar (A Mágica acontece aqui)
    private void abrirJanelaProdutos(int indiceAba) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormularioProdutos.fxml"));
            Parent root = loader.load();

            // Pega o controle da tela de produtos antes de exibi-la
            FormularioProdutosController controller = loader.getController();
            // Manda selecionar a aba baseada no botão clicado
            controller.selecionarAba(indiceAba); 

            Stage stage = new Stage();
            stage.setTitle("Controle de Produtos");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir a tela de produtos: " + e.getMessage());
        }
    }
    
}