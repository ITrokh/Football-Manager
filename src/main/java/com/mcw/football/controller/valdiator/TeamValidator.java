package com.mcw.football.controller.valdiator;

import com.google.common.base.Strings;
import com.mcw.football.domain.Team;
import com.mcw.football.domain.util.annotations.DiplomaValidating;
import com.mcw.football.exceptions.ValidationException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.mcw.football.exceptions.DIPValidationCode.DIP_VALIDATION_TEAM_NAME;

@DiplomaValidating
public class TeamValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Team.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Team team= (Team) o;
        if(Strings.isNullOrEmpty(team.getName())){
            errors.reject(DIP_VALIDATION_TEAM_NAME.name(), DIP_VALIDATION_TEAM_NAME.getMessage());
        }
        if (errors.hasErrors()){
            throw new ValidationException(errors);
        }
    }
}
