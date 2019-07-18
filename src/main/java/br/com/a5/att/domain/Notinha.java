package br.com.a5.att.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Notinha {

	private String nome = "";
	private String cnpj = "";
	private String endereco = "";
	private int qntProdutos = 0;
	private List<Produto> listaProdutos = new ArrayList<Produto>();
	private String data = "";
	private boolean excecao = false;
	private boolean ehMercado = false;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public int getQntProdutos() {
		return qntProdutos;
	}

	public void setQntProdutos(int qntProdutos) {
		this.qntProdutos = qntProdutos;
	}

	public List<Produto> getListaProdutos() {
		return listaProdutos;
	}

	public void setListaProdutos(List<Produto> listaProdutos) {
		this.listaProdutos = listaProdutos;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isExcecao() {
		return excecao;
	}

	public void setExcecao(boolean excecao) {
		this.excecao = excecao;
	}

	public boolean isEhMercado() {
		return ehMercado;
	}

	public void setEhMercado(boolean ehMercado) {
		this.ehMercado = ehMercado;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
