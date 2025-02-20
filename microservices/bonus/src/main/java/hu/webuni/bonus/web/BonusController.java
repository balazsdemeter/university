package hu.webuni.bonus.web;

import hu.webuni.bonus.api.BonusApi;
import hu.webuni.bonus.model.Bonus;
import hu.webuni.bonus.repository.BonusRepository;
import hu.webuni.bonus.service.BonusService;

import hu.webuni.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class BonusController implements BonusApi {

    @Autowired
    BonusRepository bonusRepository;

    @Autowired
    BonusService bonusService;

    @Override
    public double getPoints(String user) {
        checkUser(user);
        return bonusRepository.findById(user)
                .orElseGet(Bonus::new)
                .getPoints();
    }

    @Override
    public double addPoints(String user, double pointsToAdd) {
        try {
            checkUser(user);
            return bonusService.addPoints(user, pointsToAdd);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void checkUser(String user) {
        User securityUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!securityUser.getUsername().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
