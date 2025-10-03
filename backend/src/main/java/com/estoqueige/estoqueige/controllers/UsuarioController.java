package com.estoqueige.estoqueige.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estoqueige.estoqueige.models.usuario.RequestCadastroUsuarioDTO;
import com.estoqueige.estoqueige.models.usuario.RequestAtualizaUsuarioDTO;
import com.estoqueige.estoqueige.models.usuario.RequestAtualizaSenhaUsuarioDTO;
import com.estoqueige.estoqueige.models.usuario.ResponseUsuarioDTO;
import com.estoqueige.estoqueige.models.usuario.Usuario;
import com.estoqueige.estoqueige.services.UsuarioServices;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioServices usuarioServices;

    public UsuarioController(UsuarioServices usuarioServices) {
        this.usuarioServices = usuarioServices;
    }

    
    @GetMapping("/{idUsuario}")
    public ResponseEntity<ResponseUsuarioDTO> buscarUsuariosPorId(@PathVariable Long idUsuario) {
        Usuario usuario = this.usuarioServices.buscarUsuarioPorId(idUsuario);
        return ResponseEntity.ok().body(ResponseUsuarioDTO.fromEntity(usuario));
    }

    @GetMapping("/")
    public ResponseEntity<List<ResponseUsuarioDTO>> buscarUsuarios(@RequestParam Boolean status) {
        List<Usuario> usuarios = this.usuarioServices.buscarTodosUsuarios(status);
        return ResponseEntity.ok().body(ResponseUsuarioDTO.fromEntity(usuarios));
    }

    @PostMapping("/")
    public ResponseEntity<ResponseUsuarioDTO> criarUsuario(@Valid @RequestBody RequestCadastroUsuarioDTO dto){
        ResponseUsuarioDTO usuarioCriado = this.usuarioServices.cadastrarUsuario(dto);
    
        return ResponseEntity.ok().body(usuarioCriado);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<ResponseUsuarioDTO> atualizarUsuario(@RequestBody RequestAtualizaUsuarioDTO usuario, @PathVariable Long idUsuario){
        return ResponseEntity.ok(this.usuarioServices.atualizarUsuario(idUsuario, usuario));
    }

    @PutMapping("/inativar/{idUsuario}")
    public ResponseEntity<Void> inativarUsuario(@Valid @PathVariable Long idUsuario){
        this.usuarioServices.alterarStatusAtivoUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/alterarSenha/{idUsuario}")
    public ResponseEntity<Void> alterarSenhaUsuario(@Valid @PathVariable Long idUsuario, @RequestBody RequestAtualizaSenhaUsuarioDTO dto){
        this.usuarioServices.alterarSenhaDeUsuario(idUsuario, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/resetarSenha/{idUsuario}")
    public ResponseEntity<Void> resetarSenhaUsuario(@Valid @PathVariable Long idUsuario){
        this.usuarioServices.resetarSenhaDeUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
