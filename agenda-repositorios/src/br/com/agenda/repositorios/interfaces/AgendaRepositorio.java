package br.com.agenda.repositorios.interfaces;

import java.util.List;
//T é utilizado como parametro generico e a interface possa manipular uma lista com qualquer tipo de dado
public interface AgendaRepositorio<T> {
	//o T é utilizado como elemento generico para manipulação de qualquer tipo de dado
	
	List<T> selecionar();
	void inserir(T entidade);
	void atualizar(T entidade);
	void excluir(T entidade);
	
}
