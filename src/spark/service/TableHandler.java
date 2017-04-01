/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eduardo
 */
public final class TableHandler extends JDialog {

    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;
    private final String[] readConfig;
    private ArrayList<TableConten> data;
    private Constants constants;

    public TableHandler(ReadConfig readConfig) {
        constants = Constants.getInstance();
        this.readConfig = readConfig.getConfig();
        initComponents();
    }

    public void addData(Object[] data) {
        model.addRow(data);

    }

    public void addData(ArrayList<TableConten> data) {
        data.stream().forEach((datatable) -> {
            model.addRow(new Object[]{datatable.isStatus(),
                datatable.getNameVariable(),
                datatable.getValueVariable()
            });
        });
    }

    public void initComponents() {
        setTitle("Configure Slave");
        JMenuBar menuBar = new javax.swing.JMenuBar();
        JMenu file = new javax.swing.JMenu();
        JMenuItem save = new javax.swing.JMenuItem("Save");
        JMenuItem saveAs = new javax.swing.JMenuItem("Save As");

        file.setText("File");
        file.add(save);
        file.add(saveAs);
        menuBar.add(file);

        save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                java.awt.event.InputEvent.CTRL_MASK));
        saveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));

        save.addActionListener((ActionEvent e) -> {
            try {
                save();
            } catch (IOException ex) {
                Logger.getLogger(TableHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        setJMenuBar(menuBar);
        scroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(560, 192));

        scroll.setPreferredSize(new java.awt.Dimension(492, 602));

        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{}
        ));
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        scroll.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
        );

        setLocationRelativeTo(null);

        model = new DefaultTableModel(new Object[][]{},
                new String[]{
                    "Status", "Variable Name", "Variable Value"}) {
                    Class[] types = new Class[]{
                        java.lang.Boolean.class, java.lang.String.class,
                        java.lang.String.class,};
                    boolean[] canEdit = new boolean[]{
                        true, true, true
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                };
        table.setVisible(true);
        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(10);

    }

    private boolean isConteins(String readConfig) {
        if (data.stream().anyMatch((data1) -> (readConfig.contains(data1.getNameVariable())))) {
            return true;
        }
        return false;
    }
    /**
     * Update the "ArrayList data" with the values shown in the table
     */
    private void upDateConfig() {
        data = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            boolean status = table.getValueAt(i, 0).toString().equals("true");

            data.add(new TableConten(status, table.getValueAt(i, 1).toString(),
                    table.getValueAt(i, 2).toString(), null));
        }

    }
    /**
     * Update configuration file
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void save() throws FileNotFoundException, IOException {
        
        File fileConf = new File(constants.getSPARK_CONF_DIR());
        upDateConfig();
        String result = "";
        for (String readConfig1 : readConfig) {
            boolean flag = false;

            if (!isConteins(readConfig1)) {
                result = result.concat(readConfig1).trim().concat("\n");
            }
        }
        for (TableConten data1 : data) {
            String temp = data1.isStatus()
                    ? Constants.getInstance().getMOD()
                    : Constants.getInstance().getCOMMENTARY();
            result = result.concat(temp).concat(data1.getNameVariable().concat("=").
                    concat(data1.getValueVariable())).trim().concat("\n");

        }

        BufferedWriter writeFile = new BufferedWriter(new FileWriter(fileConf));

        writeFile.write(result);

        writeFile.close();

    }

}
