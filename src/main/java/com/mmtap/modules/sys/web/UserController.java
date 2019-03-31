package com.mmtap.modules.sys.web;

import com.mmtap.modules.sys.model.Role;
import com.mmtap.modules.sys.model.User;
import com.mmtap.modules.sys.views.MenuTreeVO;
import com.mmtap.modules.sys.repository.RoleRepository;
import com.mmtap.modules.sys.repository.UserRepository;
import com.mmtap.modules.sys.service.MenuTreeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户控制器
 *
 * @author mmtap.com
 * @date 2019/1/9
 **/
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserRepository repository;

    private RoleRepository roleRepository;

    private MenuTreeService menuTreeService;

    private PasswordEncoder passwordEncoder;

    @PostMapping
    public User add(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode("123456"));
        user.getDetails().setNickname(user.getUsername());
        List<Role> roles = roleRepository.findAllById(user.getRoleSet().stream().map(Role::getId).collect(Collectors.toList()));
        user.setRoleSet(new HashSet<>(roles));
        return repository.save(user);
    }

    @DeleteMapping
    @Transactional(rollbackFor = Exception.class)
    public void delete(@RequestBody List<Long> ids) {
        repository.deleteInBatch(repository.findAllById(ids));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") User user, @RequestBody User self) {
        user.setRoleSet(self.getRoleSet());
        user.setStatus(self.getStatus());
        user.setDetails(self.getDetails());
        repository.saveAndFlush(user);
    }

    @PutMapping("/{id}/password")
    public void changePw(@PathVariable("id") User user, @RequestBody String password) {
        user.setPassword(passwordEncoder.encode(password));
        repository.saveAndFlush(user);
    }

    @GetMapping("{id}")
    public User get(@PathVariable("id") User user) {
        return user;
    }

    @GetMapping
    public Page<User> findAll(User user, Pageable pageable) {
        return repository.findAll(User.specification(user), pageable);
    }

    @GetMapping("/current")
    public String getCurrentUsername(Principal principal) {
        return ((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getUsername();
    }

    @GetMapping("/current/menu")
    public Collection<MenuTreeVO> listCurrentUserMenu() {
        return menuTreeService.listCurrentUserSystem();
    }

}
