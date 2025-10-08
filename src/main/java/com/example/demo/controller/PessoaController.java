package com.example.demo.controller;

import com.example.demo.modelEntity.Pessoa;
import com.example.demo.repository.PessoaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//import java.util.Date;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping(path="/pessoa")
public class PessoaController {

    // CAMINHO ONDE AS FOTOS DE PERFIL SERÃO SALVAS
    private static final String UPLOAD_DIR = "uploads/";


    @Autowired
    private PessoaRepositorio pessoaRepositorio;

    //CADASTRO DE PESSOAS
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "/add")
    public ResponseEntity<?> addNewUser(
            @RequestParam String nome,
            @RequestParam String cpf,
            @RequestParam String email,
            @RequestParam String telefone,
            @RequestParam String senha
    ) {
        // VERIFICA SE O EMAIL JA É EXISTENTE
        if (pessoaRepositorio.findByEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email já existe");
        }

        // CRIA UMA NOVA PESSOA
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setCpf(cpf);
        pessoa.setEmail(email);
        pessoa.setTelefone(telefone);
        pessoa.setSenha(senha);

        // SALVA DENTRO DO BANCO PARA GERAR UM ID
        Pessoa pessoaSalva = pessoaRepositorio.save(pessoa);

        // CRIA UMA PASTA PARA SALVAR A FOTO DE PERFIL
        criarPastaPessoa(pessoaSalva.getIdPessoa());


        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    // CRIA O DIRETORIO DA PESSOA
    private void criarPastaPessoa(Long idPessoa) {
        try {
            String pasta = UPLOAD_DIR + "pessoa_" + idPessoa + "/perfil/";
            Files.createDirectories(Paths.get(pasta));
            System.out.println("Pasta criada: " + pasta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //LISTAR TODOS OS USUARIOS
    @CrossOrigin(origins = "http://localhost:3000") //CONEXÃO COM O REACT
    @GetMapping(path="/all")
    public Iterable<Pessoa> getAllUsers(){

        return pessoaRepositorio.findAll();
    }

    //PESQUISAR POR EMAIL
    @GetMapping(path="/pegarporemail")
    public Pessoa pegarPorEmail(
            @RequestParam String email
    ){
        return pessoaRepositorio.findByEmail(email);
    }


    @PostMapping(path="/verificalogin")
    public Pessoa verificaLogin(
            @RequestParam String email,
            @RequestParam String senha
    ){
        return pessoaRepositorio.findByEmailAndSenha(email, senha);
    }


    //PERFIL - TRAZER DADOS DO USUARIO LOGADO
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path="/perfil/{idPessoa}")
    public ResponseEntity<Pessoa> pegarPerfilPorId(@PathVariable Long idPessoa) {
        Optional<Pessoa> pessoaOptional = pessoaRepositorio.findById(idPessoa);
        if (pessoaOptional.isPresent()) {
            return new ResponseEntity<>(pessoaOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //ALTERAR DADOS DO USUARIO
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping(path = "/update/{idPessoa}")
    public ResponseEntity<?> update(
            @PathVariable Long idPessoa,
            @RequestParam String nome,
            @RequestParam String telefone,
            @RequestParam String senha,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem
    ) {
        Optional<Pessoa> optionalPessoa = pessoaRepositorio.findById(idPessoa);
        if (optionalPessoa.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não encontrado.");
        }

        Pessoa pessoa = optionalPessoa.get();

        pessoa.setNome(nome);
        pessoa.setTelefone(telefone);
        pessoa.setSenha(senha);

        // SALVAR UMA NOVA IMAGEM
        if (imagem != null && !imagem.isEmpty()) {
            try {
                String pasta = UPLOAD_DIR + "pessoa_" + idPessoa + "/perfil/";
                Files.createDirectories(Paths.get(pasta));

                String fileName = imagem.getOriginalFilename().replaceAll("\\s+", "_").toLowerCase();
                Path filePath = Paths.get(pasta).resolve(fileName);

                Files.write(filePath, imagem.getBytes());

                String caminhoRelativo = "pessoa_" + idPessoa + "/perfil/" + fileName;
                pessoa.setFoto_perfil(caminhoRelativo);

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar a foto.");
            }
        }


        pessoaRepositorio.save(pessoa);

        return ResponseEntity.ok(pessoa);
    }


    //DELETAR
    @DeleteMapping(path="/delete")
    public String deletePessoa(@RequestParam Long idPessoa) {
        Optional<Pessoa> optionalPessoa = pessoaRepositorio.findById(idPessoa);
        if (optionalPessoa.isPresent()) {
            pessoaRepositorio.deleteById(idPessoa);
            return "Usúario com ID " + idPessoa + " foi deletado com sucesso";
        } else {
            return "[Erro] - Usúario com ID " + idPessoa + " não encontrado";
        }
    }

}
