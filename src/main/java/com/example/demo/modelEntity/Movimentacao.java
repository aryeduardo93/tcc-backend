package com.example.demo.modelEntity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "fk_id_usuario")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name="fk_id_produto")
    private Produto produto;
    @Enumerated(value = EnumType.STRING)
    private TipoMovimentacao acao;
    private int quantidade;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public TipoMovimentacao getAcao() {
        return acao;
    }

    public void setAcao(TipoMovimentacao acao) {
        this.acao = acao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movimentacao that = (Movimentacao) o;
        return id == that.id && quantidade == that.quantidade && Objects.equals(usuario, that.usuario) && Objects.equals(produto, that.produto) && acao == that.acao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuario, produto, acao, quantidade);
    }
}
