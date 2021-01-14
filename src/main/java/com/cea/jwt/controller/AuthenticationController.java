package com.cea.jwt.controller;

import com.cea.jwt.config.JwtTokenUtil;
import com.cea.jwt.dto.JwtRequest;
import com.cea.jwt.dto.JwtResponse;
import com.cea.jwt.model.Access;
import com.cea.jwt.model.User;
import com.cea.jwt.service.AccessService;
import com.cea.jwt.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = {"AUTHENTICATION"})
@SwaggerDefinition(tags = {
        @Tag(name = "AUTHENTICATION", description = "Autenticação usuário, geração de token")
})
@RestController
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserService service;

    @Autowired
    private AccessService accessService;

    @ApiOperation(value = "Autenticação do usuário", notes = "Autenticação usuário, geração de token", response = User.class, responseContainer = "List" )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operação realizada com sucesso"),
            @ApiResponse(code = 201, message = "Novo recurso criado com sucesso"),
            @ApiResponse(code = 401, message = "Usuário sem autorização"),
            @ApiResponse(code = 403, message = "Acesso proibido"),
            @ApiResponse(code = 404, message = "Recurso não encontrado") })
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest, HttpServletRequest request) throws Exception {

        Optional<Access> access =  accessService.getUser(jwtRequest.getUsername(), jwtRequest.getPassword());

        if(access.isPresent()){
            String token = jwtTokenUtil.generateToken(jwtRequest.getUsername());
            this.setRedis(jwtRequest.getUsername(), access.get().getPerfil(), request,token, "GENERATED TOKEN");
            return new ResponseEntity<>(new JwtResponse(token), HttpStatus.CREATED);
        } else {
            this.setRedis(jwtRequest.getUsername(), null, request, null, "RESOURCE NOT FOUND - INVALID CREDENTIALS");
            return new ResponseEntity<UserDetails>(HttpStatus.NOT_FOUND);
        }
    }

    private void setRedis(String username, String perfil ,HttpServletRequest request, String token, String observation) {

        User user = new User(username,
                perfil,
                request.getRemoteAddr(),
                token,
                observation,
                new Date());

        service.save(user);
    }
}
