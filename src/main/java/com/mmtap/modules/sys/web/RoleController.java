package com.mmtap.modules.sys.web;

import com.mmtap.modules.sys.model.Role;
import com.mmtap.modules.sys.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 *
 * @author mmtap.com
 * @date 2019/1/9
 **/
@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RoleController {

    private RoleRepository repository;

    @PostMapping
    public Role add(@RequestBody Role role) {
        return repository.save(role);
    }

    @DeleteMapping
    @Transactional(rollbackFor = Exception.class)
    public void delete(@RequestBody List<Long> ids) {
        repository.deleteInBatch(repository.findAllById(ids));
    }

    @PutMapping("{id}")
    public void update(@PathVariable("id") Role old, @RequestBody Role self) {
        old.setName(self.getName());
        old.setAuthority(self.getAuthority());
        old.setMenuSet(self.getMenuSet());
        old.setDescription(self.getDescription());
        repository.saveAndFlush(old);
    }

    @GetMapping("{id}")
    public Role get(@PathVariable("id") Role role) {
        return role;
    }

    @GetMapping
    public Page<Role> findAll(@RequestParam(defaultValue = "") String name, Pageable pageable) {
        return repository.findAllByNameContains(name, pageable);
    }
}
