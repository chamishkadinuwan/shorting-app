import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileUpload {
    private List<String[]> data;
    private String[] columns;
    private List<String> numericColumns;

    public FileUpload() {
        this.data = new ArrayList<>();
        this.numericColumns = new ArrayList<>();
    }

    public void uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                // Read headers
                String headerLine = br.readLine();
                if (headerLine != null) {
                    columns = headerLine.split(",");
                    numericColumns.clear();
                }

                // Read data and determine numeric columns
                data.clear();
                String line;
                boolean firstRow = true;
                boolean[] isNumeric = new boolean[columns.length];

                // Initialize isNumeric array to true
                for (int i = 0; i < isNumeric.length; i++) {
                    isNumeric[i] = true;
                }

                while ((line = br.readLine()) != null) {
                    String[] row = line.split(",");
                    data.add(row);

                    // Check each column if it contains numeric values
                    for (int i = 0; i < row.length && i < columns.length; i++) {
                        if (isNumeric[i]) {  // Only check if we still think it's numeric
                            try {
                                Double.parseDouble(row[i].trim());
                            } catch (NumberFormatException e) {
                                isNumeric[i] = false;
                            }
                        }
                    }
                }

                // Create list of numeric column names
                for (int i = 0; i < columns.length; i++) {
                    if (isNumeric[i]) {
                        numericColumns.add(columns[i]);
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
            }
        }
    }

    public String[] getColumns() {
        return columns;
    }

    public List<String[]> getData() {
        return data;
    }

    public List<String> getNumericColumns() {
        return numericColumns;
    }
}