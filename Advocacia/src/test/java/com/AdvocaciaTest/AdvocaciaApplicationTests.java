package com.AdvocaciaTest;

import com.Advocacia.AdvocaciaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AdvocaciaApplication.class)
class AdvocaciaApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    public void testMain() {
        AdvocaciaApplication.main(new String[]{});
    }
}
