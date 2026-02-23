package sistemaestoquefx;

import br.com.sistema.dao.VendasDAO;
import br.com.sistema.model.Vendas;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FormularioHistoricoController {

    @FXML private DatePicker dtInicio;
    @FXML private DatePicker dtFim;
    @FXML private TableView<Vendas> tabelaHistorico;

    @FXML
    public void initialize() {
        // Configura as datas para abrirem com o dia de hoje por padrão
        dtInicio.setValue(LocalDate.now());
        dtFim.setValue(LocalDate.now());
        configurarTabela();
    }

    @FXML
    void pesquisarAction(ActionEvent event) {
        try {
            LocalDate dataInicio = dtInicio.getValue();
            LocalDate dataFim = dtFim.getValue();

            if (dataInicio == null || dataFim == null) {
                mostrarAlerta("Aviso", "Por favor, selecione as datas de início e fim!", Alert.AlertType.WARNING);
                return;
            }

            VendasDAO dao = new VendasDAO();
            // Chama o método que você já deixou pronto no DAO!
            List<Vendas> lista = dao.historicoVendas(dataInicio, dataFim);
            
            ObservableList<Vendas> dados = FXCollections.observableArrayList(lista);
            tabelaHistorico.setItems(dados);

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao buscar o histórico: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void configurarTabela() {
        TableColumn<Vendas, Integer> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCodigo.setPrefWidth(80);

        TableColumn<Vendas, String> colCliente = new TableColumn<>("Cliente");
        // O cliente está dentro do objeto Venda, então acessamos o nome dele assim:
        colCliente.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getClientes().getNome())
        );
        colCliente.setPrefWidth(220);

        TableColumn<Vendas, String> colData = new TableColumn<>("Data da Venda");
        colData.setCellValueFactory(new PropertyValueFactory<>("data_venda"));
        colData.setPrefWidth(120);

        TableColumn<Vendas, Double> colTotal = new TableColumn<>("Total (R$)");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total_venda"));
        colTotal.setPrefWidth(100);

        TableColumn<Vendas, String> colObs = new TableColumn<>("Observações");
        colObs.setCellValueFactory(new PropertyValueFactory<>("observacoes"));
        colObs.setPrefWidth(200);

        tabelaHistorico.getColumns().setAll(colCodigo, colCliente, colData, colTotal, colObs);
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}