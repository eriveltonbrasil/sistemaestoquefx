package sistemaestoquefx;

import br.com.sistema.dao.ItensVendasDAO;
import br.com.sistema.dao.ProdutosDAO;
import br.com.sistema.dao.VendasDAO;
import br.com.sistema.model.Clientes;
import br.com.sistema.model.ItensVendas;
import br.com.sistema.model.Produtos;
import br.com.sistema.model.Vendas;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormularioPagamentosController {

    @FXML private TextField txtTotalPagar;
    @FXML private TextField txtDinheiro;
    @FXML private TextField txtCartao;
    @FXML private TextField txtTroco;

    private double totalVenda;
    private Clientes cliente;
    private ObservableList<FormularioVendasController.ItemCarrinho> carrinho;

    @FXML
    public void initialize() {
        txtDinheiro.textProperty().addListener((obs, old, novo) -> calcularTroco());
        txtCartao.textProperty().addListener((obs, old, novo) -> calcularTroco());
    }

    public void receberDadosVenda(double total, Clientes clienteSelecionado, ObservableList<FormularioVendasController.ItemCarrinho> itens) {
        this.totalVenda = total;
        this.cliente = clienteSelecionado;
        this.carrinho = itens;
        
        txtTotalPagar.setText(String.format("%.2f", total));
        txtDinheiro.setText("0");
        txtCartao.setText("0");
        txtTroco.setText("0,00");
    }

    private void calcularTroco() {
        try {
            double dinheiro = txtDinheiro.getText().isEmpty() ? 0 : Double.parseDouble(txtDinheiro.getText().replace(",", "."));
            double cartao = txtCartao.getText().isEmpty() ? 0 : Double.parseDouble(txtCartao.getText().replace(",", "."));

            double valorPago = dinheiro + cartao;
            double troco = valorPago - totalVenda;

            if (troco > 0) {
                txtTroco.setText(String.format("%.2f", troco));
            } else {
                txtTroco.setText("0,00");
            }
        } catch (NumberFormatException e) {
            // Ignora se digitar texto inválido
        }
    }

    @FXML
    void finalizarPagamentoAction(ActionEvent event) {
        try {
            // --- NOVA VALIDAÇÃO DE PAGAMENTO ---
            // Pega os valores digitados de dinheiro e cartão
            double dinheiro = txtDinheiro.getText().isEmpty() ? 0 : Double.parseDouble(txtDinheiro.getText().replace(",", "."));
            double cartao = txtCartao.getText().isEmpty() ? 0 : Double.parseDouble(txtCartao.getText().replace(",", "."));
            double valorPago = dinheiro + cartao;

            // Se o valor pago for menor que o total da venda, exibe erro e PARA a execução!
            if (valorPago < totalVenda) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Aviso");
                alerta.setHeaderText(null);
                alerta.setContentText(String.format("Pagamento insuficiente!\nFaltam R$ %.2f para completar a venda.", (totalVenda - valorPago)));
                alerta.showAndWait();
                return; // O "return" faz o código parar aqui e não descer para salvar no banco!
            }
            // -----------------------------------

            // 1. DADOS DA VENDA (Tabela tb_vendas)
            Vendas venda = new Vendas();
            venda.setClientes(cliente);
            venda.setData_venda(LocalDate.now().toString()); 
            venda.setTotal_venda(totalVenda);
            venda.setObservacoes("Venda via PDV JavaFX");

            VendasDAO vDao = new VendasDAO();
            vDao.salvar(venda); 

            // 2. PEGA O ID DA VENDA GERADA
            venda.setId(vDao.retornarUltimoIdVenda());

            // 3. SALVANDO OS ITENS E BAIXANDO O ESTOQUE
            ItensVendasDAO itemDao = new ItensVendasDAO();
            ProdutosDAO pDao = new ProdutosDAO();

            for (FormularioVendasController.ItemCarrinho c : carrinho) {
                ItensVendas item = new ItensVendas();
                item.setVendas(venda);
                
                Produtos p = new Produtos();
                p.setId(c.getCodigo());
                item.setProdutos(p);
                
                item.setQtd(c.getQuantidade());
                item.setSubtotal(c.getSubtotal());

                itemDao.salvar(item);

                int estoqueAtual = pDao.retornaQtdAtualEstoque(c.getCodigo());
                int novoEstoque = estoqueAtual - c.getQuantidade();
                pDao.baixaEstoque(c.getCodigo(), novoEstoque);
            }

            // Você já tem o JOptionPane no DAO, então não precisa mostrar de novo aqui se não quiser.
            // Mas deixei aqui caso você remova os JOptionPanes dos DAOs depois.
            // Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            // alerta.setTitle("Sucesso");
            // alerta.setHeaderText(null);
            // alerta.setContentText("Venda registrada e estoque atualizado com sucesso!");
            // alerta.showAndWait();

            Stage stage = (Stage) txtTotalPagar.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText(null);
            alerta.setContentText("Erro ao salvar a venda: " + e.getMessage());
            alerta.showAndWait();
        }
    }
}