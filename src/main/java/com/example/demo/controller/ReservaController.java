package com.example.demo.controller;

import com.example.demo.dto.ReservaDetalhadaDTO;
import com.example.demo.modelEntity.Reserva;
import com.example.demo.modelEntity.Espaco;
import com.example.demo.modelEntity.Pessoa;
import com.example.demo.repository.ReservaRepositorio;
import com.example.demo.repository.EspacoRepositorio;
import com.example.demo.repository.PessoaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport; // Import necessário para corrigir o erro do Iterable

@RestController
@RequestMapping(path = "/reserva")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservaController {

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Autowired
    private EspacoRepositorio espacoRepositorio;

    @Autowired
    private PessoaRepositorio pessoaRepositorio;

    // ========================= CADASTRAR =========================
    @PostMapping("/add")
    public ResponseEntity<String> add(
            @RequestParam String dataDisponivel,
            @RequestParam Boolean aprovado,
            @RequestParam Integer espaco_id_espaco,
            @RequestParam Long pessoa_id_locatario
    ) {
        Optional<Espaco> espaco = espacoRepositorio.findById(espaco_id_espaco);
        Optional<Pessoa> pessoa = pessoaRepositorio.findById(pessoa_id_locatario);

        if (espaco.isEmpty() || pessoa.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Espaço ou usuário não encontrado.");
        }

        Reserva reserva = new Reserva();
        reserva.setDataDisponivel(LocalDate.parse(dataDisponivel, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        reserva.setAprovado(aprovado);
        reserva.setEspaco(espaco.get());
        reserva.setPessoa(pessoa.get());
        reservaRepositorio.save(reserva);

        return ResponseEntity.ok("Reserva efetuada com sucesso");
    }

    // ========================= LISTAR DATAS POR ESPAÇO =========================
    @GetMapping("/porEspaco/{idEspaco}")
    public ResponseEntity<List<String>> listarDatasReservadas(@PathVariable Integer idEspaco) {
        Espaco espaco = espacoRepositorio.findById(idEspaco).orElse(null);
        if (espaco == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> datas = StreamSupport.stream(reservaRepositorio.findByEspaco(espaco).spliterator(), false)
                .map(r -> r.getDataDisponivel().format(formatter))
                .collect(Collectors.toList());

        return ResponseEntity.ok(datas);
    }

    // ========================= DETALHES RESERVA POR ESPAÇO =========================
    @GetMapping("/detalhes/porEspaco/{id}")
    public ResponseEntity<List<ReservaDetalhadaDTO>> getDetalhesReservasPorEspaco(@PathVariable Integer id) {
        List<Reserva> reservas = reservaRepositorio.findByEspacoIdEspaco(id);

        List<ReservaDetalhadaDTO> dtos = reservas.stream().map(r -> {
            ReservaDetalhadaDTO dto = new ReservaDetalhadaDTO();
            dto.setIdReserva(r.getIdReserva());
            dto.setIdLocatario(r.getPessoa().getIdPessoa());
            dto.setNomeLocatario(r.getPessoa().getNome());
            dto.setDataDisponivel(r.getDataDisponivel());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // ========================= ALTERAR =========================
    @PutMapping("/update/{idReserva}")
    public ResponseEntity<String> update(
            @PathVariable Integer idReserva,
            @RequestParam String dataDisponivel,
            @RequestParam Boolean aprovado,
            @RequestParam Integer espaco_id_espaco,
            @RequestParam Long pessoa_id_locatario
    ) {
        Optional<Reserva> optionalReserva = reservaRepositorio.findById(idReserva);
        if (optionalReserva.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Erro] - Reserva não encontrada");
        }

        Optional<Espaco> espaco = espacoRepositorio.findById(espaco_id_espaco);
        Optional<Pessoa> pessoa = pessoaRepositorio.findById(pessoa_id_locatario);

        if (espaco.isEmpty() || pessoa.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("[Erro] - Espaço ou Usuário não encontrado");
        }

        Reserva reserva = optionalReserva.get();
        reserva.setDataDisponivel(LocalDate.parse(dataDisponivel, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        reserva.setAprovado(aprovado);
        reserva.setEspaco(espaco.get());
        reserva.setPessoa(pessoa.get());
        reservaRepositorio.save(reserva);

        return ResponseEntity.ok("Reserva atualizada com sucesso");
    }

    // ========================= DELETAR =========================
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirReservaPorId(@PathVariable Integer id) {
        Optional<Reserva> reservaOptional = reservaRepositorio.findById(id);
        if (reservaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("[Erro] - Reserva não encontrada com o ID informado.");
        }

        reservaRepositorio.deleteById(id);
        return ResponseEntity.ok("Reserva excluída com sucesso.");
    }

    // NO MÉTODO: getEstatisticasPorPessoa (ReservaController.java)

    // ========================= ESTATÍSTICAS POR PERÍODO =========================
    @GetMapping("/estatisticas/{idPessoa}")
    public ResponseEntity<Map<String, Object>> getEstatisticasPorPessoa(
            @PathVariable Long idPessoa,
            @RequestParam(defaultValue = "mensal") String periodo) {

        // ❌ REMOVEMOS A LÓGICA DE DATA PARA ESTE TESTE FINAL ❌

        Optional<Pessoa> pessoaOpt = pessoaRepositorio.findById(idPessoa);
        if (pessoaOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        Pessoa pessoa = pessoaOpt.get();

        List<Espaco> espacosDoUsuario = espacoRepositorio.findByPessoa(pessoa);

        List<Reserva> reservas = new ArrayList<>();
        boolean usuarioEhLocador = !espacosDoUsuario.isEmpty();

        // 💡 TESTE FINAL: Se for locador, busca TODAS as reservas, ignorando o período.
        if (usuarioEhLocador) {
            // Usa o método irrestrito (sem filtro de data)
            reservas = reservaRepositorio.findByEspacoPessoaIdPessoa(idPessoa);
        } else {
            // Usa o método irrestrito (sem filtro de data) para locatário
            reservas = StreamSupport.stream(reservaRepositorio.findByPessoa(pessoa).spliterator(), false)
                    .collect(Collectors.toList());
        }
        // 💡 Agora, 'reservas' deve ter o tamanho 7 (ou o total de suas reservas)

        Map<Integer, Map<String, Object>> agrupadoPorEspaco = new HashMap<>();
        for (Reserva r : reservas) {
            Espaco e = r.getEspaco();
            agrupadoPorEspaco.putIfAbsent(e.getIdEspaco(), new HashMap<>());
            Map<String, Object> dados = agrupadoPorEspaco.get(e.getIdEspaco());

            dados.put("id", e.getIdEspaco());
            dados.put("nome", e.getNomeEspaco());
            dados.put("locacoes", (int) dados.getOrDefault("locacoes", 0) + 1);
            dados.put("lucro", (double) dados.getOrDefault("lucro", 0.0) + e.getValor());
        }

        double lucroTotal = agrupadoPorEspaco.values().stream()
                .mapToDouble(d -> (double) d.get("lucro")).sum();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("espacos", agrupadoPorEspaco.values());
        resposta.put("lucroTotal", lucroTotal);
        resposta.put("totalLocacoes", reservas.size());
        resposta.put("tipoUsuario", usuarioEhLocador ? "LOCADOR" : "LOCATARIO");

        return ResponseEntity.ok(resposta);
    }

    // ========================= ESTATÍSTICAS GERAIS (cards do dashboard) =========================
    /**
     * Busca estatísticas gerais (último ano) para cards do dashboard.
     * ATUALMENTE EM MODO DE TESTE DE DIAGNÓSTICO: Ignora o filtro de data.
     */
    @GetMapping("/estatisticas/geral/{idPessoa}")
    public ResponseEntity<Map<String, Object>> getResumoGeral(@PathVariable Long idPessoa) {
        Optional<Pessoa> pessoaOpt = pessoaRepositorio.findById(idPessoa);
        if (pessoaOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        Pessoa pessoa = pessoaOpt.get();

        List<Espaco> espacosDoUsuario = espacoRepositorio.findByPessoa(pessoa);
        boolean usuarioEhLocador = !espacosDoUsuario.isEmpty();

        // 💡 MANUTENÇÃO DO TESTE DE DIAGNÓSTICO: Continua buscando TODAS as reservas, ignorando o período.
        List<Reserva> reservasUltimoAno = new ArrayList<>();
        if (usuarioEhLocador) {
            reservasUltimoAno = reservaRepositorio.findByEspacoPessoaIdPessoa(idPessoa);
        } else {
            reservasUltimoAno = StreamSupport.stream(reservaRepositorio.findByPessoa(pessoa).spliterator(), false)
                    .collect(Collectors.toList());
        }

        double lucroTotal = reservasUltimoAno.stream()
                .mapToDouble(r -> r.getEspaco().getValor()).sum();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("totalEspacos", espacosDoUsuario.size());
        resposta.put("totalReservas", reservasUltimoAno.size());
        resposta.put("lucroTotal", lucroTotal);
        resposta.put("tipoUsuario", usuarioEhLocador ? "LOCADOR" : "LOCATARIO");

        return ResponseEntity.ok(resposta);
    }

    // ========================= TAXA DE OCUPAÇÃO POR ESPAÇO =========================
    @GetMapping("/ocupacao/{idPessoa}")
    public ResponseEntity<List<Map<String, Object>>> getTaxaOcupacao(@PathVariable Long idPessoa) {
        Optional<Pessoa> pessoaOpt = pessoaRepositorio.findById(idPessoa);
        if (pessoaOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        Pessoa pessoa = pessoaOpt.get();

        List<Espaco> espacos = espacoRepositorio.findByPessoa(pessoa);
        LocalDate hoje = LocalDate.now();
        int diasNoMes = hoje.lengthOfMonth();

        List<Map<String, Object>> resposta = new ArrayList<>();

        for (Espaco e : espacos) {
            // A query findByEspacoIdEspaco retorna todas as reservas.
            long reservasMes = reservaRepositorio.findByEspacoIdEspaco(e.getIdEspaco()).stream()
                    // Filtra as reservas que caem no mês atual e ano atual
                    .filter(r -> r.getDataDisponivel().getMonthValue() == hoje.getMonthValue() && r.getDataDisponivel().getYear() == hoje.getYear())
                    .count();

            double taxaOcupacao = ((double) reservasMes / diasNoMes) * 100;

            Map<String, Object> dados = new HashMap<>();
            dados.put("nomeEspaco", e.getNomeEspaco());
            dados.put("taxaOcupacao", taxaOcupacao);
            resposta.add(dados);
        }

        return ResponseEntity.ok(resposta);
    }
}