package sistemaestoquefx;

import br.com.sistema.dao.FornecedoresDAO;
import br.com.sistema.dao.ProdutosDAO;
import br.com.sistema.model.Fornecedores;
import br.com.sistema.model.Produtos;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FormularioProdutosController {

    @FXML private TabPane tabPane;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtDescricao;
    @FXML private TextField txtPreco;
    @FXML private TextField txtQtdEstoque;
    @FXML private ComboBox<String> cbxFornecedor;
    
    @FXML private TextField txtPesquisa;
    
    // Mudamos de <?> para <Produtos> para a tabela reconhecer nosso modelo
    @FXML private TableView<Produtos> tabelaProdutos; 

    @FXML
    public void initialize() {
        carregarFornecedores();
        configurarTabela();
        listarTabela();
        
        // --- EVENTO 1: Pegar os dados ao clicar em uma linha da tabela ---
        tabelaProdutos.setOnMouseClicked(event -> {
            // Se clicar 2 vezes em um produto da tabela
            if (event.getClickCount() == 2 && tabelaProdutos.getSelectionModel().getSelectedItem() != null) {
                Produtos p = tabelaProdutos.getSelectionModel().getSelectedItem();
                
                // Preenche a tela de cadastro com os dados do produto clicado
                txtCodigo.setText(String.valueOf(p.getId()));
                txtDescricao.setText(p.getDescricao());
                txtPreco.setText(String.valueOf(p.getPreco()));
                txtQtdEstoque.setText(String.valueOf(p.getQtd_Estoque()));
                cbxFornecedor.setValue(p.getFornecedores().getNome());
                
                // Muda automaticamente para a aba "Dados do Produto" (Aba índice 0)
                tabPane.getSelectionModel().select(0);
            }
        });
        
        // --- EVENTO 2: Pesquisar na tabela conforme o usuário digita ---
        txtPesquisa.setOnKeyReleased(event -> {
            String nome = "%" + txtPesquisa.getText() + "%"; // O % é para o comando LIKE do SQL
            ProdutosDAO dao = new ProdutosDAO();
            List<Produtos> lista = dao.Filtar(nome); // Chama o método Filtar do DAO
            
            ObservableList<Produtos> dados = FXCollections.observableArrayList(lista);
            tabelaProdutos.setItems(dados);
        });
    }

    @FXML 
    void novoAction(ActionEvent event) {
        limparTela();
    }

    @FXML 
    void salvarAction(ActionEvent event) {
        try {
            Produtos obj = new Produtos();
            obj.setDescricao(txtDescricao.getText());
            obj.setPreco(Double.parseDouble(txtPreco.getText().replace(",", ".")));
            obj.setQtd_Estoque(Integer.parseInt(txtQtdEstoque.getText()));

            String nomeFornecedor = cbxFornecedor.getValue();
            FornecedoresDAO fDao = new FornecedoresDAO();
            List<Fornecedores> listaF = fDao.Filtrar(nomeFornecedor);
            
            if (!listaF.isEmpty()) {
                obj.setFornecedores(listaF.get(0)); 
            } else {
                mostrarAlerta("Aviso", "Selecione um fornecedor válido no ComboBox!", Alert.AlertType.WARNING);
                return; 
            }

            ProdutosDAO dao = new ProdutosDAO();
            dao.Salvar(obj);
            
            limparTela();
            listarTabela(); // Atualiza a tabela após salvar um novo
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Verifique se os campos de Preço e Estoque são apenas números!", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao tentar salvar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML 
    void editarAction(ActionEvent event) {
        try {
            if (txtCodigo.getText().isEmpty()) {
                mostrarAlerta("Aviso", "Pesquise ou selecione um produto na tabela antes de editar!", Alert.AlertType.WARNING);
                return;
            }

            Produtos obj = new Produtos();
            obj.setId(Integer.parseInt(txtCodigo.getText())); 
            obj.setDescricao(txtDescricao.getText());
            obj.setPreco(Double.parseDouble(txtPreco.getText().replace(",", ".")));
            obj.setQtd_Estoque(Integer.parseInt(txtQtdEstoque.getText()));

            String nomeFornecedor = cbxFornecedor.getValue();
            FornecedoresDAO fDao = new FornecedoresDAO();
            List<Fornecedores> listaF = fDao.Filtrar(nomeFornecedor);
            
            if (!listaF.isEmpty()) {
                obj.setFornecedores(listaF.get(0));
            } else {
                mostrarAlerta("Aviso", "Selecione um fornecedor válido no ComboBox!", Alert.AlertType.WARNING);
                return;
            }

            ProdutosDAO dao = new ProdutosDAO();
            dao.Editar(obj);
            
            limparTela();
            listarTabela(); // Atualiza a tabela após editar
            
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao tentar editar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML 
    void excluirAction(ActionEvent event) {
        try {
            if (txtCodigo.getText().isEmpty()) {
                mostrarAlerta("Aviso", "Pesquise ou selecione um produto na tabela antes de excluir!", Alert.AlertType.WARNING);
                return;
            }
            
            Produtos obj = new Produtos();
            obj.setId(Integer.parseInt(txtCodigo.getText()));

            ProdutosDAO dao = new ProdutosDAO();
            dao.Excluir(obj);
            
            limparTela();
            listarTabela(); // Atualiza a tabela após excluir
            
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao excluir o produto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML 
    void pesquisarAction(ActionEvent event) {
        try {
            // Este é o botão pesquisar da aba "Dados do Produto"
            String nome = txtDescricao.getText();
            ProdutosDAO dao = new ProdutosDAO();
            Produtos obj = dao.BuscarProdutos(nome);

            if (obj != null && obj.getDescricao() != null) {
                txtCodigo.setText(String.valueOf(obj.getId()));
                txtDescricao.setText(obj.getDescricao());
                txtPreco.setText(String.valueOf(obj.getPreco()));
                txtQtdEstoque.setText(String.valueOf(obj.getQtd_Estoque()));
                cbxFornecedor.setValue(obj.getFornecedores().getNome());
            } else {
                mostrarAlerta("Aviso", "Produto não encontrado com este nome!", Alert.AlertType.INFORMATION);
                limparTela();
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao pesquisar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML 
    void relatorioAction(ActionEvent event) {
        mostrarAlerta("Info", "Funcionalidade de relatório em desenvolvimento!", Alert.AlertType.INFORMATION);
    }

    // --- MÉTODOS AUXILIARES E UTILITÁRIOS ---

    // Configura as colunas da tabela via código (mais seguro que pelo SceneBuilder)
    private void configurarTabela() {
        TableColumn<Produtos, Integer> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Produtos, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colDescricao.setPrefWidth(200); // Deixa a coluna de descrição mais larguinha

        TableColumn<Produtos, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        TableColumn<Produtos, Integer> colQtd = new TableColumn<>("Qtd. Estoque");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("qtd_Estoque"));

        TableColumn<Produtos, String> colFornecedor = new TableColumn<>("Fornecedor");
        // Como o Fornecedor é um objeto dentro de Produto, precisamos pegar o nome dele assim:
        colFornecedor.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFornecedores().getNome())
        );
        colFornecedor.setPrefWidth(150);

        // Limpa as colunas antigas e adiciona as nossas configuradas
        tabelaProdutos.getColumns().setAll(colCodigo, colDescricao, colPreco, colQtd, colFornecedor);
    }

    // Busca os dados no banco e preenche a tabela
    private void listarTabela() {
        ProdutosDAO dao = new ProdutosDAO();
        List<Produtos> lista = dao.Listar();
        
        // No JavaFX, tabelas usam "ObservableList" para se atualizarem sozinhas
        ObservableList<Produtos> dados = FXCollections.observableArrayList(lista);
        tabelaProdutos.setItems(dados);
    }

    private void carregarFornecedores() {
        FornecedoresDAO dao = new FornecedoresDAO();
        List<Fornecedores> lista = dao.Listar();
        cbxFornecedor.getItems().clear(); 
        for (Fornecedores f : lista) {
            cbxFornecedor.getItems().add(f.getNome());
        }
    }

    private void limparTela() {
        txtCodigo.clear();
        txtDescricao.clear();
        txtPreco.clear();
        txtQtdEstoque.clear();
        cbxFornecedor.getSelectionModel().clearSelection();
        txtDescricao.requestFocus(); 
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
    // Método público para ser chamado pela Área de Trabalho e trocar a aba
    public void selecionarAba(int index) {
        tabPane.getSelectionModel().select(index);
    }
}