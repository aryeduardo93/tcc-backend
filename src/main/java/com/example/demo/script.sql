-- -----------------------------------------------------
-- Tabela Pessoa
-- -----------------------------------------------------
CREATE TABLE Pessoa (
                        id_pessoa INT NOT NULL auto_increment,
                        nome VARCHAR(45) NOT NULL,
                        cpf VARCHAR(45) NOT NULL,
                        email VARCHAR(45) NOT NULL,
                        telefone VARCHAR(45) NOT NULL,
                        senha VARCHAR(45) NOT NULL,
                        foto_perfil TEXT NULL,
                            PRIMARY KEY (id_pessoa));

-- -----------------------------------------------------
-- Tabela Espaco
-- -----------------------------------------------------
CREATE TABLE Espaco (
                        id_espaco INT NOT NULL auto_increment,
                        nome_espaco VARCHAR(45) NOT NULL,
                        tipo_espaco VARCHAR(45) NOT NULL,
                        descricao TEXT NOT NULL,
                        regras TEXT NOT NULL,
                        valor DECIMAL(10,2) NOT NULL,
                        caminho_foto text NULL,
                        endereco VARCHAR(45) NOT NULL,
                        bairro VARCHAR(45) NOT NULL,
                        cidade VARCHAR(45) NOT NULL,
                        estado VARCHAR(45) NOT NULL,
                        cep VARCHAR(45) NOT NULL,
                        latitude DECIMAL (10,8) NULL,
                        longitude DECIMAL (10,8) NULL,
                        Pessoa_id_locador INT NOT NULL,
                        PRIMARY KEY (id_espaco),
                        FOREIGN KEY (Pessoa_id_locador) REFERENCES Pessoa (id_pessoa));


-- -----------------------------------------------------
-- Tabela Reserva
-- -----------------------------------------------------
CREATE TABLE Reserva (
                         id_reserva INT NOT NULL AUTO_INCREMENT,
                         data_disponivel DATETIME NOT NULL,
                         aprovado BOOLEAN NULL,
                         Espaco_id_espaco INT NOT NULL,
                         Pessoa_id_locatario INT NOT NULL,
                         PRIMARY KEY (id_reserva),
                         FOREIGN KEY (Espaco_id_espaco) REFERENCES Espaco(id_espaco),
                         FOREIGN KEY (Pessoa_id_locatario) REFERENCES Pessoa(id_pessoa),
                         CONSTRAINT unique_reserva_data UNIQUE (data_disponivel, Espaco_id_espaco));

-- -----------------------------------------------------
-- Tabela Fotos Espaço
-- -----------------------------------------------------
CREATE TABLE Fotos_Espaco (
                              id_foto INT NOT NULL AUTO_INCREMENT,
                              id_espaco INT NOT NULL,
                              caminho_foto VARCHAR(255) NOT NULL,
                              legenda VARCHAR(255),
                              PRIMARY KEY (id_foto),
                              FOREIGN KEY (id_espaco) REFERENCES Espaco (id_espaco) ON DELETE CASCADE
);

