package service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AdminServiceTests {
    @Test
    public void testClear() {
        var actual = AdminService.clear();
        var expected = true;
        Assertions.assertEquals(expected, actual);
    }
}
