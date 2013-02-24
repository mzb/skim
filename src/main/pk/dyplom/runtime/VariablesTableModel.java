package pk.dyplom.runtime;

import pk.dyplom.I18n;
import pk.dyplom.eval.Environment;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VariablesTableModel extends DefaultTableModel {
    
    public static final String[] COLUMNS = {
            I18n.t("variablesTable.context"),
            I18n.t("variablesTable.name"),
            I18n.t("variablesTable.value")
    };

    public VariablesTableModel() {
        super(new Object[][]{}, COLUMNS);
    }

    // TODO: Wykonac update zaraz po kompilacji
    public void update(Environment env) {
        setRowCount(0);
        for (String name : env.keySet()) {
            Entry entry = new Entry(name, env.get(name));
            if (!entry.isPrimitive())
                continue;
            addRow(new Object[]{ entry.context, entry.name, entry.toString() });
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    
    private static class Entry {

        public final String context;
        public final String name;
        public final Object value;
        
        public Entry(String key, Object value) {
            this.value = value;
            this.name = extractNameFromKey(key);
            this.context = extractContextFromKey(key);
        }
        
        private String extractContextFromKey(String key) {
            String[] keyParts = key.split("\\.");
            if (keyParts.length != 3) {
                return "";
            }
            return keyParts[1];
        }
        
        private String extractNameFromKey(String key) {
            String[] keyParts = key.split("\\.");
            if (keyParts.length != 3) {
                return keyParts[0];
            }
            return keyParts[2];
        }

        public boolean isPrimitive() {
            return  !name.startsWith("$") && (
                    value instanceof Integer ||
                    value instanceof Double ||
                    value instanceof Float ||
                    value instanceof String ||
                    value instanceof Boolean ||
                    value instanceof Character ||
                    value instanceof List);
        }
        
        @Override
        public String toString() {
            return value.toString();
        }
    }
}
