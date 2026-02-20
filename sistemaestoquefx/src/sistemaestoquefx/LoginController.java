package sistemaestoquefx;

import br.com.sistema.dao.FuncionariosDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
            
            // Chama o nosso DAO atualizado
            FuncionariosDAO dao = new FuncionariosDAO();
            dao.efetuarLogin(email, senha);
            
            // Se passou da linha de cima sem dar erro, o login funcionou!
            Alert alertaSucesso = new Alert(Alert.AlertType.INFORMATION);
            alertaSucesso.setTitle("Sucesso!");
            alertaSucesso.setHeaderText("Login aprovado!");
            alertaSucesso.setContentText("A conexão com o MySQL deu certo. Em breve, a sua Tela Principal abrirá aqui.");
            alertaSucesso.showAndWait();
            
        } catch (Exception e) {
            // Se o DAO lançar aquele erro de "senha inválida", ele cai aqui e mostra na tela
            Alert alertaErro = new Alert(Alert.AlertType.ERROR);
            alertaErro.setTitle("Erro de Autenticação");
            alertaErro.setHeaderText("Não foi possível acessar");
            alertaErro.setContentText(e.getMessage());
            alertaErro.showAndWait();
        }
    }

    @FXML
    void cancelarAction(ActionEvent event) {
        // Fecha o sistema
        System.exit(0);
    }
}