package sistemaestoquefx;

import br.com.sistema.dao.FornecedoresDAO;
import br.com.sistema.model.Fornecedores;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FormularioFornecedoresController {

    @FXML private TabPane tabPane;
    @FXML private TextField txtCodigo, txtNome, txtEmail, txtCelular, txtTelefone, txtCep, txtEndereco, txtNumero, txtBairro, txtCidade, txtComplemento, txtCnpj, txtPesquisa;
    @FXML private ComboBox<String> cbxEstado;

    @FXML private TableView<Fornecedores> tabelaFornecedores;
    @FXML private TableColumn<Fornecedores, Integer> colCodigo;
    @FXML private TableColumn<Fornecedores, String> colNome, colCnpj, colEmail, colTelefone, colCelular, colCep, colEndereco, colBairro, colCidade, colUf;
    @FXML private TableColumn<Fornecedores, Integer> colNumero;

    @FXML
    public void initialize() {
        ObservableList<String> estados = FXCollections.observableArrayList("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");
        cbxEstado.setItems(estados);

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
        colCep.setCellValueFactory(new PropertyValueFactory<>("cep"));
        colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colBairro.setCellValueFactory(new PropertyValueFactory<>("bairro"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colUf.setCellValueFactory(new PropertyValueFactory<>("estado"));

        aplicarMascara(txtCnpj, "CNPJ");
        aplicarMascara(txtCep, "CEP");
        aplicarMascara(txtCelular, "CELULAR");
        aplicarMascara(txtTelefone, "TELEFONE");

        listarTabela();

        tabelaFornecedores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) preencherDadosFormulario();
        });
    }

    public void listarTabela() {
        FornecedoresDAO dao = new FornecedoresDAO();
        tabelaFornecedores.setItems(FXCollections.observableArrayList(dao.Listar()));
    }

    private void preencherDadosFormulario() {
        Fornecedores obj = tabelaFornecedores.getSelectionModel().getSelectedItem();
        if (obj != null) {
            txtCodigo.setText(String.valueOf(obj.getId()));
            txtNome.setText(obj.getNome());
            txtCnpj.setText(obj.getCnpj());
            txtEmail.setText(obj.getEmail());
            txtTelefone.setText(obj.getTelefone());
            txtCelular.setText(obj.getCelular());
            txtCep.setText(obj.getCep());
            txtEndereco.setText(obj.getEndereco());
            txtNumero.setText(String.valueOf(obj.getNumero()));
            txtComplemento.setText(obj.getComplemento());
            txtBairro.setText(obj.getBairro());
            txtCidade.setText(obj.getCidade());
            cbxEstado.setValue(obj.getEstado());
            tabPane.getSelectionModel().select(0);
        }
    }

    @FXML void novoAction(ActionEvent event) {
        limparCampos();
        tabPane.getSelectionModel().select(0);
    }

    @FXML void salvarAction(ActionEvent event) {
        try {
            Fornecedores obj = montarObjeto();
            new FornecedoresDAO().Salvar(obj);
            exibirAlerta("Sucesso", "Fornecedor cadastrado!", Alert.AlertType.INFORMATION);
            limparCampos();
            listarTabela();
        } catch (Exception e) {
            exibirAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML void editarAction(ActionEvent event) {
        if (txtCodigo.getText().isEmpty()) return;
        try {
            Fornecedores obj = montarObjeto();
            obj.setId(Integer.parseInt(txtCodigo.getText()));
            new FornecedoresDAO().Editar(obj);
            exibirAlerta("Sucesso", "Fornecedor editado!", Alert.AlertType.INFORMATION);
            limparCampos();
            listarTabela();
        } catch (Exception e) {
            exibirAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML void excluirAction(ActionEvent event) {
        if (txtCodigo.getText().isEmpty()) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Excluir " + txtNome.getText() + "?", ButtonType.OK, ButtonType.CANCEL);
        if (confirm.showAndWait().get() == ButtonType.OK) {
            Fornecedores obj = new Fornecedores();
            obj.setId(Integer.parseInt(txtCodigo.getText()));
            new FornecedoresDAO().Excluir(obj);
            limparCampos();
            listarTabela();
        }
    }

    @FXML void pesquisarAction(ActionEvent event) {
        tabelaFornecedores.setItems(FXCollections.observableArrayList(new FornecedoresDAO().Filtrar("%" + txtPesquisa.getText() + "%")));
    }

    @FXML void relatorioAction(ActionEvent event) {
        exibirAlerta("Relatório", "Módulo JasperReports em breve!", Alert.AlertType.INFORMATION);
    }

    private Fornecedores montarObjeto() {
        Fornecedores obj = new Fornecedores();
        obj.setNome(txtNome.getText());
        obj.setCnpj(txtCnpj.getText());
        obj.setEmail(txtEmail.getText());
        obj.setTelefone(txtTelefone.getText());
        obj.setCelular(txtCelular.getText());
        obj.setCep(txtCep.getText());
        obj.setEndereco(txtEndereco.getText());
        obj.setBairro(txtBairro.getText());
        obj.setCidade(txtCidade.getText());
        obj.setComplemento(txtComplemento.getText());
        obj.setEstado(cbxEstado.getValue() != null ? cbxEstado.getValue() : "");
        try { obj.setNumero(Integer.parseInt(txtNumero.getText())); } catch (Exception e) { obj.setNumero(0); }
        return obj;
    }

    private void limparCampos() {
        txtCodigo.clear(); txtNome.clear(); txtCnpj.clear(); txtEmail.clear(); txtTelefone.clear(); txtCelular.clear(); txtCep.clear(); txtEndereco.clear(); txtNumero.clear(); txtComplemento.clear(); txtBairro.clear(); txtCidade.clear(); cbxEstado.setValue(null);
    }

    private void exibirAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert a = new Alert(tipo); a.setTitle(titulo); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    private void aplicarMascara(TextField tf, String tipo) {
        tf.textProperty().addListener((obs, old, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;
            String limpo = newValue.replaceAll("[^0-9]", "");
            String m = "";
            if (tipo.equals("CNPJ")) {
                if (limpo.length() > 14) limpo = limpo.substring(0, 14);
                m = limpo.replaceFirst("(\\d{2})(\\d)", "$1.$2").replaceFirst("(\\.\\d{3})(\\d)", "$1.$2").replaceFirst("(\\.\\d{3})(\\d)", "$1/$2").replaceFirst("(/\\d{4})(\\d)", "$1-$2");
            } else if (tipo.equals("CEP")) {
                if (limpo.length() > 8) limpo = limpo.substring(0, 8);
                m = limpo.replaceFirst("(\\d{5})(\\d)", "$1-$2");
            } else if (tipo.equals("CELULAR")) {
                if (limpo.length() > 11) limpo = limpo.substring(0, 11);
                m = limpo.replaceFirst("(\\d{2})(\\d)", "($1) $2").replaceFirst("(\\d{5})(\\d)", "$1-$2");
            } else if (tipo.equals("TELEFONE")) {
                if (limpo.length() > 10) limpo = limpo.substring(0, 10);
                m = limpo.replaceFirst("(\\d{2})(\\d)", "($1) $2").replaceFirst("(\\d{4})(\\d)", "$1-$2");
            }
            final String f = m;
            if (!newValue.equals(f)) { tf.setText(f); Platform.runLater(() -> tf.positionCaret(f.length())); }
        });
    }
}