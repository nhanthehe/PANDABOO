package com.asm.pandaboo.services;

import com.asm.pandaboo.entities.RoleEntity;
import com.asm.pandaboo.jpa.RoleJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleJPA roleJPA;

    public RoleEntity findById(int id) {
        return roleJPA.findById(id).orElse(null);
    }

    public List<RoleEntity> findAll() {
        return roleJPA.findAll();
    }
}
