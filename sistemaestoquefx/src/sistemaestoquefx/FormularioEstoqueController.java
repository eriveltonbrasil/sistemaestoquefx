package sistemaestoquefx;

import br.com.sistema.dao.ProdutosDAO;
import br.com.sistema.model.Produtos;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FormularioEstoqueController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtDescricao;
    @FXML private TextField txtQtdAtual;
    @FXML private TextField txtQtdAdicionar;
    
    @FXML private TableView<Produtos> tabelaProdutos;

    @FXML
    public void initialize() {
        configurarTabela();
        listarTabela();
        
        // Evento de clique na tabela
        tabelaProdutos.setOnMouseClicked(event -> {
            if (tabelaProdutos.getSelectionModel().getSelectedItem() != null) {
                Produtos p = tabelaProdutos.getSelectionModel().getSelectedItem();
                txtCodigo.setText(String.valueOf(p.getId()));
                txtDescricao.setText(p.getDescricao());
                txtQtdAtual.setText(String.valueOf(p.getQtd_Estoque()));
            }
        });
    }

    @FXML
    void pesquisarAction(ActionEvent event) {
        try {
            String nome = txtDescricao.getText();
            ProdutosDAO dao = new ProdutosDAO();
            Produtos obj = dao.BuscarProdutos(nome);

            if (obj != null && obj.getDescricao() != null) {
                txtCodigo.setText(String.valueOf(obj.getId()));
                txtDescricao.setText(obj.getDescricao());
                txtQtdAtual.setText(String.valueOf(obj.getQtd_Estoque()));
            } else {
                mostrarAlerta("Aviso", "Produto não encontrado!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao pesquisar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void adicionarEstoqueAction(ActionEvent event) {
        try {
            if (txtCodigo.getText().isEmpty() || txtQtdAdicionar.getText().isEmpty()) {
                mostrarAlerta("Aviso", "Selecione um produto e informe a quantidade para adicionar!", Alert.AlertType.WARNING);
                return;
            }

            int idProduto = Integer.parseInt(txtCodigo.getText());
            int qtdAtual = Integer.parseInt(txtQtdAtual.getText());
            int qtdAdicionar = Integer.parseInt(txtQtdAdicionar.getText());
            
            int novaQtd = qtdAtual + qtdAdicionar;

            // Chama o método que você já tinha criado no DAO!
            ProdutosDAO dao = new ProdutosDAO();
            dao.adicionarEstoque(idProduto, novaQtd);
            
            // Atualiza a tela
            txtQtdAtual.setText(String.valueOf(novaQtd));
            txtQtdAdicionar.clear();
            listarTabela();
            
            mostrarAlerta("Sucesso", "Estoque atualizado com sucesso!", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "A quantidade deve ser um número inteiro!", Alert.AlertType.ERROR);
        }
    }

    private void configurarTabela() {
        TableColumn<Produtos, Integer> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Produtos, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colDescricao.setPrefWidth(200);

        TableColumn<Produtos, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        TableColumn<Produtos, Integer> colQtd = new TableColumn<>("Qtd. Estoque");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("qtd_Estoque"));

        TableColumn<Produtos, String> colFornecedor = new TableColumn<>("Fornecedor");
        colFornecedor.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFornecedores().getNome())
        );
        colFornecedor.setPrefWidth(150);

        tabelaProdutos.getColumns().setAll(colCodigo, colDescricao, colPreco, colQtd, colFornecedor);
    }

    private void listarTabela() {
        ProdutosDAO dao = new ProdutosDAO();
        List<Produtos> lista = dao.Listar();
        ObservableList<Produtos> dados = FXCollections.observableArrayList(lista);
        tabelaProdutos.setItems(dados);
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}