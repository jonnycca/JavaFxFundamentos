package br.com.agenda.repositorios.interfaces;

import java.util.List;
//T � utilizado como parametro generico e a interface possa manipular uma lista com qualquer tipo de dado
public interface AgendaRepositorio<T> {
	//o T � utilizado como elemento generico para manipula��o de qualquer tipo de dado
	
	List<T> selecionar();
	void inserir(T entidade);
	void atualizar(T entidade);
	void excluir(T entidade);
	
}
