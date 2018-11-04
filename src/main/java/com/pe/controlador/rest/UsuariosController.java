/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pe.controlador.rest;

import com.pe.controlador.util.AditionalFunction;
import com.pe.interfaces.ICrud;
import com.pe.interfaces.IJWT;
import com.pe.interfaces.IJsonTransformer;
import com.pe.modelo.dto.UsuariosDTO;
import com.pe.util.BussinessException;
import com.pe.util.BussinessMessage;
import com.pe.util.UtilDefines;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author dbermudez
 */
@RestController
public class UsuariosController {

    @Autowired
    @Qualifier(UtilDefines.INSTANCE_JSONTRANSFORMER)
    private IJsonTransformer jsonTransformer;
    //
    @Autowired
    @Qualifier(UtilDefines.INSTANCE_USUARIOS)
    private ICrud crud;
    //
    @Autowired
    @Qualifier(UtilDefines.INSTANCE_JWT)
    private IJWT jwt;

    //@CrossOrigin
    @RequestMapping(value = "/usuarios/login", method = RequestMethod.POST, consumes = UtilDefines.APLJSON, produces = UtilDefines.APLJSON)
    public void login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody String jsonEntrada) {
        UsuariosDTO objetoDTO = null;
        List<UsuariosDTO> arrayListDTO = new ArrayList<UsuariosDTO>();
        List<BussinessMessage> bussinessMessage = null;
        String jsonSalida = null, contentType = null;
        int iStatusRet = 0;
        String token = null;
        try {
            AditionalFunction.changeContext(this.getClass().getClassLoader());
            objetoDTO = jsonTransformer.fromJSON(jsonEntrada, UsuariosDTO.class);
            arrayListDTO.add(objetoDTO);
            arrayListDTO = crud.consultarTodo(arrayListDTO);
            if (arrayListDTO != null && arrayListDTO.size() > 0) {
                objetoDTO = arrayListDTO.get(0);
                iStatusRet = HttpServletResponse.SC_OK;
                contentType = UtilDefines.APLJSON;
                jsonSalida = jsonTransformer.toJson(objetoDTO);
                jwt.setApiKey(objetoDTO.getPassusuario() + UtilDefines.APIKEY);
                jwt.createJWT(objetoDTO.getIdusuario() + "", UtilDefines.AUTHOR, objetoDTO.getNombreusuario() + " " + objetoDTO.getApellidousuario(), 86400000);
                jwt.setClaims(UtilDefines.USER, objetoDTO.getAccusuario());
                jwt.setClaims(UtilDefines.PASS, objetoDTO.getPassusuario());
                jwt.setClaims(UtilDefines.EMAIL, objetoDTO.getCorreousuario());
                token = jwt.getJWT();
                
            } else {
                iStatusRet = HttpServletResponse.SC_OK;
                contentType = UtilDefines.APLJSON;
                jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_NOT_FIND_USER);
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
            httpServletResponse.setHeader(UtilDefines.HEADER_AUTORIZATION, token);
            AditionalFunction.response(iStatusRet, contentType, jsonSalida, httpServletRequest, httpServletResponse);
        }
    }

    //@CrossOrigin
    @RequestMapping(value = "/usuarios/logout/{id}", method = RequestMethod.POST, produces = UtilDefines.APLJSON)
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("id") String id) {
        UsuariosDTO objetoDTO = null;
        List<BussinessMessage> bussinessMessage = null;
        String jsonSalida = null, contentType = null;
        int iStatusRet = 0;
        String token = null;
        try {
            AditionalFunction.changeContext(this.getClass().getClassLoader());
            objetoDTO = (UsuariosDTO) crud.consultarUnoByPK(id);
            if (objetoDTO != null) {
                jwt.setApiKey(objetoDTO.getPassusuario() + UtilDefines.APIKEY);//debemos poner la clave privada + clave publica
                if (jwt.validateJWT(httpServletRequest.getHeader(UtilDefines.HEADER_AUTORIZATION))) {
                    //logica para destruir el token
                    //se debe retornar un nuevo token con tiempo de vida 0
                    jwt.createJWT(objetoDTO.getIdusuario() + "", UtilDefines.AUTHOR, objetoDTO.getNombreusuario() + " " + objetoDTO.getApellidousuario(), 0);
                    token = jwt.getJWT();
                }
                //mensaje de que el token ya fue destruido
                iStatusRet = HttpServletResponse.SC_OK;
                contentType = UtilDefines.APLJSON;
                jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_DESTROY_TOKEN);

                httpServletResponse.setHeader(UtilDefines.HEADER_AUTORIZATION, token);
            } else {
                iStatusRet = HttpServletResponse.SC_OK;
                contentType = UtilDefines.APLJSON;
                jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_NOT_EXIST_TOKEN);
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
    @RequestMapping(value = "/usuarios", method = RequestMethod.POST, consumes = UtilDefines.APLJSON, produces = UtilDefines.APLJSON)
    public void registrar(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody String jsonEntrada) {
        String sid = null;
        UsuariosDTO objetoDTO = null;
        List<UsuariosDTO> arrayListDTO = new ArrayList<UsuariosDTO>();
        List<BussinessMessage> bussinessMessage = null;
        String jsonSalida = null, contentType = null;
        int iStatusRet = 0;
        boolean tokenValido = false;
        try {
            tokenValido = jwt.validateJWT(httpServletRequest.getHeader(UtilDefines.HEADER_AUTORIZATION));
            if (tokenValido) {
                AditionalFunction.changeContext(this.getClass().getClassLoader());
                objetoDTO = jsonTransformer.fromJSON(jsonEntrada, UsuariosDTO.class);
                arrayListDTO.add(objetoDTO);
                arrayListDTO = crud.consultarTodo(arrayListDTO);
                if (arrayListDTO != null && arrayListDTO.size() > 0) {
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_USER_EXIST);
                } else {
                    sid = crud.insertar(objetoDTO);
                    if (sid != null && !sid.equals(UtilDefines.INSERT_ERROR)) {
                        objetoDTO.setIdusuario(Integer.parseInt(sid));
                        iStatusRet = HttpServletResponse.SC_OK;
                        contentType = UtilDefines.APLJSON;
                        jsonSalida = jsonTransformer.toJson(objetoDTO);
                    } else {
                        iStatusRet = HttpServletResponse.SC_OK;
                        contentType = UtilDefines.APLJSON;
                        jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_CANNOT_REGISTER);
                    }
                }
            } else {
                iStatusRet = HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION;
                contentType = UtilDefines.APLJSON;
                jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_DESTROY_TOKEN);
            }
        } catch (BussinessException ex) {
            bussinessMessage = ex.getBussinessMessages();
            iStatusRet = HttpServletResponse.SC_BAD_REQUEST;
            contentType = UtilDefines.APLJSON;
            jsonSalida = jsonTransformer.toJson(bussinessMessage);
        } catch (Exception ex) {
            jsonSalida = ex.getMessage();
            iStatusRet = HttpServletResponse.SC_BAD_REQUEST;
            contentType = UtilDefines.CONTENT_TYPE_TEXT;
        } finally {
            AditionalFunction.response(iStatusRet, contentType, jsonSalida, httpServletRequest, httpServletResponse);
        }
    }

    //@CrossOrigin
    @RequestMapping(value = "/usuarios", method = RequestMethod.GET, produces = UtilDefines.APLJSON)
    public void consultarTodo(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        List<UsuariosDTO> arrayListDTO = null;
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
                    contentType = UtilDefines.APLJSON;
                    iStatusRet = HttpServletResponse.SC_OK;
                    jsonSalida = jsonTransformer.toJson(arrayListDTO);
                } else {
                    contentType = UtilDefines.APLJSON;
                    iStatusRet = HttpServletResponse.SC_OK;
                    jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_NOT_FIND_USER);
                }
            } else {
                contentType = UtilDefines.APLJSON;
                iStatusRet = HttpServletResponse.SC_UNAUTHORIZED;
                jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_DESTROY_TOKEN);
            }
        } catch (BussinessException ex) {
            contentType = UtilDefines.APLJSON;
            bussinessMessage = ex.getBussinessMessages();
            jsonSalida = jsonTransformer.toJson(bussinessMessage);
            iStatusRet = HttpServletResponse.SC_BAD_REQUEST;
        } catch (Exception ex) {
            contentType = UtilDefines.CONTENT_TYPE_TEXT;
            iStatusRet = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            jsonSalida = ex.getMessage();
        } finally {
            AditionalFunction.response(iStatusRet, contentType, jsonSalida, httpServletRequest, httpServletResponse);
        }
    }
}
