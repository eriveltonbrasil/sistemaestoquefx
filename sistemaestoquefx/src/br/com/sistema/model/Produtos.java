/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistema.model;

/**
 *
 * @author ERIVELTON BRASIL
 */
public class Produtos {

    private int id;
    private String descricao;
    private double preco;
    private int qtd_Estoque;
    private Fornecedores fornecedores;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQtd_Estoque() {
        return qtd_Estoque;
    }

    public void setQtd_Estoque(int qtd_Estoque) {
        this.qtd_Estoque = qtd_Estoque;
    }

    public Fornecedores getFornecedores() {
        return fornecedores;
    }

    public void setFornecedores(Fornecedores fornecedores) {
        this.fornecedores = fornecedores;
    }

}
