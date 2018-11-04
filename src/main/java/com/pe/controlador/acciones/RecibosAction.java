/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pe.controlador.acciones;

import com.pe.controlador.util.CrudInstance;
import com.pe.modelo.dto.RecibosDTO;
import com.pe.modelo.dto.UsuariosDTO;
import com.pe.modelo.dto.Usuarios_ServiciosDTO;
import java.util.List;

/**
 *
 * @author dbermudez
 */
public class RecibosAction {

    public List<RecibosDTO> obtenerRecibosConDetalle(String id) {
        List<RecibosDTO> arrayListRetorno = null;
        List<Usuarios_ServiciosDTO> arrayListUsuario_servicio = null;
        UsuariosDTO usuarioDTO = null;
        CrudInstance crud = null;
        try {
            crud = new CrudInstance();
            usuarioDTO = new UsuariosDTO();
            usuarioDTO.setIdusuario(Integer.parseInt(id));
            arrayListUsuario_servicio.add(new Usuarios_ServiciosDTO(-1, null, usuarioDTO, null, null, -1));
            arrayListUsuario_servicio = crud.getUsuarios_servicios().consultarTodo(arrayListUsuario_servicio);
            arrayListRetorno = crud.getRecibos().consultarTodo(arrayListRetorno);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return arrayListRetorno;
        }
    }
}
