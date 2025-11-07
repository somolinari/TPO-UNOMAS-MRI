package com.tpopdsunomas.patterns.adapter.geolocalizacion;

import com.tpopdsunomas.model.Ubicacion;

public interface IGeolocalizacionAdapter {

    double calcularDistancia(Ubicacion origen, Ubicacion destino) throws Exception;
} 
