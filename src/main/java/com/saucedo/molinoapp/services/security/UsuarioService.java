package com.saucedo.molinoapp.services.security;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.saucedo.molino_json_models.security.JRole;
import com.saucedo.molino_json_models.security.JUsuario;
import com.saucedo.molino_json_models.security.SessionRequest;
import com.saucedo.molino_json_models.security.SessionResponse;
import com.saucedo.molinoapp.Global;
import com.saucedo.molinoapp.SessionStatus;
import com.saucedo.molinoapp.Error;
import com.saucedo.molinoapp.exceptions.ResponseException;
import com.saucedo.molinoapp.services.Request;

public class UsuarioService {

	private SessionResponse parseJSonToSessionResponse(JSONObject r) {
		JSONArray array_roles = (JSONArray) r.get("roles");
		SessionResponse response = new SessionResponse();
		response.setToken((String) r.get("token"));
		response.setUsername((String) r.get("username"));
		for (Object o : array_roles) {
			String rol = (String) o;
			response.addRole(rol);
		}
		return response;
	}
	public List<JUsuario> parseJsonToArrayUsuarios(JSONArray rr){
		List<JUsuario> usuarios = new ArrayList<>();
		for(Object u:rr) {
			JSONObject item = (JSONObject) u;
			JUsuario usuario = new JUsuario();
			//Campos de usuario
			Long id = (Long) item.get("id");
			String username = (String) item.get("username");
			String owner = (String) item.get("owner");
			Long status =(Long) item.get("status");
			
			usuario.setId(id);
			usuario.setUsername(username);
			usuario.setStatus(status);
			usuario.setOwner(owner);
			
			JSONArray rolesJson = (JSONArray) item.get("roles");
			for(Object rol:rolesJson) {
				JSONObject roleJson = (JSONObject) rol;
				JRole role = new JRole();
				//Capos de role
				Long roleID = (Long) roleJson.get("id");
				String noleName = (String)roleJson.get("name");
				role.setId(roleID);
				role.setName(noleName);
				usuario.addRole(role);
			}
			
			usuarios.add(usuario);
		}
		return usuarios;
	}

	@SuppressWarnings({ "unchecked" })
	public SessionResponse initSession(SessionRequest r) throws ResponseException {
		if (!SessionStatus.isStarted()) {
			JSONObject jsonResponse = null;
			JSONObject request = new JSONObject();
			request.put("username", r.getUsername());
			request.put("password", r.getPassword());
			try {
				jsonResponse = Request.requestPostToLogin(Global.LOGING_PATH, request);
			} catch (Exception e1) {
				throw new ResponseException(Error.ERROR_SESSION_USER_OR_PASSWORD, "");
			}
			return this.parseJSonToSessionResponse(jsonResponse);
		}
		return null;
	}

	public List<JUsuario> findAll() throws ResponseException {
		if(SessionStatus.isStarted()) {
			JSONArray jsonResponse = null;
			try {
				jsonResponse = Request.get(Global.GET_ALL_USERS);
				return this.parseJsonToArrayUsuarios(jsonResponse);
			} catch (Exception e1) {
				throw new ResponseException(Error.ERROR_CONNECTION_API_REST, "");
			}
		}
		return null;
	}
}