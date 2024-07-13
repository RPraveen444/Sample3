import org.example.DAO.UserDAOImpl;
import org.example.model.Client;
import org.example.model.MileStone;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserDAOImplNewTest {
    @Mock
    private UserDAOImpl userDAO;

    @InjectMocks
    private UserService userService;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rs;

    @Mock
    private Connection conn;

    @BeforeEach
    public void setUp() throws SQLException {
        userDAO = new UserDAOImpl();
//        userService = new UserService();
//        userService.userDAO = userDAO;
        MockitoAnnotations.openMocks(this);
//        when(conn.prepareStatement(anyString())).thenReturn(stmt);
//        when(stmt.executeQuery()).thenReturn(rs);

//        userDAO = new UserDAOImpl() {
//            {
//                this.conn = conn;
//            }
//        };
    }

    @Test
    void testLoginUserSuccess() {
        String username = "tm_raj";
        String password = "tm_pass";
        User expectedUser = new User(1, username, password, "Teammember", "Active", "Teammember");

        when(userDAO.loginUser(username, password)).thenReturn(expectedUser);

        User actualUser = userService.loginUser(username, password);

        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);

        verify(userDAO).loginUser(username, password);
    }

    @Test
    void testLoginUserNotFound() {
        String username = "testUser1";
        String password = "testPass1";

        when(userDAO.loginUser(username, password)).thenReturn(null);

        User actualUser = userService.loginUser(username, password);

        assertNull(actualUser);

        verify(userDAO).loginUser(username, password);
    }

    @Test
    void testRegisterUserSuccess() {
        String username = "mocktestUser";
        String password = "mocktestPass";
        String role = "Teammember";
        String status = "Active";
        String accessLevel = "Teammember";

        User user = new User(1, username, password, role, status, accessLevel);

        when(userDAO.registerUser(user)).thenReturn(true);

        boolean result = userService.registerUser(username, password, role, status, accessLevel);

        assertTrue(result);
        verify(userDAO).registerUser(user);
    }

    @Test
    void testRegisterUserFailure() {
        String username = "testUser";
        String password = "testPass";
        String role = "teamember";
        String status = "Active";
        String accessLevel = "teamember";

        User user = new User(1, username, password, role, status, accessLevel);

        when(userDAO.registerUser(user)).thenReturn(false);

        boolean result = userService.registerUser(username, password, role, status, accessLevel);

        assertFalse(result);
        verify(userDAO).registerUser(user);
    }

    @Test
    void testGetprintUserNamesByRole() {
        userService.getprintUserNamesByRole();
        verify(userDAO).printUserNamesByRole("TeamMember");
    }

//    @Test
//    void testPrintProjectManagerNamesByRole() throws SQLException {
//        // Set up the mock result set
//        when(rs.next()).thenReturn(true).thenReturn(false);
//        when(rs.getString("Username")).thenReturn("testProjectManager");
//
//        // Call the method
//        userDAO.printProjectManagerNamesByRole("ProjectManager");
//
//        // Verify that the prepared statement was set with the correct role
//        verify(stmt).setString(1, "ProjectManager");
//
//        // Verify that the query was executed
//        verify(stmt).executeQuery();
//
//        // Verify that the result set was processed
//        verify(rs).next();
//        verify(rs).getString("Username");
//    }


    @Test
    void testLoginUser_Success() {
        String username = "tm_raj";
        String password = "tm_pass";

        User result = userDAO.loginUser(username, password);

        assertNotNull(result);
        assertEquals(username, result.getUser_name());
    }

    @Test
    void testLoginUser_Failure() {
        String username = "nonExistingUser";
        String password = "wrongPassword";

        User result = userDAO.loginUser(username, password);

        assertNull(result);
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User(0, "TestUser", "test_password", "TeamMember", "Active", "TeamMember");

        boolean result = userDAO.registerUser(user);

        assertTrue(result, "User should be registered successfully");
    }



    @Test
    void testPrintUserNamesByRole() {
        UserDAOImpl userDAO = new UserDAOImpl();
        String role = "admin";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        userDAO.printUserNamesByRole(role);

        System.setOut(originalOut);

        String output = outContent.toString().trim();
        assertFalse(output.isEmpty(), "Output should not be empty if there are users with the specified role.");

    }

    @Test
    void testResetPassword_Success() {
        UserDAOImpl userDAO = new UserDAOImpl();
        String username = "pm_abc";
        String newPassword = "pm_pass";

        boolean result = userDAO.resetPassword(username, newPassword);

        assertTrue(result, "Password should be reset successfully.");
    }


    @Test
    void testGetClients() {
        UserDAOImpl userDAO = new UserDAOImpl();

        List<Client> clients = userDAO.getClients();

        assertNotNull(clients, "List of clients should not be null");
        assertFalse(clients.isEmpty(), "List of clients should not be empty");

        boolean foundClient = false;
        for (Client client : clients) {
            if (client.getClient_name().equals("Client B")) {
                assertEquals("clientb@example.com", client.getContact_information(), "Incorrect contact information for ClientName1");
                foundClient = true;
                break;
            }
        }

        assertTrue(foundClient, "Client with name 'Client B' should be found");
    }

//    @Test
//    void testGetClientIDByName_ClientExists() {
//        UserDAOImpl userDAO = new UserDAOImpl();
//        String clientName = "Client B";
//
//        int clientID = userDAO.getClientIDByName(clientName);
//
//        assertNotEquals(-1, clientID, "Client ID should not be -1 when client exists");
//        assertTrue(clientID > 0, "Client ID should be greater than 0");
//    }

    @Test
    void testGetClientIDByName_ClientNotFound() {
        UserDAOImpl userDAO = new UserDAOImpl();
        String nonExistentClientName = "Client D";

        int clientID = userDAO.getClientIDByName(nonExistentClientName);

        assertEquals(-1, clientID, "Client ID should be -1 when client is not found");
    }

    @Test
    void testCreateMilestone_Successful() {
        UserDAOImpl userDAO = new UserDAOImpl();
        String milestoneName = "Milestone Test";
        String milestoneDescription = "Milestone Test Description";

        boolean created = userDAO.createMilestone(milestoneName, milestoneDescription);

        assertTrue(created, "Milestone should be created successfully");
    }

   @Test
   void testGetProjectIDByName_ProjectExists() {
      UserDAOImpl userDAO = new UserDAOImpl();
      String projectName = "Project Beta";

      int projectID = userDAO.getProjectIDByName(projectName);

      assertNotEquals(-1, projectID, "Project ID should not be -1 when project exists");
      assertTrue(projectID > 0, "Project ID should be greater than 0");
   }

   @Test
   void testGetProjectIDByName_ProjectNotFound() {
      UserDAOImpl userDAO = new UserDAOImpl();
      String nonExistentProjectName = "NoProjectName";

      int projectID = userDAO.getProjectIDByName(nonExistentProjectName);

      assertEquals(-1, projectID, "Project ID should be -1 when project is not found");
   }

   @Test
   void testGetUserIDByUsername_UserExists() {
      UserDAOImpl userDAO = new UserDAOImpl();
      String username = "tm_raj";

      int userID = userDAO.getUserIDByUsername(username);

      assertNotEquals(-1, userID, "User ID should not be -1 when user exists");
      assertTrue(userID > 0, "User ID should be greater than 0");
   }

    @Test
    void testGetUserIDByUsername_UserNotFound() {
        UserDAOImpl userDAO = new UserDAOImpl();
        String nonExistentUsername = "nouser";

        int userID = userDAO.getUserIDByUsername(nonExistentUsername);

        assertEquals(-1, userID, "User ID should be -1 when user is not found");
    }

//    @Test
//    void testGetUsernamesByProjectAndPM_UsernamesFound() {
//        UserDAOImpl userDAO = new UserDAOImpl();
//        int projectId = 3;
//        int pmId = 13;
//
//        List<String> usernames = userDAO.getUsernamesByProjectAndPM(projectId, pmId);
//
//        assertFalse(usernames.isEmpty(), "Usernames list should not be empty");
//        assertTrue(usernames.contains("tm_raj"), "Usernames list should contain username1");
//    }

    @Test
    void testGetAllMilestones_MilestonesFound() {
        UserDAOImpl userDAO = new UserDAOImpl();

        List<MileStone> milestones = userDAO.getAllMilestones();

        assertFalse(milestones.isEmpty(), "Milestones list should not be empty");
        assertEquals(8, milestones.size(), "Expected number of milestones should match");
    }

    @Test
    void testGetMilestoneIDByName_MilestoneFound() {
        UserDAOImpl userDAO = new UserDAOImpl();
        String milestoneName = "Initial Planning";

        int milestoneID = userDAO.getMilestoneIDByName(milestoneName);

        assertNotEquals(-1, milestoneID, "Milestone ID should not be -1 when milestone is found");
        assertEquals(1, milestoneID, "Expected milestone ID should match");
    }

    @Test
    void testGetMilestoneIDByName_MilestoneNotFound() {
        UserDAOImpl userDAO = new UserDAOImpl();
        String milestoneName = "No Milestone";

        int milestoneID = userDAO.getMilestoneIDByName(milestoneName);

        assertEquals(-1, milestoneID, "Milestone ID should be -1 when milestone is not found");
    }


    @Test
    void testInsertUserActivity_Success() {
        UserDAOImpl userDAO = new UserDAOImpl();
        int userID = 3;
        int taskID = 4;
        int milestoneID = 3;

        assertDoesNotThrow(() -> userDAO.insertUserActivity(userID, taskID, milestoneID), "Inserting user activity should not throw an exception");

    }


 }
