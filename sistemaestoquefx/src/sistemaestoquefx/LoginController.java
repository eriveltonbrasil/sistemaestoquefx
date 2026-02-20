package sistemaestoquefx;

import br.com.sistema.dao.FuncionariosDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnCancelar;

    @FXML
    void entrarAction(ActionEvent event) {
        try {
            String email = txtEmail.getText();
            String senha = txtSenha.getText();
            
            FuncionariosDAO dao = new FuncionariosDAO();
            
            // O DAO agora nos devolve uma String com o nome da pessoa!
            String nomeDaPessoa = dao.efetuarLogin(email, senha);
            
            // 1. Fecha a tela de Login atual
            Stage stageAtual = (Stage) btnEntrar.getScene().getWindow();
            stageAtual.close();
            
            // 2. Carrega a Tela Principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AreaTrabalho.fxml"));
            Parent root = loader.load();
            
            // 3. O PASSE DE MÁGICA: Manda o nome para a Área de Trabalho
            AreaTrabalhoController controller = loader.getController();
            controller.exibirNomeUsuario(nomeDaPessoa);
            
            Stage stageAreaTrabalho = new Stage();
            stageAreaTrabalho.setTitle("Painel de Controle - Sistema de Estoque");
            stageAreaTrabalho.setScene(new Scene(root));
            stageAreaTrabalho.show();
            
        } catch (Exception e) {
            Alert alertaErro = new Alert(Alert.AlertType.ERROR);
            alertaErro.setTitle("Erro de Autenticação");
            alertaErro.setHeaderText("Não foi possível acessar");
            alertaErro.setContentText(e.getMessage());
            alertaErro.showAndWait();
        }
    }

    @FXML
    void cancelarAction(ActionEvent event) {
        System.exit(0);
    }
}