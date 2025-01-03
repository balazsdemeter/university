package hu.cubix.university.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
@Service
public class MockService {
    private final Random random = new Random();

    public int getNumberOfFreeSemesters(Integer externalId) throws TimeoutException {
        if (random.nextBoolean()) {
            throw new TimeoutException();
        } else {
            return random.nextInt();
        }
    }
}