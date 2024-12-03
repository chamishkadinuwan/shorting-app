import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.IntStream;

public class AppGUI {
    private FileUpload fileUpload = new FileUpload();
    private PerformanceEvaluator performanceEvaluator = new PerformanceEvaluator();

    private JFrame frame;
    private JComboBox<String> columnSelector;
    private JComboBox<String> sortingMethodSelector;
    private JTextArea resultArea;

    public void createAndShowGUI() {
        frame = new JFrame("Sorting Performance App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 200);

        // Upload Btn
        JButton uploadButton = new JButton("Upload CSV");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileUpload.uploadFile();
                updateColumnSelector();
            }
        });

        // Column Selector Dropdown
        columnSelector = new JComboBox<>();
        columnSelector.setPreferredSize(new Dimension(150, 25));

        // Sorting Method Selector Dropdown
        String[] sortingMethods = {"Insertion Sort", "Shell Sort", "Merge Sort", "Quick Sort", "Heap Sort"};
        sortingMethodSelector = new JComboBox<>(sortingMethods);
        sortingMethodSelector.setPreferredSize(new Dimension(150, 25));

        // Sort Button
        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSorting();
            }
        });

        // Result Display Area
        resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Layout
        JPanel panel = new JPanel();
        panel.add(uploadButton);
        panel.add(new JLabel("Select Column:"));
        panel.add(columnSelector);
        panel.add(new JLabel("Select Sorting Method:"));
        panel.add(sortingMethodSelector);
        panel.add(sortButton);

        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void performSorting() {
        String selectedColumn = (String) columnSelector.getSelectedItem();
        String selectedMethod = (String) sortingMethodSelector.getSelectedItem();

        if (selectedColumn == null || selectedMethod == null) {
            JOptionPane.showMessageDialog(frame, "Please upload a CSV and select both column and sorting method.");
            return;
        }

        // Get the column data as doubles for sorting
        List<String[]> data = fileUpload.getData();
        int columnIndex;
        String[] columns = fileUpload.getColumns();

        // Find the correct column index
        columnIndex = IntStream.range(0, columns.length).filter(i -> columns[i].equals(selectedColumn)).findFirst().orElse(-1);

        if (columnIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Column not found.");
            return;
        }

        double[] columnData = data.stream()
                .mapToDouble(row -> Double.parseDouble(row[columnIndex]))
                .toArray();

        // Perform Sorting and Measure Performance
        long executionTimeNano = performanceEvaluator.evaluateSorting(columnData.clone(), selectedMethod);
        double executionTimeMillis = executionTimeNano / 1_000_000.0; // Convert nanoseconds to milliseconds

        // Display Result
        resultArea.append("Execution Time for " + selectedMethod + ": " + executionTimeMillis + " ms\n");
    }

    private void updateColumnSelector() {
        String[] columns = fileUpload.getColumns();
        if (columns != null) {
            List<String> numericColumns = fileUpload.getNumericColumns();
            columnSelector.setModel(new DefaultComboBoxModel<>(numericColumns.toArray(new String[0])));
            JOptionPane.showMessageDialog(frame, "Numeric Columns Loaded: " + String.join(", ", numericColumns));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppGUI().createAndShowGUI());
    }
}
