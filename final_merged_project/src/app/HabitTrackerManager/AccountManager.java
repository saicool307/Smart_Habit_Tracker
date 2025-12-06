package app.HabitTrackerManager;

import app.user.UserProfile;
import app.exception.DataException;
import app.exception.AccountException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private Map<String, UserProfile> users = new HashMap<>();
    private final File storageFile;

    public AccountManager() {
        storageFile = new File("habit_data.ser");
        try { load(); } catch (DataException e) { /* ignore on first run */ }
    }

    public UserProfile signUp(String username) throws AccountException {
        String key = username.toLowerCase();
        if (users.containsKey(key)) throw new AccountException("Username already exists.");
        UserProfile profile = new UserProfile(username);
        users.put(key, profile);
        return profile;
    }

    public UserProfile login(String username) throws AccountException {
        UserProfile p = users.get(username.toLowerCase());
        if (p == null) throw new AccountException("User not found.");
        return p;
    }

    public void save() throws DataException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            out.writeObject(users);
        } catch (IOException e) {
            throw new DataException("Failed to save data", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void load() throws DataException {
        if (!storageFile.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(storageFile))) {
            Object o = in.readObject();
            users = (Map<String, UserProfile>) o;
        } catch (IOException | ClassNotFoundException e) {
            throw new DataException("Failed to load data", e);
        }
    }

    public Map<String, UserProfile> getAllUsers() { return users; }
}
