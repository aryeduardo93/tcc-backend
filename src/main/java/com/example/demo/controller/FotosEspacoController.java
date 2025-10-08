package com.example.demo.controller;

import com.example.demo.modelEntity.Espaco;
import com.example.demo.modelEntity.FotosEspaco;
import com.example.demo.repository.EspacoRepositorio;
import com.example.demo.repository.FotosEspacoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fotosEspaco")
public class FotosEspacoController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private FotosEspacoRepositorio fotosEspacoRepository;

    @Autowired
    private EspacoRepositorio espacoRepositorio;

    // ADICIONAR MAIS FOTOS NO ESPAÇO
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFoto(
            @RequestParam("idEspaco") Integer idEspaco,
            @RequestParam("imagem") MultipartFile imagem,
            @RequestParam(value = "legenda", required = false) String legenda
    ) {
        Optional<Espaco> espacoOptional = espacoRepositorio.findById(idEspaco);

        if (espacoOptional.isEmpty()) {
            return new ResponseEntity<>("Espaço não encontrado", HttpStatus.NOT_FOUND);
        }

        Espaco espaco = espacoOptional.get();
        Long idPessoa = espaco.getPessoa().getIdPessoa();

        // VERIFICAÇÃO DE LIMITE DE FOTOS
        List<FotosEspaco> fotosExistentes = fotosEspacoRepository.findByEspacoIdEspaco(idEspaco.longValue());
        if (fotosExistentes.size() >= 10) {  // 👉 aqui define o limite (10, 15, o que quiser)
            return new ResponseEntity<>("Limite máximo de fotos atingido para este espaço.", HttpStatus.BAD_REQUEST);
        }


        String caminhoFoto = salvarFoto(imagem, idPessoa, idEspaco);

        if (caminhoFoto == null) {
            return new ResponseEntity<>("Falha ao salvar a imagem", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        FotosEspaco foto = new FotosEspaco();
        foto.setEspaco(espaco);
        foto.setCaminhoFoto(caminhoFoto);
        foto.setLegenda(legenda);

        fotosEspacoRepository.save(foto);

        return new ResponseEntity<>(foto, HttpStatus.CREATED);
    }


    // LISTAR OS FOTOS DOS ESPAÇOS
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/porEspaco/{idEspaco}")
    public ResponseEntity<List<FotosEspaco>> listarFotosPorEspaco(@PathVariable Integer idEspaco) {
        List<FotosEspaco> fotos = fotosEspacoRepository.findByEspacoIdEspaco(idEspaco.longValue());

        if (fotos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(fotos, HttpStatus.OK);
    }

    // DELETAR UMA FOTO ESPECIFICA
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/delete/{idFoto}")
    public ResponseEntity<String> deletarFoto(@PathVariable Long idFoto) {
        Optional<FotosEspaco> fotoOptional = fotosEspacoRepository.findById(idFoto);

        if (fotoOptional.isEmpty()) {
            return new ResponseEntity<>("Foto não encontrada", HttpStatus.NOT_FOUND);
        }

        FotosEspaco foto = fotoOptional.get();

        // Deletar o arquivo físico
        Path caminhoImagem = Paths.get(UPLOAD_DIR + foto.getCaminhoFoto());
        try {
            Files.deleteIfExists(caminhoImagem);
        } catch (IOException e) {
            return new ResponseEntity<>("Erro ao deletar o arquivo físico", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Deletar do banco
        fotosEspacoRepository.delete(foto);

        return new ResponseEntity<>("Foto deletada com sucesso", HttpStatus.OK);
    }

    // SALVAR FOTO
    private String salvarFoto(MultipartFile imagem, Long idPessoa, Integer idEspaco) {
        if (imagem.isEmpty()) {
            return null;
        }
        try {
            // CAMINHO PARA SALVAR FOTO
            String pasta = UPLOAD_DIR + "pessoa_" + idPessoa + "/espaco_" + idEspaco + "/";
            Files.createDirectories(Paths.get(pasta));

            String nomeArquivo = imagem.getOriginalFilename().replaceAll("\\s+", "_").toLowerCase();
            Path caminhoArquivo = Paths.get(pasta).resolve(nomeArquivo);

            Files.write(caminhoArquivo, imagem.getBytes());

            // Retorna o caminho relativo pra salvar no banco
            return "pessoa_" + idPessoa + "/espaco_" + idEspaco + "/" + nomeArquivo;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
