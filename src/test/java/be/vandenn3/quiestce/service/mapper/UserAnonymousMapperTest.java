package be.vandenn3.quiestce.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserAnonymousMapperTest {

    private UserAnonymousMapper userAnonymousMapper;

    @BeforeEach
    public void setUp() {
        userAnonymousMapper = new UserAnonymousMapperImpl();
    }
}
