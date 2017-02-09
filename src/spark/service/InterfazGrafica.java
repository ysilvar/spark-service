/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.service;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eduardo
 */
public class InterfazGrafica extends JDialog {

    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;

    public InterfazGrafica() {

        initComponents();
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

    }

    public void addData(Object[] data) {
        model.addRow(data);
    }

    public void addData(Object[][] data) {
        for (Object[] data1 : data) {

            model.addRow(data1);
        }
    }

    public void initComponents() {
        setTitle("Configure Slave");
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

    }

    public static void main(String[] args) {
        new InterfazGrafica().setVisible(true);
    }
}
