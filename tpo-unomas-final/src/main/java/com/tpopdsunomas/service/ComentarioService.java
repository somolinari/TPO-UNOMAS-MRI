package com.tpopdsunomas.service;

import com.tpopdsunomas.model.Comentario;
import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;
import com.tpopdsunomas.patterns.repo.IComentarioRepository;
import com.tpopdsunomas.patterns.repo.ICuentaRepository;
import com.tpopdsunomas.patterns.repo.IPartidoRepository;
import java.util.List;

/**
 * Servicio de Comentarios - Capa de lógica de negocio
 */
public class ComentarioService {

    private IComentarioRepository comentarioRepo;
    private ICuentaRepository cuentaRepo;
    private IPartidoRepository partidoRepo;

    public ComentarioService(IComentarioRepository comentarioRepo, ICuentaRepository cuentaRepo, IPartidoRepository partidoRepo) {
        this.comentarioRepo = comentarioRepo;
        this.cuentaRepo = cuentaRepo;
        this.partidoRepo = partidoRepo;
    }

    /**
     * Crea y guarda un nuevo comentario de un usuario en un partido
     */
    public Comentario crearComentario(int idUsuario, int idPartido, String texto) {
        
        Cuenta autor = cuentaRepo.buscarPorId(idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuario (autor) no encontrado con ID: " + idUsuario));
        
        Partido partido = partidoRepo.buscarPorId(idPartido)
            .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado con ID: " + idPartido));

        // Usamos el constructor que nos diste, pasando 0 para que el repo asigne el ID
        Comentario nuevoComentario = new Comentario(0, autor, partido, texto);
        
        comentarioRepo.guardar(nuevoComentario);
        return nuevoComentario;
    }

    /**
     * Obtiene todos los comentarios de un partido específico
     */
    public List<Comentario> obtenerComentariosPorPartido(int partidoId) {
        return comentarioRepo.buscarPorPartidoId(partidoId);
    }
    
    // (Puedes añadir más métodos como buscarPorId, actualizar, eliminar, etc. si los necesitas)
}