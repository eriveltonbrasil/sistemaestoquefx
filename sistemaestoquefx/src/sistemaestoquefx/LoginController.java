package sistemaestoquefx;

import br.com.sistema.dao.FuncionariosDAO;
import br.com.sistema.jdbc.ConexaoBanco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private Button btnEntrar;
    @FXML private Button btnCancelar;

    @FXML
    void entrarAction(ActionEvent event) {
        try {
            String email = txtEmail.getText();
            String senha = txtSenha.getText();
            
            FuncionariosDAO dao = new FuncionariosDAO();
            
            // O DAO nos devolve uma String com o nome da pessoa
            String nomeDaPessoa = dao.efetuarLogin(email, senha);
            
            // --- NOVA BUSCA PARA DESCOBRIR O NÍVEL DE ACESSO ---
            String nivelAcesso = "Usuário"; // Por padrão, entra como restrito por segurança
            try {
                Connection conn = new ConexaoBanco().pegarConexao();
                String sql = "SELECT nivel_acesso FROM tb_funcionarios WHERE email = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    nivelAcesso = rs.getString("nivel_acesso");
                }
                stmt.close();
                conn.close();
            } catch (Exception e) {
                System.out.println("Erro ao buscar nível de acesso: " + e.getMessage());
            }
            // ---------------------------------------------------
            
            // 1. Fecha a tela de Login atual
            Stage stageAtual = (Stage) btnEntrar.getScene().getWindow();
            stageAtual.close();
            
            // 2. Carrega a Tela Principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AreaTrabalho.fxml"));
            Parent root = loader.load();
            
            // 3. O PASSE DE MÁGICA: Manda o NOME e o NÍVEL para a Área de Trabalho
            AreaTrabalhoController controller = loader.getController();
            controller.exibirNomeUsuario(nomeDaPessoa);
            controller.configurarNivelDeAcesso(nivelAcesso); // <-- A TRAVA DE SEGURANÇA ACONTECE AQUI!
            
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