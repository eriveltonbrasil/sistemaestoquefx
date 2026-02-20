package sistemaestoquefx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FormularioFuncionariosController {

    @FXML private ComboBox<String> cbxEstado;
    @FXML private ComboBox<String> cbxNivel;
    @FXML private PasswordField txtSenha;
    @FXML private TextField txtCargo; // Variável nova aqui!

    @FXML
    public void initialize() {
        ObservableList<String> estados = FXCollections.observableArrayList(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        );
        cbxEstado.setItems(estados);

        ObservableList<String> niveis = FXCollections.observableArrayList(
            "Administrador", "Usuário"
        );
        cbxNivel.setItems(niveis);
    }
}