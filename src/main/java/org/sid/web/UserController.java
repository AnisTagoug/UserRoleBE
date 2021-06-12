package org.sid.web;

import lombok.Data;
import net.bytebuddy.asm.Advice;
import org.sid.dao.AppUserRepository;
import org.sid.entities.AppUser;
import org.sid.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RepositoryRestResource
@RestController
public class UserController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AppUserRepository appUserRepository;
    @GetMapping("/Users")
    public List<AppUser> getAllUsers (){
        return appUserRepository.findAll();
    }



    @PostMapping("/register")
    public AppUser register(@RequestBody  UserForm userForm){
        return  accountService.saveUser(
                userForm.getUsername(),userForm.getPassword(),userForm.getConfirmedPassword(),userForm.getEmail(),userForm.getEtat());
    }
    @GetMapping("/User/{username}")
    public ResponseEntity<AppUser> getUserById(@PathVariable String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if(appUser==null) throw new UsernameNotFoundException("User doesn't exist with username "+ username);
        return ResponseEntity.ok(appUser);
    }
    @PutMapping("/update/{username}")
    public ResponseEntity<AppUser> updateServor(@PathVariable String username, @RequestBody AppUser userDetails ){
        AppUser appUser = appUserRepository.findByUsername(username);


        appUser.setUsername(userDetails.getUsername());
        appUser.setEmail(userDetails.getEmail());
        appUser.setEtat(userDetails.getEtat());


      AppUser updatedServor = appUserRepository.save(appUser);
        return ResponseEntity.ok(updatedServor);
    }
    @DeleteMapping ("/delete/{username}")
    public ResponseEntity<Map<String, Boolean>> delete(@PathVariable String username){
        AppUser appUser = appUserRepository.findByUsername(username);


       appUserRepository.delete(appUser);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}

@Data
class UserForm{
    private String username;
    private String password;
    private String confirmedPassword;
    private String email;
    private Boolean etat;
}
