package com.cea.jwt.controller;

import com.cea.jwt.config.JwtTokenUtil;
import com.cea.jwt.dao.ActiveDirectoryDAO;
import com.cea.jwt.dto.JwtRequest;
import com.cea.jwt.dto.JwtResponse;
import com.cea.jwt.model.Access;
import com.cea.jwt.model.User;
import com.cea.jwt.service.AccessService;
import com.cea.jwt.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

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
    ActiveDirectoryDAO activeDirectoryDAO;

    @Autowired
    private AccessService accessService;

    @ApiOperation(value = "Autenticação do usuário", notes = "Autenticação usuário, geração de token", response = User.class )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operação realizada com sucesso"),
            @ApiResponse(code = 201, message = "Novo recurso criado com sucesso"),
            @ApiResponse(code = 401, message = "Usuário sem autorização"),
            @ApiResponse(code = 403, message = "Acesso proibido"),
            @ApiResponse(code = 404, message = "Recurso não encontrado") })
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> checkADAndGenerationToken(@RequestBody JwtRequest jwtRequest, HttpServletRequest request) throws Exception {

        String membersOf =  activeDirectoryDAO.getMembersOf(jwtRequest, request);

        if(membersOf!=null){
            String token = jwtTokenUtil.generateToken(jwtRequest.getUsername());
            activeDirectoryDAO.setRedis(jwtRequest.getUsername(), membersOf.substring(membersOf.indexOf(";")+1), request,token, "GENERATED TOKEN", membersOf.substring(0,membersOf.lastIndexOf("]")+1));
            return new ResponseEntity<>(new JwtResponse(token), HttpStatus.CREATED);
        }

        return null;
    }
}
