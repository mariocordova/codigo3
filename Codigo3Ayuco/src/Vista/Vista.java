/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import codigo3ayuco.Codigo3;
import codigo3ayuco.Cuadrupla;
import codigo3ayuco.Interprete;
import codigo3ayuco.TSS;
import codigo3ayuco.TSVAR;
import java.awt.Color;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mariocordova
 */
public class Vista extends javax.swing.JFrame
{
    int cont;
    int ultimoIp;
    Interprete VM;
    TMCuadruplas modelCuadruplas;

    /**
     * Creates new form Vista
     */
    public Vista()
    {
        initComponents();
        
        tablaCuadruplas.setSelectionBackground(Color.CYAN);
        programar(new Codigo3(new TSVAR(),new TSS()));
        VM = new Interprete();
        VM.open("triangulo.c3");
        
        Mostrar(VM.getC3());
        VM.run();
        cont =1;
        ultimoIp= VM.ultimoIP();
        modelCuadruplas = new TMCuadruplas(VM.getC3());
        tablaCuadruplas.setModel(modelCuadruplas);
        
        tsvar.setText(VM.getC3().getTSVar().toString()+"\n"+"\n"+"\n"+"\n"+VM.getC3().getTSS().imprimirTss());
        //tss.setText(VM.getC3().getTSS().toString());
        actualizarVista();
        
        ip.setText("IP = "+ VM.getIP());
         
        try
        {
            tablaCuadruplas.setRowSelectionInterval(VM.getIP(), VM.getIP());
        } catch (Exception e)
        {
        }
    }
    private void actualizarVista() {
        actualizarVistaPilas();
                tsvar.setText(VM.getC3().getTSVar().toString()+"\n"+"\n"+"\n"+"\n"+VM.getC3().getTSS().imprimirTss());
    }
    
    private void actualizarVistaPilas() {
        //int pilaT[1] = new int[Interprete.pilaTemp.size()];
        //int pilaAux[1] = new int[Interprete.pilaEBP.size()];
        
        String Titulo[]={"PilaTemp"};
        DefaultTableModel tmpt = new DefaultTableModel(null,Titulo);
        
        String Titulo2[]={"PilaEBP"};
        DefaultTableModel tmpe = new DefaultTableModel(null,Titulo2);
        
        for (int i = 0; i < Interprete.pilaTemp.size(); i++)
        {
            Integer f[]={Interprete.pilaTemp.get(i)};
            //pilaT[i] = Interprete.pilaTemp.get(i);
            tmpt.addRow(f);
        }
        pilaTemp.setModel(tmpt);
        for (int i = 0; i < Interprete.pilaEBP.size(); i++)
        {
            Integer f[]={Interprete.pilaEBP.get(i)};
            //pilaT[i] = Interprete.pilaTemp.get(i);
            tmpe.addRow(f);
            
        }
        pilaEbp.setModel(tmpe);
    }
    public static void Mostrar(Codigo3 c3){ //Para mostrar las tablas y el C3.
        System.out.println(c3.getTSVar());
        System.out.println(c3.getTSS());
        System.out.println(c3);
    }
    private static void programar(Codigo3 c3)
    {
        TSVAR tsvar = c3.getTSVar();
        TSS tss = c3.getTSS();
        //llenar tss
        tss.add("Introduzca N: ");
        tss.add("*");

        //llenar tvar
        tsvar.addProc("$MAIN");
        tsvar.addProc("Lectura");
        tsvar.addProc("Linea");
        tsvar.addVar("I", TSVAR.TIPOINT);
        tsvar.addVar("N", TSVAR.TIPOINT);
        tsvar.addVar("J", TSVAR.TIPOINT);
        tsvar.addVar("K", TSVAR.TIPOINT);  
        
        //Lectura
        c3.add(Cuadrupla.OPASIGNNUM, 1,0);
        c3.add(Cuadrupla.ETIQUETA, 1);
        c3.add(Cuadrupla.OPWRITES,-0);
        c3.add(Cuadrupla.OPNL);
        c3.add(Cuadrupla.OPREAD,-4);
        c3.add(Cuadrupla.OPMEI,2, -4, 1);
        c3.add(Cuadrupla.OPIF1, 2, 1);
        c3.add(Cuadrupla.OPRET);
        
        //linea
        c3.add(Cuadrupla.OPASIGNNUM, -5,1);
        c3.add(Cuadrupla.ETIQUETA, 2);
        c3.add(Cuadrupla.OPMEI,1, -5, -6);
        c3.add(Cuadrupla.OPIF0,1,3);
        c3.add(Cuadrupla.OPWRITES,-1);
        c3.add(Cuadrupla.OPINC,-5);
        c3.add(Cuadrupla.OPGOTO,2);
        c3.add(Cuadrupla.ETIQUETA,3);
        c3.add(Cuadrupla.OPNL);
        c3.add(Cuadrupla.OPRET);
        
        //$MAIN
        c3.add(Cuadrupla.OPCALL,-1);
        c3.add(Cuadrupla.OPASIGNNUM,-3,1);
        c3.add(Cuadrupla.ETIQUETA,4);
        c3.add(Cuadrupla.OPMEI,1,-3,-4);
        c3.add(Cuadrupla.OPIF0,1,5);
        c3.add(Cuadrupla.OPASIGNID,-6,-3);
        c3.add(Cuadrupla.OPCALL,-2);
        c3.add(Cuadrupla.OPINC,-3);
        c3.add(Cuadrupla.OPGOTO,4);
        c3.add(Cuadrupla.ETIQUETA,5);
        c3.add(Cuadrupla.OPRET);

        /*
        poner en la tsvar las posiciones del c3 de cada procedimiento
        en las cuadruplas se hace referencia en negativa
        en la tsvar se lo hace positivo
         */
        // MAIN
        tsvar.setValorI(0, 18);
        tsvar.setValorF(0, 28);
        tsvar.setCantTmp(0, 1);
        // Lectura
        tsvar.setValorI(1,  0);
        tsvar.setValorF(1, 7);
        tsvar.setCantTmp(1, 2);
        // Linea
        tsvar.setValorI(2, 8);
        tsvar.setValorF(2, 17);
        tsvar.setCantTmp(2, 1);
         
        c3.Save("triangulo.c3");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCuadruplas = new javax.swing.JTable();
        jCheckBox1 = new javax.swing.JCheckBox();
        tsvarasddf = new javax.swing.JLabel();
        ip = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tsvar = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        tss = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        pilaTemp = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        pilaEbp = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        salida = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        AbrirFile = new javax.swing.JMenuItem();
        Salir = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tablaCuadruplas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String []
            {
                "Nro", "c3", "OPCODE", "Dir1", "Dir2", "Dir3"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean []
            {
                true, true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaCuadruplas);

        jCheckBox1.setText("modo DEBUG");

        tsvarasddf.setText("TSVAR");

        ip.setText("jLabel6");

        tsvar.setColumns(20);
        tsvar.setRows(5);
        jScrollPane5.setViewportView(tsvar);

        jButton1.setText("Ejecutar");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        tss.setText("TSS");

        jLabel7.setText("jLabel7");

        pilaTemp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null},
                {null},
                {null},
                {null}
            },
            new String []
            {
                "PILA DE TEMPORALES"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class
            };
            boolean[] canEdit = new boolean []
            {
                false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(pilaTemp);

        pilaEbp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null},
                {null},
                {null},
                {null}
            },
            new String []
            {
                "PILA DE EBPs"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class
            };
            boolean[] canEdit = new boolean []
            {
                false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(pilaEbp);

        salida.setColumns(20);
        salida.setRows(5);
        jScrollPane4.setViewportView(salida);

        jLabel1.setText("SALIDAS:");

        jMenu1.setText("File");

        AbrirFile.setText("Abrir");
        AbrirFile.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AbrirFileActionPerformed(evt);
            }
        });
        jMenu1.add(AbrirFile);

        Salir.setText("Salir");
        Salir.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                SalirActionPerformed(evt);
            }
        });
        jMenu1.add(Salir);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tsvarasddf)
                                    .addComponent(ip))
                                .addGap(192, 192, 192))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addGap(36, 36, 36)
                                .addComponent(jButton1)))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7)
                            .addComponent(tss)))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(tss)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1)
                                    .addComponent(jCheckBox1))
                                .addGap(16, 16, 16)
                                .addComponent(tsvarasddf)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ip)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGap(25, 25, 25))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AbrirFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AbrirFileActionPerformed
    {//GEN-HEADEREND:event_AbrirFileActionPerformed
        // TODO add your handling code here:
        /**llamamos el metodo que permite cargar la ventana*/
        JFileChooser file=new JFileChooser();
        file.showOpenDialog(this);
        /**abrimos el archivo seleccionado*/
        File abre=file.getSelectedFile();
        String nombreArchivo = abre.getName();

        VM = new Interprete();
        VM.open(nombreArchivo);

        Mostrar(VM.getC3());
        VM.run();
        cont =1;
        ultimoIp= VM.ultimoIP();
    }//GEN-LAST:event_AbrirFileActionPerformed

    private void SalirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_SalirActionPerformed
    {//GEN-HEADEREND:event_SalirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SalirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        // TODO add your handling code here:

        ultimoIp= VM.ultimoIP();
        if(VM.getIP() != ultimoIp || VM.pilasVacias())
        {
            VM.interpretarCuadrupla(VM.getCuadrupla(VM.getIP()));
            actualizarVista();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "el programa finalizó", "Mensaje", JOptionPane.ERROR_MESSAGE);
        }

        ip.setText("IP = "+ VM.getIP());
        
        try
        {
            tablaCuadruplas.setRowSelectionInterval(VM.getIP(), VM.getIP());
        } catch (Exception e)
        {
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new Vista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AbrirFile;
    private javax.swing.JMenuItem Salir;
    private javax.swing.JLabel ip;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable pilaEbp;
    private javax.swing.JTable pilaTemp;
    public static javax.swing.JTextArea salida;
    private javax.swing.JTable tablaCuadruplas;
    private javax.swing.JLabel tss;
    private javax.swing.JTextArea tsvar;
    private javax.swing.JLabel tsvarasddf;
    // End of variables declaration//GEN-END:variables
}
