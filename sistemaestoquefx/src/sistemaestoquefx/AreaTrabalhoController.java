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

    // --- VARIÁVEIS DE CONTROLE DE ACESSO ---
    @FXML private javafx.scene.control.Menu menuFuncionarios;
    @FXML private javafx.scene.control.MenuItem menuItemPosicaoDia;
    @FXML private javafx.scene.control.MenuItem menuItemHistoricoVendas;

    // --- MÉTODOS DE USUÁRIO E ACESSO ---

    public void exibirNomeUsuario(String nome) {
        lblUsuarioLogado.setText(nome);
    }

    // A MÁGICA DO ACESSO ACONTECE AQUI
    public void configurarNivelDeAcesso(String nivelAcesso) {
        // Se a pessoa que logou for Funcionário (ou Usuário normal), esconde as telas
        if (nivelAcesso.equalsIgnoreCase("Funcionário") || nivelAcesso.equalsIgnoreCase("Usuário")) {
            menuFuncionarios.setVisible(false); // Esconde o menu de Funcionários inteiro
            menuItemPosicaoDia.setVisible(false); // Esconde o submenu Posição do Dia
            menuItemHistoricoVendas.setVisible(false); // Esconde o submenu Histórico
        }
        // Se for "Administrador", não faz nada, pois por padrão tudo já vem visível (true)
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

    // --- MÉTODOS DO MENU DE PRODUTOS ---
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormularioEstoque.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Controle de Estoque");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir controle de estoque: " + e.getMessage());
        }
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

    @FXML
    void abrirPDV(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormularioVendas.fxml"));
            Parent root = loader.load();
            
            // Para o PDV, geralmente queremos a tela maximizada
            Stage stage = new Stage();
            stage.setTitle("Ponto de Vendas");
            stage.setScene(new Scene(root));
            stage.setMaximized(true); // Abre em tela cheia
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir o PDV: " + e.getMessage());
        }
    }

    @FXML
    void abrirPosicaoDoDia(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("FormularioTotalDia.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Posição do Dia");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir a Posição do Dia: " + e.getMessage());
        }
    }

    @FXML
    void abrirHistoricoVendas(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("FormularioHistorico.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Histórico de Vendas");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir o Histórico: " + e.getMessage());
        }
    }

    // --- MÉTODOS DE CONFIGURAÇÕES E SAÍDA ---

    @FXML
    void trocarUsuarioAction(ActionEvent event) {
        try {
            // 1. Carrega e abre a tela de Login
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Login.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Login - Sistema de Estoque");
            stage.setScene(new javafx.scene.Scene(root));
            stage.setResizable(false);
            stage.show();

            // 2. Descobre qual é a janela atual (Área de Trabalho) e fecha ela
            javafx.stage.Stage janelaAtual = (javafx.stage.Stage) lblUsuarioLogado.getScene().getWindow();
            janelaAtual.close();

        } catch (Exception e) {
            System.out.println("Erro ao trocar de usuário: " + e.getMessage());
        }
    }
    
    @FXML
    void sairDoSistemaAction(ActionEvent event) {
        // 1. Cria a caixa de diálogo do tipo Confirmação
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Sair do Sistema");
        alerta.setHeaderText(null);
        alerta.setContentText("Tem certeza que deseja sair do sistema?");

        // 2. Cria os botões personalizados "Sim" e "Não"
        javafx.scene.control.ButtonType botaoSim = new javafx.scene.control.ButtonType("Sim");
        javafx.scene.control.ButtonType botaoNao = new javafx.scene.control.ButtonType("Não", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
        
        // 3. Coloca os botões na caixa de diálogo
        alerta.getButtonTypes().setAll(botaoSim, botaoNao);

        // 4. Mostra a mensagem e espera o usuário clicar
        java.util.Optional<javafx.scene.control.ButtonType> resultado = alerta.showAndWait();
        
        // 5. Se ele clicar em "Sim", fecha tudo. Se for "Não", o aviso apenas some.
        if (resultado.isPresent() && resultado.get() == botaoSim) {
            System.exit(0);
        }
    }
    
}