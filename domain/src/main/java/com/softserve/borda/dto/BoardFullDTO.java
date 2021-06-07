package com.softserve.borda.dto;

import com.softserve.borda.validation.ValidationUtils;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class BoardFullDTO {

    private Long id;

    @NotBlank(message = "Board name cannot contain only whitespace characters")
    @NotEmpty(message = "Board name cannot be empty")
    @Size(max = 255, message = "Board name cannot be more than 255 characters")
    @Pattern(regexp = ValidationUtils.REG_EXP_BOARD_NAME,
            message = "Board name can contain only english letters")
    private String name;

    private List<BoardColumnDTO> boardColumns;
    private List<UserBoardRelationDTO> userBoardRelations;
}
