package com.cea.jwt.controller;

import com.cea.jwt.model.User;
import com.cea.jwt.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = {"USER"})
@SwaggerDefinition(tags = {
		@Tag(name = "USER", description = "Lista todos os usuários que solicitaram acesso a api")
})
@RestController
@RequestMapping(value = "/users")
public class AccessController {

	private static final Logger LOG = LoggerFactory.getLogger(AccessController.class);

	@Autowired
	UserService service;

	@ApiOperation(value = "Lista todos os usuários", notes = "Lista todos os usuários que solicitaram acesso", response = User.class, responseContainer = "List" )
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Busca realizada com sucesso"),
			@ApiResponse(code = 401, message = "Usuário sem autorização"),
			@ApiResponse(code = 403, message = "Acesso proibido"),
			@ApiResponse(code = 404, message = "Recurso não encontrado") })
	@GetMapping
	public ResponseEntity<List<User>> getUsers() {
		LOG.info("Fetching all users from the redis.");
		List<User> users = new ArrayList<>();
		service.getAll().forEach(users::add);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

}
