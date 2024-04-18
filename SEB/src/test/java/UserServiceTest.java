
import com.seb.dao.UserDao;
import com.seb.model.User;
import com.seb.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private UserDao userDaoMock;

    @BeforeEach
    void setUp() {
        userDaoMock = Mockito.mock(UserDao.class);
        userService = new UserService(userDaoMock);
    }

    @Test
    void getUserByUsername_found() {
        User expectedUser = new User(1L, "john_doe", "securePassword", 1000);
        Mockito.when(userDaoMock.findUserByUsername("john_doe")).thenReturn(expectedUser);

        User result = userService.getUserByUsername("john_doe");

        assertNotNull(result);
        assertEquals(expectedUser.getUsername(), result.getUsername());
    }

    @Test
    void getUserByUsername_notFound() {
        Mockito.when(userDaoMock.findUserByUsername("unknown_user")).thenReturn(null);

        User result = userService.getUserByUsername("unknown_user");

        assertNull(result);
    }

    @Test
    void registerUser_success() {
        User newUser = new User(null, "new_user", "password123", 1000);
        Mockito.when(userDaoMock.createUser(Mockito.any(User.class))).thenReturn(true);

        boolean result = userService.registerUser(newUser.getUsername(), newUser.getPassword());

        assertTrue(result);
    }

    @Test
    void registerUser_duplicateUsername() {
        // Assume the database already contains a user with the same username
        User newUser = new User(null, "john_doe", "securePassword", 1000);
        Mockito.when(userDaoMock.findUserByUsername("john_doe")).thenReturn(newUser);

        // Try to register the same username
        boolean result = userService.registerUser("john_doe", "securePassword");

        assertFalse(result);
    }

    @Test
    void updateUser_success() {
        User existingUser = new User(1L, "existing_user", "oldPassword", 1000);
        Mockito.when(userDaoMock.updateUserDetails(Mockito.any(User.class))).thenReturn(true);

        boolean result = userService.updateUser(existingUser);

        assertTrue(result);
    }

    @Test
    void loginUser_correctCredentials() {
        User user = new User(1L, "john_doe", "password123", 1000);
        Mockito.when(userDaoMock.findUserByUsername("john_doe")).thenReturn(user);

        User result = userService.loginUser("john_doe", "password123");

        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
    }

    @Test
    void loginUser_incorrectCredentials() {
        User user = new User(1L, "john_doe", "password123", 1000);
        Mockito.when(userDaoMock.findUserByUsername("john_doe")).thenReturn(user);

        User result = userService.loginUser("john_doe", "wrongPassword");

        assertNull(result);
    }

    @Test
    void isValidUser_validToken() {
        String validToken = "Basic john_doe-sebToken";
        assertTrue(userService.isValidUser(validToken, "john_doe"));
    }

    @Test
    void isValidUser_invalidToken() {
        String invalidToken = "Basic jane_doe-sebToken";
        assertFalse(userService.isValidUser(invalidToken, "john_doe"));
    }
}
