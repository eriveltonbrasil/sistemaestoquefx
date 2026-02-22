package sistemaestoquefx;

import br.com.sistema.dao.FuncionariosDAO;
import br.com.sistema.model.Funcionarios;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FormularioFuncionariosController {

    @FXML private TabPane tabPane;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCelular;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtCep;
    @FXML private TextField txtEndereco;
    @FXML private TextField txtNumero;
    @FXML private TextField txtBairro;
    @FXML private TextField txtCidade;
    @FXML private TextField txtComplemento;
    @FXML private ComboBox<String> cbxEstado;
    @FXML private TextField txtRg;
    @FXML private TextField txtCpf;
    
    // CAMPOS NOVOS DO FUNCIONÁRIO
    @FXML private PasswordField txtSenha;
    @FXML private ComboBox<String> cbxNivel;
    @FXML private TextField txtCargo;

    @FXML private TextField txtPesquisa;

    @FXML private TableView<Funcionarios> tabelaFuncionarios;
    @FXML private TableColumn<Funcionarios, Integer> colCodigo;
    @FXML private TableColumn<Funcionarios, String> colNome;
    @FXML private TableColumn<Funcionarios, String> colCpf;
    @FXML private TableColumn<Funcionarios, String> colEmail;
    @FXML private TableColumn<Funcionarios, String> colNivel;
    @FXML private TableColumn<Funcionarios, String> colCargo;
    @FXML private TableColumn<Funcionarios, String> colCelular;
    @FXML private TableColumn<Funcionarios, String> colCep;
    @FXML private TableColumn<Funcionarios, String> colEndereco;
    @FXML private TableColumn<Funcionarios, Integer> colNumero;
    @FXML private TableColumn<Funcionarios, String> colBairro;
    @FXML private TableColumn<Funcionarios, String> colCidade;
    @FXML private TableColumn<Funcionarios, String> colUf;

    @FXML
    public void initialize() {
        // Preenche os ComboBoxes
        ObservableList<String> estados = FXCollections.observableArrayList(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        );
        cbxEstado.setItems(estados);

        ObservableList<String> niveis = FXCollections.observableArrayList("Administrador", "Usuário");
        cbxNivel.setItems(niveis);

        // Vincula as colunas da Tabela
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivel_acesso"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
        colCep.setCellValueFactory(new PropertyValueFactory<>("cep"));
        colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colBairro.setCellValueFactory(new PropertyValueFactory<>("bairro"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colUf.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Aplica as Máscaras
        aplicarMascara(txtCpf, "CPF");
        aplicarMascara(txtCep, "CEP");
        aplicarMascara(txtCelular, "CELULAR");
        aplicarMascara(txtTelefone, "TELEFONE");
        aplicarMascara(txtRg, "RG");

        // Lista a Tabela
        listarTabela();

        // Clique Duplo na Tabela
        tabelaFuncionarios.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                preencherDadosFormulario();
            }
        });
    }

    public void listarTabela() {
        FuncionariosDAO dao = new FuncionariosDAO();
        List<Funcionarios> lista = dao.Listar();
        ObservableList<Funcionarios> dadosDaTabela = FXCollections.observableArrayList(lista);
        tabelaFuncionarios.setItems(dadosDaTabela);
    }

    private void preencherDadosFormulario() {
        Funcionarios obj = tabelaFuncionarios.getSelectionModel().getSelectedItem();
        
        if (obj != null) {
            txtCodigo.setText(String.valueOf(obj.getId()));
            txtNome.setText(obj.getNome());
            txtRg.setText(obj.getRg());
            txtCpf.setText(obj.getCpf());
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
            
            // Campos de Funcionário
            txtSenha.setText(obj.getSenha());
            cbxNivel.setValue(obj.getNivel_acesso());
            txtCargo.setText(obj.getCargo());

            tabPane.getSelectionModel().select(0);
        }
    }

    @FXML
    void novoAction(ActionEvent event) {
        limparCampos();
        tabPane.getSelectionModel().select(0);
    }

    @FXML
    void salvarAction(ActionEvent event) {
        try {
            Funcionarios obj = new Funcionarios();
            obj.setNome(txtNome.getText());
            obj.setRg(txtRg.getText());
            obj.setCpf(txtCpf.getText());
            obj.setEmail(txtEmail.getText());
            obj.setTelefone(txtTelefone.getText());
            obj.setCelular(txtCelular.getText());
            obj.setCep(txtCep.getText());
            obj.setEndereco(txtEndereco.getText());
            obj.setComplemento(txtComplemento.getText());
            obj.setBairro(txtBairro.getText());
            obj.setCidade(txtCidade.getText());
            
            // Setando campos novos
            obj.setSenha(txtSenha.getText());
            obj.setCargo(txtCargo.getText());
            
            if (cbxEstado.getValue() != null) obj.setEstado(cbxEstado.getValue());
            else obj.setEstado("");
            
            if (cbxNivel.getValue() != null) obj.setNivel_acesso(cbxNivel.getValue());
            else obj.setNivel_acesso("");
            
            try {
                obj.setNumero(Integer.parseInt(txtNumero.getText()));
            } catch (NumberFormatException e) {
                obj.setNumero(0); 
            }

            FuncionariosDAO dao = new FuncionariosDAO();
            dao.Salvar(obj);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sucesso!");
            alerta.setHeaderText(null);
            alerta.setContentText("Funcionário cadastrado com sucesso!");
            alerta.showAndWait();
            
            limparCampos();
            listarTabela();

        } catch (Exception e) {
            Alert alertaErro = new Alert(Alert.AlertType.ERROR);
            alertaErro.setTitle("Atenção");
            alertaErro.setHeaderText("Erro ao salvar o funcionário");
            alertaErro.setContentText(e.getMessage());
            alertaErro.showAndWait();
        }
    }

    @FXML
    void editarAction(ActionEvent event) {
        if (txtCodigo.getText().isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Atenção");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecione um funcionário na aba de Consulta antes de editar!");
            alerta.showAndWait();
            return;
        }

        try {
            Funcionarios obj = new Funcionarios();
            obj.setId(Integer.parseInt(txtCodigo.getText()));
            obj.setNome(txtNome.getText());
            obj.setRg(txtRg.getText());
            obj.setCpf(txtCpf.getText());
            obj.setEmail(txtEmail.getText());
            obj.setTelefone(txtTelefone.getText());
            obj.setCelular(txtCelular.getText());
            obj.setCep(txtCep.getText());
            obj.setEndereco(txtEndereco.getText());
            obj.setComplemento(txtComplemento.getText());
            obj.setBairro(txtBairro.getText());
            obj.setCidade(txtCidade.getText());
            
            obj.setSenha(txtSenha.getText());
            obj.setCargo(txtCargo.getText());
            
            if (cbxEstado.getValue() != null) obj.setEstado(cbxEstado.getValue());
            else obj.setEstado("");
            
            if (cbxNivel.getValue() != null) obj.setNivel_acesso(cbxNivel.getValue());
            else obj.setNivel_acesso("");
            
            try {
                obj.setNumero(Integer.parseInt(txtNumero.getText()));
            } catch (NumberFormatException e) {
                obj.setNumero(0); 
            }

            FuncionariosDAO dao = new FuncionariosDAO();
            dao.Editar(obj);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sucesso!");
            alerta.setHeaderText(null);
            alerta.setContentText("Funcionário editado com sucesso!");
            alerta.showAndWait();
            
            limparCampos();
            listarTabela();

        } catch (Exception e) {
            Alert alertaErro = new Alert(Alert.AlertType.ERROR);
            alertaErro.setTitle("Atenção");
            alertaErro.setHeaderText("Erro ao editar o funcionário");
            alertaErro.setContentText(e.getMessage());
            alertaErro.showAndWait();
        }
    }

    @FXML
    void excluirAction(ActionEvent event) {
        if (txtCodigo.getText().isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Atenção");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecione um funcionário na aba de Consulta antes de excluir!");
            alerta.showAndWait();
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação");
        confirmacao.setHeaderText("Atenção: Ação irreversível!");
        confirmacao.setContentText("Tem certeza que deseja excluir o funcionário " + txtNome.getText() + "?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                Funcionarios obj = new Funcionarios();
                obj.setId(Integer.parseInt(txtCodigo.getText()));

                FuncionariosDAO dao = new FuncionariosDAO();
                dao.Excluir(obj);

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Sucesso!");
                alerta.setHeaderText(null);
                alerta.setContentText("Funcionário excluído com sucesso!");
                alerta.showAndWait();

                limparCampos();
                listarTabela();

            } catch (Exception e) {
                Alert alertaErro = new Alert(Alert.AlertType.ERROR);
                alertaErro.setTitle("Atenção");
                alertaErro.setHeaderText("Erro ao tentar excluir");
                alertaErro.setContentText(e.getMessage());
                alertaErro.showAndWait();
            }
        }
    }

    @FXML
    void pesquisarAction(ActionEvent event) {
        String nomePesquisa = "%" + txtPesquisa.getText() + "%";
        FuncionariosDAO dao = new FuncionariosDAO();
        List<Funcionarios> lista = dao.Filtar(nomePesquisa);
        ObservableList<Funcionarios> dadosDaTabela = FXCollections.observableArrayList(lista);
        tabelaFuncionarios.setItems(dadosDaTabela);
    }

    @FXML
    void relatorioAction(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Relatório");
        alerta.setHeaderText("Módulo em Construção");
        alerta.setContentText("A geração de relatórios será implementada na Fase 4, usando JasperReports!");
        alerta.showAndWait();
    }

    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtRg.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtCelular.setText("");
        txtCep.setText("");
        txtEndereco.setText("");
        txtNumero.setText("");
        txtComplemento.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        cbxEstado.setValue(null);
        
        // Limpa os campos de Funcionário
        txtSenha.setText("");
        txtCargo.setText("");
        cbxNivel.setValue(null);
    }

    private void aplicarMascara(TextField textField, String tipo) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String textoLimpo = tipo.equals("RG") ? newValue.replaceAll("[^a-zA-Z0-9]", "").toUpperCase() : newValue.replaceAll("[^0-9]", "");
                String mascara = "";

                switch (tipo) {
                    case "CPF":
                        if (textoLimpo.length() > 11) textoLimpo = textoLimpo.substring(0, 11);
                        mascara = textoLimpo.replaceFirst("(\\d{3})(\\d)", "$1.$2")
                                            .replaceFirst("(\\d{3})\\.(\\d{3})(\\d)", "$1.$2.$3")
                                            .replaceFirst("(\\d{3})\\.(\\d{3})\\.(\\d{3})(\\d)", "$1.$2.$3-$4");
                        break;
                    case "CEP":
                        if (textoLimpo.length() > 8) textoLimpo = textoLimpo.substring(0, 8);
                        mascara = textoLimpo.replaceFirst("(\\d{5})(\\d)", "$1-$2");
                        break;
                    case "CELULAR":
                        if (textoLimpo.length() > 11) textoLimpo = textoLimpo.substring(0, 11);
                        mascara = textoLimpo.replaceFirst("(\\d{2})(\\d)", "($1) $2")
                                            .replaceFirst("(\\(\\d{2}\\) \\d{5})(\\d)", "$1-$2");
                        break;
                    case "TELEFONE":
                        if (textoLimpo.length() > 10) textoLimpo = textoLimpo.substring(0, 10);
                        mascara = textoLimpo.replaceFirst("(\\d{2})(\\d)", "($1) $2")
                                            .replaceFirst("(\\(\\d{2}\\) \\d{4})(\\d)", "$1-$2");
                        break;
                    case "RG": 
                        if (textoLimpo.length() > 9) textoLimpo = textoLimpo.substring(0, 9);
                        mascara = textoLimpo.replaceFirst("(\\w{2})(\\w)", "$1.$2")
                                            .replaceFirst("(\\w{2})\\.(\\w{3})(\\w)", "$1.$2.$3")
                                            .replaceFirst("(\\w{2})\\.(\\w{3})\\.(\\w{3})(\\w)", "$1.$2.$3-$4");
                        break;
                }

                final String textoFormatado = mascara;
                final int posicaoCursor = mascara.length();

                if (!newValue.equals(textoFormatado)) {
                    textField.setText(textoFormatado);
                    Platform.runLater(() -> textField.positionCaret(posicaoCursor));
                }
            }
        });
    }
}