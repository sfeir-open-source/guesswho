package be.vandenn3.quiestce.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PlayerMapperTest {

    private PlayerMapper playerMapper;

    @BeforeEach
    public void setUp() {
        playerMapper = new PlayerMapperImpl();
    }
}
