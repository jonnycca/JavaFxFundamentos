package br.com.agendaFX;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import br.com.agenda.entendidades.Contato;
import br.com.agenda.repositorios.impl.ContatoRepositorio;
import br.com.agenda.repositorios.interfaces.AgendaRepositorio;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController implements Initializable { // fazendo referencia a interface Initializable para poder usar o
														// metodo de inicialização
	// Criando os elementos do SceneBuider para poder fazer referencia aos mesmos.
	@FXML
	private TableView<Contato> tableContatos;
	@FXML
	private Button botaoInserir;
	@FXML
	private Button botaoAlterar;
	@FXML
	private Button botaoExcluir;
	@FXML
	private TextField txfNome;
	@FXML
	private TextField txfIdade;
	@FXML
	private TextField txfTelefone;
	@FXML
	private Button botaoSalvar;
	@FXML
	private Button botaoCancelar;

	private Boolean ehInserir;
	private Contato contatoSelecionado;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.tableContatos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // preenchendo a tabela 1 por 1
		habilitarEdicaoAgenda(false);
		// pegando o item selecionado na tabela, change listener é usado para pegar o
		// contato selecionado
		/*
		 * this.tableContatos.getSelectionModel().selectedItemProperty().addListener(new
		 * ChangeListener<Contato>() {
		 * 
		 * //implementação do que acontece quando algum contato é selecionado na
		 * TableView
		 * 
		 * @Override public void changed(ObservableValue<? extends Contato> observable,
		 * Contato oldValue, Contato newValue) {
		 * 
		 * if(newValue != null) {//verifica se selecionou algum elemento
		 * txfNome.setText(newValue.getNome());
		 * txfIdade.setText(String.valueOf(newValue.getIdade()));
		 * txfTelefone.setText(newValue.getTelefone()); } } });;
		 */

		// pegando o item selecionado na tabela usando lambda
		this.tableContatos.getSelectionModel().selectedItemProperty()
				.addListener((observador, contatoAntigo, contatoNovo) -> {
					if (contatoNovo != null) {// se selecionou algum contato
						txfNome.setText(contatoNovo.getNome());
						txfIdade.setText(String.valueOf(contatoNovo.getIdade()));
						txfTelefone.setText(contatoNovo.getTelefone());
						this.contatoSelecionado = contatoNovo; //passa informação de qual contato está selecionado para poder usar nos metodos alterar e excluir
					}
				});
		carregarTabelaContatos();

	}

	//metodo acionado quando o botão inserir for pressionado
	public void botaoInserir_Action() {
		this.ehInserir = true;
		this.txfNome.setText("");
		this.txfIdade.setText("");
		this.txfTelefone.setText("");
		habilitarEdicaoAgenda(true);
	}
	
	//metodo acionado quando o botão alterar for pressionado
	public void botaoAlterar_Action() {
		habilitarEdicaoAgenda(true);
		ehInserir = false;
		this.txfNome.setText(this.contatoSelecionado.getNome());
		this.txfIdade.setText(Integer.toString((this.contatoSelecionado.getIdade())));
		this.txfTelefone.setText(this.contatoSelecionado.getTelefone());
	}
	
	//metodo acionado quando o botão excluir for pressionado
	public void botaoExcluir_Action() {
		Alert confirmação = new Alert(AlertType.CONFIRMATION); //cria um alerta para confirmação de exclusão 
		confirmação.setTitle("Confirmação");	
		confirmação.setHeaderText("Confirmação de exclusão de contato."); 
		confirmação.setContentText("Tem certeza que deseja excluir este contato?");
		Optional<ButtonType> resultadoConfirmacao = confirmação.showAndWait(); //envia a mensagem, e utiliza optional pq showAndWait retorna um optional<ButtonType>
		
		if(resultadoConfirmacao.isPresent() && resultadoConfirmacao.get() == ButtonType.OK) {  //verifica se obteu um resultado no click do usuario e se o resultado foi "Ok"
		AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
		repositorioContato.excluir(this.contatoSelecionado);
		carregarTabelaContatos();
		this.tableContatos.getSelectionModel().selectFirst();

		}
	}
	
	//metodo acionado quando o botão cancelar for pressionado
	public void botaoCancelar_Action() {
		habilitarEdicaoAgenda(false);
		this.tableContatos.getSelectionModel().selectFirst(); //retornar a seleção para o primeiro elemento da tableView
	}
	
	public void botaoSalvar_Acition() {
		AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio(); //instancia ContatoRepositorio para obter os metodos de alterações e inclusão
		Contato contato = new Contato();//cria o novo contato
		contato.setNome(txfNome.getText());  //passa os dados dos textFild para o contato
		contato.setIdade(Integer.parseInt(txfIdade.getText()));
		contato.setTelefone(txfTelefone.getText());
		if(this.ehInserir) {//se inserir tiver true, é inserir, senão, é atualizar
			repositorioContato.inserir(contato);
		}else {
			repositorioContato.atualizar(contato);
		}
		habilitarEdicaoAgenda(false);
		carregarTabelaContatos(); //carrega a tableView
		this.tableContatos.getSelectionModel().selectFirst(); //seleciona o primeiro contato da tableView
	}

	private void carregarTabelaContatos() {
		AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();// cria uma nova instancia de
																					// ContatoReposito para poder
																					// manipular
		List<Contato> contatos = repositorioContato.selecionar(); // obtem os contatos que são retornados de
																	// ContatoRepositorio
		if (contatos.isEmpty()) { // verificando se a lista de contatos está vazia e inserindo um novo contato
			Contato contato = new Contato();
			contato.setNome("Jonny");
			contato.setIdade(22);
			contato.setTelefone("34242342");
			contatos.add(contato);
		}

		ObservableList<Contato> contatosObserverList = FXCollections.observableArrayList(contatos);// criando um
																									// observervableList
																									// para poder
																									// inserir um
																									// tableContatos.getItems()
		this.tableContatos.getItems().setAll(contatosObserverList);
	}

	// metodo para abilitar os campos de adicionar contato
	private void habilitarEdicaoAgenda(Boolean edicaoEstadoHabilitado) {
		this.txfNome.setDisable(!edicaoEstadoHabilitado);
		this.txfIdade.setDisable(!edicaoEstadoHabilitado);
		this.txfTelefone.setDisable(!edicaoEstadoHabilitado);
		this.botaoSalvar.setDisable(!edicaoEstadoHabilitado);
		this.botaoCancelar.setDisable(!edicaoEstadoHabilitado);
		this.botaoInserir.setDisable(edicaoEstadoHabilitado);
		this.botaoExcluir.setDisable(edicaoEstadoHabilitado);
		this.botaoAlterar.setDisable(edicaoEstadoHabilitado);
		this.tableContatos.setDisable(edicaoEstadoHabilitado);

	}

}
