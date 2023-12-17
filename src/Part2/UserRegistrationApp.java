package Part2;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

public class UserRegistrationApp {
    private JFrame frame;
    private JPanel panel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> genderComboBox;
    private JCheckBox agreementCheckBox;
    private JTextArea infoTextArea;
    private JButton registerButton;
    private JButton loadButton;
    private JButton saveButton;

    private Set<String> registeredUsers = new HashSet<>();
    private Preferences preferences;

    public UserRegistrationApp() {
        frame = new JFrame("User Registration App");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(600, 450);

        preferences = Preferences.userNodeForPackage(UserRegistrationApp.class);

        panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Gender:"));
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        panel.add(genderComboBox);

        agreementCheckBox = new JCheckBox("Accept the rules");
        panel.add(agreementCheckBox);

        panel.add(new JLabel("Additional info:"));
        infoTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        panel.add(scrollPane);

        registerButton = new JButton("Sign in");
        panel.add(registerButton);

        saveButton = new JButton("Save to file");
        panel.add(saveButton);

        loadButton = new JButton("Load from file");
        panel.add(loadButton);

        frame.add(panel);
        frame.setVisible(true);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUsersToFile();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUsersFromFile();
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(frame, "Are you sure?", "Closing app", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            }
        });
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        String gender = genderComboBox.getSelectedItem().toString();
        boolean agreement = agreementCheckBox.isSelected();
        String info = infoTextArea.getText();

        if (agreement) {
            registeredUsers.add("Username: " + username + "\nPassword: " + password + "\nGender: " + gender + "\nAdditional info: " + info + "\n");
            JOptionPane.showMessageDialog(frame, "User registered!");
        } else {
            JOptionPane.showMessageDialog(frame, "For registration you should accept the rules.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveUsersToFile() {
        JFileChooser fileChooser = new JFileChooser();
        String lastDirectory = preferences.get("lastDirectory", "");
        if (!lastDirectory.isEmpty()) {
            fileChooser.setCurrentDirectory(new File(lastDirectory));
        }
        int result = fileChooser.showSaveDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            preferences.put("lastDirectory", file.getParent());

            try (FileWriter fileWriter = new FileWriter(file, true);
                 BufferedWriter writer = new BufferedWriter(fileWriter)) {
                for (String user : registeredUsers) {
                    writer.write(user);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(frame, "Data saved to file " + file.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error saving to file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadUsersFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        String lastDirectory = preferences.get("lastDirectory", "");
        if (!lastDirectory.isEmpty()) {
            fileChooser.setCurrentDirectory(new File(lastDirectory));
        }
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            preferences.put("lastDirectory", file.getParent());
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                StringBuilder usersData = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    usersData.append(line).append("\n");
                }
                infoTextArea.setText(usersData.toString());
                JOptionPane.showMessageDialog(frame, "Data loaded from file " + file.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error loading data from file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserRegistrationApp();
            }
        });
    }
}