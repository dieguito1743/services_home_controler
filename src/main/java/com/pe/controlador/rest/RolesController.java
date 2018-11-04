/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pe.controlador.rest;

import com.pe.controlador.acciones.RolesAction;
import com.pe.controlador.util.AditionalFunction;
import com.pe.interfaces.ICrud;
import com.pe.interfaces.IJWT;
import com.pe.interfaces.IJsonTransformer;
import com.pe.modelo.dto.RolesDTO;
import com.pe.util.BussinessException;
import com.pe.util.BussinessMessage;
import com.pe.util.UtilDefines;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dbermudez
 */
@RestController
public class RolesController {

    @Autowired
    @Qualifier(UtilDefines.INSTANCE_JSONTRANSFORMER)
    private IJsonTransformer jsonTransformer;
    //
    @Autowired
    @Qualifier(UtilDefines.INSTANCE_ROLES)
    private ICrud crud;
    //
    @Autowired
    @Qualifier(UtilDefines.INSTANCE_JWT)
    private IJWT jwt;

    //@CrossOrigin
    @RequestMapping(value = "/roles", method = RequestMethod.GET, produces = UtilDefines.APLJSON)
    public void consultarTodo(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        List<RolesDTO> arrayListDTO = null;
        List<BussinessMessage> bussinessMessage = null;
        String jsonSalida = null, contentType = null;
        int iStatusRet = 0;
        boolean tokenValido = false;
        try {
            tokenValido = jwt.validateJWT(httpServletRequest.getHeader(UtilDefines.HEADER_AUTORIZATION));
            if (tokenValido) {
                AditionalFunction.changeContext(this.getClass().getClassLoader());
                arrayListDTO = crud.consultarTodo();
                if (arrayListDTO != null && arrayListDTO.size() > 0) {
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = jsonTransformer.toJson(arrayListDTO);
                } else {
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_NOT_FIND_ROL);
                }
            } else {
                iStatusRet = HttpServletResponse.SC_UNAUTHORIZED;
                contentType = UtilDefines.APLJSON;
                jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_DESTROY_TOKEN);
            }
        } catch (BussinessException ex) {
            bussinessMessage = ex.getBussinessMessages();
            iStatusRet = HttpServletResponse.SC_BAD_REQUEST;
            contentType = UtilDefines.APLJSON;
            jsonSalida = jsonTransformer.toJson(bussinessMessage);
        } catch (Exception ex) {
            iStatusRet = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            contentType = UtilDefines.APLJSON;
            jsonSalida = ex.getMessage();
        } finally {
            AditionalFunction.response(iStatusRet, contentType, jsonSalida, httpServletRequest, httpServletResponse);
        }
    }

    //@CrossOrigin
    @RequestMapping(value = "/roles", method = RequestMethod.POST, consumes = UtilDefines.APLJSON, produces = UtilDefines.APLJSON)
    public void registrar(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody String jsonEntrada) {
        String sid = null;
        List<RolesDTO> arrayListDTOIN = new ArrayList<RolesDTO>();
        List<RolesDTO> arrayListDTOOUT = new ArrayList<RolesDTO>();
        List<BussinessMessage> bussinessMessage = null;
        String jsonSalida = null, contentType = null;
        int iStatusRet = 0;
        boolean tokenValido = false;
        try {
            tokenValido = jwt.validateJWT(httpServletRequest.getHeader(UtilDefines.HEADER_AUTORIZATION));
            if (tokenValido) {
                AditionalFunction.changeContext(this.getClass().getClassLoader());
                arrayListDTOIN = jsonTransformer.fromListJSON(jsonEntrada, RolesDTO.class);
                arrayListDTOOUT = crud.consultarTodo(arrayListDTOIN);
                if (arrayListDTOOUT != null && arrayListDTOOUT.size() > 0) {
                    //algunos de estos roles ya existen o no
                    //falta codigo para actualizar roles.
                    for (int i = 0; i < arrayListDTOOUT.size(); i++) {
                        for (int y = 0; y < arrayListDTOIN.size(); y++) {
                            if (arrayListDTOIN.get(y).getModulo().getIdmodulo() == arrayListDTOOUT.get(i).getModulo().getIdmodulo()
                                    && arrayListDTOIN.get(y).getUsuario().getIdusuario() == arrayListDTOOUT.get(i).getUsuario().getIdusuario()
                                    && arrayListDTOIN.get(y).getNivelModulo() != arrayListDTOOUT.get(i).getNivelModulo()) {
                                //ACTUALIZAR ROLES
                                crud.actualizar(arrayListDTOOUT.get(i), arrayListDTOIN.get(y));
                            }
                        }
                    }
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_ROL_UPDATE);
                } else {
                    for (int i = 0; i < arrayListDTOIN.size(); i++) {
                        sid = crud.insertar(arrayListDTOIN.get(i));
                        if (sid != null && !sid.equals(UtilDefines.INSERT_ERROR)) {
                            arrayListDTOIN.get(i).setIdrol(Integer.parseInt(sid));
                        }
                    }
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_SE_GUARDO);
                }
            } else {
                iStatusRet = HttpServletResponse.SC_UNAUTHORIZED;
                contentType = UtilDefines.APLJSON;
                jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_DESTROY_TOKEN);
            }
        } catch (BussinessException ex) {
            bussinessMessage = ex.getBussinessMessages();
            iStatusRet = HttpServletResponse.SC_BAD_REQUEST;
            contentType = UtilDefines.APLJSON;
            jsonSalida = jsonTransformer.toJson(bussinessMessage);
        } catch (Exception ex) {
            iStatusRet = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            contentType = UtilDefines.CONTENT_TYPE_TEXT;
            jsonSalida = ex.getMessage();
        } finally {
            AditionalFunction.response(iStatusRet, contentType, jsonSalida, httpServletRequest, httpServletResponse);
        }
    }

    //@CrossOrigin
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.GET, produces = UtilDefines.APLJSON)
    public void consultarRolxUsuario(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("id") String id) {
        List<RolesDTO> arrayListDTO = new ArrayList<RolesDTO>();
        String jsonSalida = null, contentType = null;
        int iStatusRet = 0;
        boolean tokenValido = false;
        try {
            tokenValido = jwt.validateJWT(httpServletRequest.getHeader(UtilDefines.HEADER_AUTORIZATION));
            if (tokenValido) {
                AditionalFunction.changeContext(this.getClass().getClassLoader());
                arrayListDTO = new RolesAction().obtenerRolesxUsuario(id);
                if (arrayListDTO != null && arrayListDTO.size() > 0) {
                    //se encontro todos los roles de este usuario en cada modulo
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = jsonTransformer.toJson(arrayListDTO);
                } else {
                    //No se encontro los roles de este usuario en los modulos
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_NOT_FIND_ROL);
                }
            } else {
                iStatusRet = HttpServletResponse.SC_UNAUTHORIZED;
                contentType = UtilDefines.APLJSON;
                jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_DESTROY_TOKEN);
            }
        } catch (Exception ex) {
            iStatusRet = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            contentType = UtilDefines.CONTENT_TYPE_TEXT;
            jsonSalida = ex.getMessage();
        } finally {
            AditionalFunction.response(iStatusRet, contentType, jsonSalida, httpServletRequest, httpServletResponse);
        }
    }
}
