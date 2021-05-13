package com.softserve.borda.dto;

import com.softserve.borda.entities.Permission;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardRoleDTO {

    private String name;
    private List<PermissionDTO> permissions;
}
