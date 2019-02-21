package br.com.agenda.repositorios.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.agenda.entendidades.Contato;
import br.com.agenda.repositorios.interfaces.AgendaRepositorio;

public class ContatoRepositorio implements AgendaRepositorio<Contato> {
	// cria uma lista estatica de contatos pelo fato de não estar utilizando banco
	// de dados para armazenar os contatos
	private static List<Contato> contatos = new ArrayList<Contato>();

	@Override
	public List<Contato> selecionar() {
		// retorna uma lista de contatos
		return contatos;
	}

	@Override
	public void inserir(Contato entidade) {
		// adiciona o contato a lista de contatos
		contatos.add(entidade);
	}

	@Override
	public void atualizar(Contato entidade) {
		// faz uma pesquisa na lista de contatos e atraves do metodo stream.filter e o
		// findFirst retorna o primeiro elemento encontrado
		// Optional foi utilizado caso não encontre nenhum elemento ele não retorne
		// nada.
		Optional<Contato> original = contatos.stream().filter(contato -> contato.getNome().equals(entidade.getNome())).findFirst();
		// verifica se tem um contato retornado
		if (original.isPresent()) {
			original.get().setNome(entidade.getNome());
			original.get().setIdade(entidade.getIdade());
			original.get().setTelefone(entidade.getTelefone());
		}
	}

	@Override
	public void excluir(Contato entidade) {
		// remove o contato da lista de contatos
		contatos.remove(entidade);
	}

}
