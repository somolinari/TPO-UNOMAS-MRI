package com.tpopdsunomas.model;

import java.util.ArrayList;
import java.util.List;
import com.tpopdsunomas.patterns.strategy.INivelJugador;

/**
 * Clase Cuenta
 * Representa una cuenta de usuario en el sistema
 */
public class Cuenta {
    private int id;
    private String nombre;
    private String email;
    private String clave;
    private INivelJugador nivel;
    private Deporte deporteFavorito;
    private Ubicacion ubicacion;
    private List<Partido> partidosCreados;
    private List<Partido> partidosInscritos;
    
    public Cuenta(int id, String nombre, String email, String clave) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.clave = clave;
        this.partidosCreados = new ArrayList<>();
        this.partidosInscritos = new ArrayList<>();
    }
    
    public void buscarPartido(String ciudad) {
        System.out.println(nombre + " está buscando partidos en: " + ciudad);
    }
    
    public void crearPartido(Partido partido) {
        this.partidosCreados.add(partido);
        System.out.println(nombre + " ha creado un nuevo partido");
    }
    
    public void inscribirseAPartido(Partido partido) {
        if (!this.partidosInscritos.contains(partido)) {
            this.partidosInscritos.add(partido);
            System.out.println(nombre + " se ha inscrito al partido #" + partido.getId());
        }
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getClave() {
        return clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public INivelJugador getNivel() {
        return nivel;
    }
    
    public void setNivel(INivelJugador nivel) {
        this.nivel = nivel;
    }

    public Deporte getDeporteFavorito() {
        return deporteFavorito;
    }
    
    public void setDeporteFavorito(Deporte deporte) {
        this.deporteFavorito = deporte;
    }
    
    public Ubicacion getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public List<Partido> getPartidosCreados() {
        return partidosCreados;
    }
    
    public void agregarPartidoCreado(Partido partido) {
        if (!partidosCreados.contains(partido)) {
            partidosCreados.add(partido);
        }
    }
    
    public List<Partido> getPartidosInscritos() {
        return partidosInscritos;
    }
    
    public void agregarPartidoInscrito(Partido partido) {
        if (!partidosInscritos.contains(partido)) {
            partidosInscritos.add(partido);
        }
    }
    
    @Override
    public String toString() {
        return nombre + " (" + email + ")" + 
               (nivel != null ? " - " + nivel.getNombre() : "");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cuenta cuenta = (Cuenta) obj;
        return id == cuenta.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public String getCuenta() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCuenta'");
    }
}
