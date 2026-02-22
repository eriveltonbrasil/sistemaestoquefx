package sistemaestoquefx;

import br.com.sistema.dao.ClientesDAO;
import br.com.sistema.dao.ProdutosDAO;
import br.com.sistema.model.Clientes;
import br.com.sistema.model.Produtos;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FormularioVendasController {

    @FXML private TextField txtCpfCliente;
    @FXML private TextField txtNomeCliente;
    @FXML private Label lblDataAtual; 
    
    @FXML private TextField txtPesquisaProduto;
    @FXML private TableView<Produtos> tabelaPesquisaProdutos;
    
    @FXML private TextField txtCodigoProduto;
    @FXML private TextField txtDescricaoProduto;
    @FXML private TextField txtPrecoProduto;
    @FXML private TextField txtEstoqueProduto;
    @FXML private TextField txtQtdVenda;
    @FXML private TextField txtDesconto;
    
    @FXML private TableView<ItemCarrinho> tabelaCarrinho; 
    @FXML private TextField txtTotalVenda;

    private ObservableList<ItemCarrinho> listaCarrinho = FXCollections.observableArrayList();
    private double totalDaVenda = 0.0;
    
    // NOVO: Guarda o cliente pesquisado para salvar no banco depois
    private Clientes clienteSelecionado; 

    @FXML
    public void initialize() {
        lblDataAtual.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtQtdVenda.setText("1");
        txtDesconto.setText("0");
        
        configurarTabelaProdutos();
        listarTabelaProdutosGeral();
        configurarTabelaCarrinho();
        
        txtCpfCliente.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                String numeros = newValue.replaceAll("[^0-9]", "");
                if (numeros.length() > 11) numeros = numeros.substring(0, 11);
                
                StringBuilder cpfFormatado = new StringBuilder();
                if (numeros.length() > 0) cpfFormatado.append(numeros.substring(0, Math.min(3, numeros.length())));
                if (numeros.length() > 3) cpfFormatado.append(".").append(numeros.substring(3, Math.min(6, numeros.length())));
                if (numeros.length() > 6) cpfFormatado.append(".").append(numeros.substring(6, Math.min(9, numeros.length())));
                if (numeros.length() > 9) cpfFormatado.append("-").append(numeros.substring(9, numeros.length()));
                
                txtCpfCliente.setText(cpfFormatado.toString());
                txtCpfCliente.positionCaret(cpfFormatado.length());
            }
        });
        
        txtPesquisaProduto.setOnKeyReleased(event -> {
            String nome = "%" + txtPesquisaProduto.getText() + "%";
            ProdutosDAO dao = new ProdutosDAO();
            List<Produtos> lista = dao.Filtar(nome);
            ObservableList<Produtos> dados = FXCollections.observableArrayList(lista);
            tabelaPesquisaProdutos.setItems(dados);
        });
        
        tabelaPesquisaProdutos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tabelaPesquisaProdutos.getSelectionModel().getSelectedItem() != null) {
                Produtos p = tabelaPesquisaProdutos.getSelectionModel().getSelectedItem();
                txtCodigoProduto.setText(String.valueOf(p.getId()));
                txtDescricaoProduto.setText(p.getDescricao());
                txtPrecoProduto.setText(String.valueOf(p.getPreco()));
                txtEstoqueProduto.setText(String.valueOf(p.getQtd_Estoque()));
                txtQtdVenda.requestFocus(); 
            }
        });
    }

    @FXML
    void pesquisarClienteAction(ActionEvent event) {
        try {
            String cpf = txtCpfCliente.getText();
            ClientesDAO dao = new ClientesDAO();
            clienteSelecionado = dao.BuscarClienteCPF(cpf); // Salva o cliente na variável

            if (clienteSelecionado != null && clienteSelecionado.getNome() != null) {
                txtNomeCliente.setText(clienteSelecionado.getNome());
                txtPesquisaProduto.requestFocus(); 
            } else {
                mostrarAlerta("Aviso", "Cliente não encontrado!", Alert.AlertType.WARNING);
                txtNomeCliente.setText("");
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao pesquisar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void buscarProdutoPorCodigoAction(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtCodigoProduto.getText());
            ProdutosDAO dao = new ProdutosDAO();
            Produtos obj = dao.BuscarProdutosCodigo(id);

            if (obj != null && obj.getDescricao() != null) {
                txtDescricaoProduto.setText(obj.getDescricao());
                txtPrecoProduto.setText(String.valueOf(obj.getPreco()));
                txtEstoqueProduto.setText(String.valueOf(obj.getQtd_Estoque()));
                txtQtdVenda.requestFocus();
            } else {
                mostrarAlerta("Aviso", "Produto não encontrado!", Alert.AlertType.WARNING);
                limparCamposProduto();
            }
        } catch (Exception e) {}
    }

    @FXML
    void limparCamposProdutoAction(ActionEvent event) {
        limparCamposProduto();
    }

    @FXML
    void adicionarItemAction(ActionEvent event) {
        try {
            if (txtCodigoProduto.getText().isEmpty() || txtDescricaoProduto.getText().isEmpty()) return;

            int codigo = Integer.parseInt(txtCodigoProduto.getText());
            String descricao = txtDescricaoProduto.getText();
            double preco = Double.parseDouble(txtPrecoProduto.getText());
            int qtdDesejada = Integer.parseInt(txtQtdVenda.getText());
            double descontoPercentual = Double.parseDouble(txtDesconto.getText());
            int estoqueAtual = Integer.parseInt(txtEstoqueProduto.getText());

            if (qtdDesejada > estoqueAtual) {
                mostrarAlerta("Aviso", "Estoque insuficiente!", Alert.AlertType.WARNING);
                return;
            }

            double valorTotalItens = preco * qtdDesejada;
            double valorDesconto = valorTotalItens * (descontoPercentual / 100);
            double subtotal = valorTotalItens - valorDesconto;

            ItemCarrinho item = new ItemCarrinho(codigo, descricao, qtdDesejada, preco, subtotal);
            listaCarrinho.add(item);
            tabelaCarrinho.setItems(listaCarrinho);

            totalDaVenda += subtotal;
            txtTotalVenda.setText(String.format("R$ %.2f", totalDaVenda));

            limparCamposProduto();
            txtPesquisaProduto.requestFocus();
        } catch (Exception e) {}
    }

    @FXML
    void cancelarVendaAction(ActionEvent event) {
        listaCarrinho.clear();
        totalDaVenda = 0.0;
        txtTotalVenda.setText("R$ 0,00");
        txtCpfCliente.clear();
        txtNomeCliente.clear();
        clienteSelecionado = null; // Zera o cliente
        limparCamposProduto();
    }

    @FXML
    void finalizarVendaAction(ActionEvent event) {
        if (listaCarrinho.isEmpty()) {
            mostrarAlerta("Aviso", "O carrinho está vazio!", Alert.AlertType.WARNING);
            return;
        }
        if (clienteSelecionado == null || clienteSelecionado.getNome() == null) {
            mostrarAlerta("Aviso", "Pesquise e selecione um cliente antes de pagar!", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("FormularioPagamentos.fxml"));
            javafx.scene.Parent root = loader.load();
            
            // Passa tudo: o total, o cliente e os itens do carrinho!
            FormularioPagamentosController controller = loader.getController();
            controller.receberDadosVenda(totalDaVenda, clienteSelecionado, listaCarrinho);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Pagamento");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
            
            cancelarVendaAction(null); // Limpa tudo após a venda
            listarTabelaProdutosGeral(); // Atualiza a tabela com o estoque novo!
            
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao abrir tela: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void configurarTabelaCarrinho() {
        TableColumn<ItemCarrinho, Integer> colCod = new TableColumn<>("Código");
        colCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        
        TableColumn<ItemCarrinho, String> colDesc = new TableColumn<>("Produto");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colDesc.setPrefWidth(200);
        
        TableColumn<ItemCarrinho, Integer> colQtd = new TableColumn<>("QTD");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        
        TableColumn<ItemCarrinho, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        
        TableColumn<ItemCarrinho, Double> colSub = new TableColumn<>("SubTotal");
        colSub.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tabelaCarrinho.getColumns().setAll(colCod, colDesc, colQtd, colPreco, colSub);
    }

    private void configurarTabelaProdutos() {
        TableColumn<Produtos, Integer> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCodigo.setPrefWidth(60);

        TableColumn<Produtos, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colDescricao.setPrefWidth(220);

        TableColumn<Produtos, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colPreco.setPrefWidth(80);

        TableColumn<Produtos, Integer> colQtd = new TableColumn<>("Estoque");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("qtd_Estoque"));
        colQtd.setPrefWidth(70);

        TableColumn<Produtos, String> colFornecedor = new TableColumn<>("Fornecedor");
        colFornecedor.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFornecedores().getNome())
        );
        colFornecedor.setPrefWidth(130);

        tabelaPesquisaProdutos.getColumns().setAll(colCodigo, colDescricao, colPreco, colQtd, colFornecedor);
    }

    private void listarTabelaProdutosGeral() {
        ProdutosDAO dao = new ProdutosDAO();
        List<Produtos> lista = dao.Listar();
        ObservableList<Produtos> dados = FXCollections.observableArrayList(lista);
        tabelaPesquisaProdutos.setItems(dados);
    }

    private void limparCamposProduto() {
        txtCodigoProduto.clear();
        txtDescricaoProduto.clear();
        txtPrecoProduto.clear();
        txtEstoqueProduto.clear();
        txtQtdVenda.setText("1");
        txtDesconto.setText("0");
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static class ItemCarrinho {
        private int codigo;
        private String descricao;
        private int quantidade;
        private double preco;
        private double subtotal;

        public ItemCarrinho(int codigo, String descricao, int quantidade, double preco, double subtotal) {
            this.codigo = codigo;
            this.descricao = descricao;
            this.quantidade = quantidade;
            this.preco = preco;
            this.subtotal = subtotal;
        }

        public int getCodigo() { return codigo; }
        public String getDescricao() { return descricao; }
        public int getQuantidade() { return quantidade; }
        public double getPreco() { return preco; }
        public double getSubtotal() { return subtotal; }
    }
}