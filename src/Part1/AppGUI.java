package Part1;


import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class AppGUI {
    private static JFrame frame;
    private static JPanel panel;
    private static JList<String> itemList;
    private static DefaultListModel<String> listModel;
    private static JTextArea textArea;
    private static JButton addButton;
    private static JButton clearButton;

    public static void main(String[] args) {
        frame = new JFrame("Multiple Selection App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        itemList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(itemList);
        panel.add(listScrollPane, BorderLayout.WEST);

        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        panel.add(textArea, BorderLayout.CENTER);

        addButton = new JButton("Add");
        clearButton = new JButton("Clear");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        panel.add(buttonPanel, BorderLayout.EAST);

        frame.add(panel);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(frame, "Are you sure?", "Closing app", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> selectedItems = itemList.getSelectedValuesList();
                StringBuilder selectedText = new StringBuilder();
                for (String item : selectedItems) {
                    selectedText.append(item).append(" ");
                }
                String currentText = textArea.getText();
                if (currentText.length() + selectedText.length() > 100) {
                    JOptionPane.showMessageDialog(frame, "Maximum number of characters (100) exceeded!");
                } else {
                    textArea.append(selectedText.toString());
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });

        listModel.addElement("Java");
        listModel.addElement("Python");
        listModel.addElement("Pascal");
    }
}
