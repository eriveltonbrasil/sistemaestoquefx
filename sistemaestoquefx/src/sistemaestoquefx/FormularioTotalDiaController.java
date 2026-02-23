package sistemaestoquefx;

import br.com.sistema.dao.VendasDAO;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class FormularioTotalDiaController {

    @FXML private DatePicker dtDataVenda;
    @FXML private TextField txtTotalVenda;

    @FXML
    public void initialize() {
        // Já deixa a data de hoje preenchida por padrão quando a tela abre
        dtDataVenda.setValue(LocalDate.now());
    }

    @FXML
    void pesquisarAction(ActionEvent event) {
        try {
            // Pega a data selecionada no calendário
            LocalDate dataSelecionada = dtDataVenda.getValue();
            
            if (dataSelecionada == null) {
                mostrarAlerta("Aviso", "Por favor, selecione uma data!", Alert.AlertType.WARNING);
                return;
            }

            // Chama o seu DAO para buscar o valor somado no banco de dados
            VendasDAO dao = new VendasDAO();
            double totalDoDia = dao.posicaoDoDia(dataSelecionada);

            // Formata o valor para o padrão moeda do Brasil (R$ 0.000,00)
            NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            String valorFormatado = formatadorMoeda.format(totalDoDia);

            // Joga na tela!
            txtTotalVenda.setText(valorFormatado);

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao buscar a posição do dia: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}