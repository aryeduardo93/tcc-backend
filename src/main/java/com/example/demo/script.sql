create table usuario(
	id bigint auto_increment primary key,
	nome text,
	email text,
	senha text,
	data_nascimento date,
	ultimo_login timestamp,
	created_at timestamp default current_timestamp,
	updated_at timestamp default current_timestamp on update current_timestamp
);

create table categoria(
    id bigint auto_increment primary key,
    nome text
);

create table produto(
	id bigint auto_increment primary key,
	nome text,
	descricao text,
	quantidade_estoque int,
	fk_id_categoria bigint,
	created_at timestamp default current_timestamp,
	updated_at timestamp default current_timestamp on update current_timestamp,
	foreign key fk_produto_categoria (fk_id_categoria) references categoria(id)
);

create table movimentacao(
    id bigint auto_increment primary key,
    fk_id_usuario bigint not null,
    fk_id_produto bigint not null,
    acao text,
    quantidade int,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    foreign key fk_mov_usuario (fk_id_usuario) references usuario (id),
    foreign key fk_mov_produto (fk_id_produto) references produto (id),
    constraint acao_check CHECK (acao IN ('ENTRADA', 'SAIDA'))
);