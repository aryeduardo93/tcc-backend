package com.example.demo.controller;

import com.example.demo.dto.EspacoDTO;
import com.example.demo.modelEntity.Pessoa;
import com.example.demo.modelEntity.Espaco;
import com.example.demo.modelEntity.Reserva;
import com.example.demo.repository.EspacoRepositorio;
import com.example.demo.repository.PessoaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(path = "/espaco")
public class EspacoController {
    @Autowired
    EspacoRepositorio espacoRepositorio;
    private static String UPLOAD_DIR = "uploads/";

    @Autowired
    PessoaRepositorio pessoaRepositorio;

    //CADASTRAR
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "/add")
    public ResponseEntity<Espaco> registerEspaco(
            @RequestParam("nomeEspaco") String nomeEspaco,
            @RequestParam("tipoEspaco") String tipoEspaco,
            @RequestParam("descricao") String descricao,
            @RequestParam("regras") String regras,
            @RequestParam("valor") Double valor,
            @RequestParam("imagem") MultipartFile imagem,
            @RequestParam("endereco") String endereco,
            @RequestParam("bairro") String bairro,
            @RequestParam("cidade") String cidade,
            @RequestParam("estado") String estado,
            @RequestParam("cep") String cep,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam("pessoa_id_locador") Long pessoa_id_locador
    ) {
        Optional<Pessoa> pessoa = pessoaRepositorio.findById(pessoa_id_locador);
        if (pessoa.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 1. Cria o objeto Espaco sem a imagem
        Espaco espaco = new Espaco();
        espaco.setNomeEspaco(nomeEspaco);
        espaco.setTipoEspaco(tipoEspaco);
        espaco.setDescricao(descricao);
        espaco.setRegras(regras);
        espaco.setValor(valor);
        espaco.setEndereco(endereco);
        espaco.setBairro(bairro);
        espaco.setCidade(cidade);
        espaco.setEstado(estado);
        espaco.setCep(cep);
        espaco.setLatitude(latitude);
        espaco.setLongitude(longitude);
        espaco.setPessoa(pessoa.get());

        // 2. Salva o espaço no banco para gerar o ID do espaço
        Espaco savedEspaco = espacoRepositorio.save(espaco);

        try {
            // 3. Salva a imagem principal com nome fixo
            String caminhoFoto = saveFotoPrincipalEspaco(imagem, pessoa_id_locador, savedEspaco.getIdEspaco());

            // 4. Atualiza o caminho da imagem no banco
            savedEspaco.setCaminhoFoto(caminhoFoto);

            // 5. Salva novamente com o caminho da imagem
            Espaco finalEspaco = espacoRepositorio.save(savedEspaco);

            return new ResponseEntity<>(finalEspaco, HttpStatus.CREATED);
        } catch (IOException e) {
            espacoRepositorio.delete(savedEspaco); // rollback
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String saveFotoPrincipalEspaco(MultipartFile imagem, Long pessoaId, Integer espacoId) throws IOException {
        if (imagem.isEmpty()) {
            throw new IllegalArgumentException("A imagem não pode estar vazia.");
        }

        if (!imagem.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Apenas arquivos de imagem são permitidos.");
        }

        // Define o diretório destino
        String pasta = UPLOAD_DIR + "pessoa_" + pessoaId + "/espaco_" + espacoId + "/";
        Files.createDirectories(Paths.get(pasta));

        // Nome fixo da imagem principal
        String nomeArquivo = "foto_principal.jpg";
        Path caminhoArquivo = Paths.get(pasta, nomeArquivo);

        // Se já existir, substitui
        Files.write(caminhoArquivo, imagem.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // Caminho relativo salvo no banco
        return "pessoa_" + pessoaId + "/espaco_" + espacoId + "/" + nomeArquivo;
    }


    //LISTAR ESPAÇOS POR CLIENTES
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/porCliente/{idCliente}")
    public ResponseEntity<List<Espaco>> listarEspacosPorCliente(@PathVariable Long idCliente) {
        // Busca o cliente pelo ID
        Optional<Pessoa> pessoa = pessoaRepositorio.findById(idCliente);

        if (pessoa.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Retorna 404 caso o cliente não seja encontrado
        }

        // Busca os espaços relacionados ao cliente
        Iterable<Espaco> espacosIterable = espacoRepositorio.findByPessoa(pessoa.get());

        // Converte Iterable para List
        List<Espaco> espacos = new ArrayList<>();
        espacosIterable.forEach(espacos::add);

        // Verifica se encontrou espaços
        if (espacos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(null); // Retorna 204 caso o cliente não tenha espaços cadastrados
        }

        // Retorna os espaços encontrados
        System.out.println("Usuário autenticado: " + SecurityContextHolder.getContext().getAuthentication());

        return ResponseEntity.ok(espacos);
    }



    //PESQUISAR POR ID
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "findById")
    public ResponseEntity<EspacoDTO> findById(@RequestParam Integer id_espaco) {
        Optional<Espaco> espaco = espacoRepositorio.findById(id_espaco);
        return espaco.map(EspacoDTO::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Pesquisa por Nome do Espaço
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/findByNome")
    public List<Espaco> findByNome(@RequestParam String nomeEspaco) {
        return espacoRepositorio.findByNomeEspacoContainingIgnoreCase(nomeEspaco);
    }

    // Pesquisa por Tipo de Espaço
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/findByTipo")
    public List<Espaco> findByTipo(@RequestParam String tipoEspaco) {
        return espacoRepositorio.findByTipoEspacoContainingIgnoreCase(tipoEspaco);
    }

    // Pesquisa por Cidade
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/findByCidade")
    public List<Espaco> findByCidade(@RequestParam String cidade) {
        return espacoRepositorio.findByCidadeContainingIgnoreCase(cidade);
    }

    // Filtros Multiplos
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/find")
    public List<Espaco> findByFiltros(
            @RequestParam(required = false) String nomeEspaco,
            @RequestParam(required = false) String tipoEspaco,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEvento
    ) {
        if (dataEvento != null ) {
            return espacoRepositorio.findDisponiveisPorData(nomeEspaco, tipoEspaco, cidade, dataEvento);
        } else {
            return espacoRepositorio.findByFiltros(nomeEspaco, tipoEspaco, cidade);
        }
    }

    //LISTAR TODOS
    @CrossOrigin(origins = "http://localhost:3000") //CONEXÃO COM O REACT
    @GetMapping(path = "/all")
    public Iterable<Espaco> findAll() {
        return espacoRepositorio.findAll();
    }


    //PESQUISAR POR PESSOA
    @GetMapping(path = "/pegarporpessoa")
    public Iterable<Espaco> pegarPorPessoa(@RequestParam Long fk_id_locador) {
        Optional<Pessoa> pessoa = pessoaRepositorio.findById(fk_id_locador);
        if (pessoa.isPresent()) {
            return espacoRepositorio.findByPessoa(pessoa.get());
        } else {
            return null;
        }
    }


    //ALTERAR
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping(path = "/update/{idEspaco}")
    public ResponseEntity<String> update(
            @PathVariable Integer idEspaco,
            @RequestParam String nomeEspaco,
            @RequestParam String tipoEspaco,
            @RequestParam String descricao,
            @RequestParam String regras,
            @RequestParam Double valor,
            @RequestParam(required = false) MultipartFile imagem,
            @RequestParam String endereco,
            @RequestParam String bairro,
            @RequestParam String cidade,
            @RequestParam String estado,
            @RequestParam String cep,
            @RequestParam Long pessoa_id_locador
    ) {
        Optional<Espaco> optionalEspaco = espacoRepositorio.findById(idEspaco);
        if (optionalEspaco.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Erro] - Espaço não encontrado");
        }

        Espaco espaco = optionalEspaco.get();
        Optional<Pessoa> pessoa = pessoaRepositorio.findById(pessoa_id_locador);
        if (pessoa.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Erro] - Usuário não encontrado");
        }

        // Atualiza os campos
        espaco.setNomeEspaco(nomeEspaco);
        espaco.setTipoEspaco(tipoEspaco);
        espaco.setDescricao(descricao);
        espaco.setRegras(regras);
        espaco.setValor(valor);
        espaco.setEndereco(endereco);
        espaco.setBairro(bairro);
        espaco.setCidade(cidade);
        espaco.setEstado(estado);
        espaco.setCep(cep);
        espaco.setPessoa(pessoa.get());

        try {
            if (imagem != null && !imagem.isEmpty()) {
                // Salva nova imagem de perfil (substitui se já existir)
                String caminhoImagem = salvarFotoPrincipalEspaco(imagem, pessoa_id_locador, idEspaco);
                espaco.setCaminhoFoto(caminhoImagem);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("[Erro] - Falha ao salvar a nova imagem");
        }

        espacoRepositorio.save(espaco);
        return ResponseEntity.ok(espaco.toString() + " [Atualizado]");
    }


    private String salvarFotoPrincipalEspaco(MultipartFile imagem, Long pessoaId, Integer espacoId) throws IOException {
        if (imagem.isEmpty()) {
            throw new IllegalArgumentException("A imagem não pode estar vazia.");
        }

        if (!imagem.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Apenas arquivos de imagem são permitidos.");
        }

        // Cria pasta: uploads/pessoa_{id}/espaco_{id}/
        String pasta = UPLOAD_DIR + "pessoa_" + pessoaId + "/espaco_" + espacoId + "/";
        Files.createDirectories(Paths.get(pasta));

        String nomeArquivo = "foto_principal.jpg";
        Path caminhoArquivo = Paths.get(pasta, nomeArquivo);

        // Substitui a imagem se já existir
        Files.write(caminhoArquivo, imagem.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return "pessoa_" + pessoaId + "/espaco_" + espacoId + "/" + nomeArquivo;
    }


    // MARCAR NO MAPA OS ENDEREÇOS
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/coordenadas")
    public ResponseEntity<List<Map<String, Object>>> getCoordenadasDosEspacos() {
        List<Espaco> espacos = espacoRepositorio.findAll();
        List<Map<String, Object>> resposta = new ArrayList<>();

        for (Espaco espaco : espacos) {
            if (espaco.getLatitude() != null && espaco.getLongitude() != null) {
                Map<String, Object> dados = new HashMap<>();
                dados.put("id", espaco.getIdEspaco());
                dados.put("nome", espaco.getNomeEspaco());
                dados.put("latitude", espaco.getLatitude());
                dados.put("longitude", espaco.getLongitude());
                dados.put("cidade", espaco.getCidade());
                dados.put("endereco", espaco.getEndereco());
                resposta.add(dados);
            }
        }

        return ResponseEntity.ok(resposta);
    }

    // PEGAR POR ID
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/buscarPorIdEspaco/{idEspaco}")
    public ResponseEntity<Map<String, String>> buscarDonoPorIdEspaco(@PathVariable Integer idEspaco) {
        Optional<Espaco> espacoOptional = espacoRepositorio.findById(idEspaco);

        if (espacoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pessoa dono = espacoOptional.get().getPessoa();

        if (dono == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("nome", dono.getNome());
        response.put("fotoPerfil", dono.getFoto_perfil());

        return ResponseEntity.ok(response);
    }


    //DELETAR
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping(path = "/delete/{idEspaco}")
    public ResponseEntity<String> delete(@PathVariable Integer idEspaco) {
        Optional<Espaco> optionalEspaco = espacoRepositorio.findById(idEspaco);
        if (optionalEspaco.isPresent()) {
            Espaco espaco = optionalEspaco.get();

            try {
                Path pastaDoEspaco = Paths.get(UPLOAD_DIR + "espaco_" + idEspaco);

                if (Files.exists(pastaDoEspaco)) {
                    Files.walk(pastaDoEspaco)
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                }
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[Erro] - Falha ao excluir os arquivos da pasta");
            }

            espacoRepositorio.delete(espaco);
            return ResponseEntity.ok("Espaço excluído com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Erro] - Espaço não encontrado");
        }
    }



}