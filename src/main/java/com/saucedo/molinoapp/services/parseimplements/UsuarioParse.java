package com.saucedo.molinoapp.services.parseimplements;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.saucedo.molino_json_models.security.JRole;
import com.saucedo.molino_json_models.security.JUsuario;

public class UsuarioParse implements IParse<JUsuario>{

	@Override
	public List<JUsuario> parseJsonToArrayEntity(JSONArray rr) {
		List<JUsuario> usuarios = new ArrayList<>();
		for (Object u : rr) {
			JSONObject item = (JSONObject) u;	
			usuarios.add(this.parseJsonToEntity(item));
		}
		return usuarios;
	}
	@Override
	public JUsuario parseJsonToEntity(JSONObject oo) {
		JUsuario usuario = new JUsuario();
		// Campos de usuario
		Long id = (Long) oo.get("id");
		String username = (String) oo.get("username");
		String owner = (String) oo.get("owner");
		Long status = (Long) oo.get("status");

		usuario.setId(id);
		usuario.setUsername(username);
		usuario.setStatus(status);
		usuario.setOwner(owner);

		JSONArray rolesJson = (JSONArray) oo.get("roles");
		for (Object rol : rolesJson) {
			JSONObject roleJson = (JSONObject) rol;
			JRole role = new JRole();
			// Capos de role
			Long roleID = (Long) roleJson.get("id");
			String noleName = (String) roleJson.get("name");
			role.setId(roleID);
			role.setName(noleName);
			usuario.addRole(role);
		}
		return usuario;
	}
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject parseJEntityToJSONObject(JUsuario entity) {
		JSONObject json = new JSONObject();
		if(entity!=null) {
			json.put("id",entity.getId());
			json.put("username",entity.getUsername());
			json.put("password",entity.getPassword());
			json.put("owner",entity.getOwner());
			json.put("status",entity.getStatus());
			JSONArray roles = new JSONArray();
			for(JRole rol:entity.getRoles()) {
				JSONObject role = new JSONObject();
				role.put("name", rol.getName());
				roles.add(role);
			}
			json.put("roles",roles);
		}
		return json;
	}

	

}
