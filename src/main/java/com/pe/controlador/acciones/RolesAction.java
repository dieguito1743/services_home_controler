/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pe.controlador.acciones;

import com.pe.controlador.util.CrudInstance;
import com.pe.modelo.dto.ModulosDTO;
import com.pe.modelo.dto.RolesDTO;
import com.pe.modelo.dto.UsuariosDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dbermudez
 */
public class RolesAction {

    public List<RolesDTO> obtenerRolesxUsuario(String id) {
        RolesDTO objetoDTO = null;
        List<RolesDTO> arrayListRetorno = new ArrayList<RolesDTO>();
        CrudInstance crud = null;
        try {
            crud = new CrudInstance();
            objetoDTO = new RolesDTO(-1, new UsuariosDTO(Integer.parseInt(id), -1, null, null, null, null, null, null), null, -1);
            arrayListRetorno.add(objetoDTO);
            arrayListRetorno = crud.getRoles().consultarTodo(arrayListRetorno);
        } catch (Exception ex) {
            ex.printStackTrace();
            arrayListRetorno = null;
        } finally {
            return arrayListRetorno;
        }
    }
}
