import org.example.DAO.UserDAOImpl;
import org.example.connection.TestDatabaseConnection;
import org.example.model.User;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class UserDAOImplTest {

    private static Connection conn_test;
    private UserDAOImpl userDAO;

    @BeforeAll
    static void setUpClass() throws SQLException {
        conn_test = TestDatabaseConnection.getConnection();
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        if (conn_test != null && !conn_test.isClosed()) {
            conn_test.close();
        }
    }

    @BeforeEach
    void setUp() {
//        userDAO = new UserDAOImpl(conn_test);
    }

    @Test
    void testLoginUser_Success() {
        String username = "newUser";
        String password = "newPass";

        User result = userDAO.loginUser(username, password);

        assertNotNull(result);
        assertEquals(username, result.getUser_name());
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User();
        user.setUser_name("newUser");
        user.setPassword("newPass");
        user.setRole("TeamMember");
        user.setStatus("Active");
        user.setAccess_level("TeamMember");

        boolean result = userDAO.registerUser(user);

        assertTrue(result);
    }

    @Test
    void testRegisterUser_SQLException() {
        try {
            conn_test.close();
        } catch (SQLException e) {
            fail("SQLException thrown during connection close: " + e.getMessage());
        }

        User user = new User();
        user.setUser_name("newUser");
        user.setPassword("newPass");
        user.setRole("TeamMember");
        user.setStatus("Active");
        user.setAccess_level("TeamMember");

        boolean result = userDAO.registerUser(user);

        assertFalse(result);

        try {
            conn_test = TestDatabaseConnection.getConnection();
        } catch (SQLException e) {
            fail("SQLException thrown during connection re-open: " + e.getMessage());
        }
    }

}
