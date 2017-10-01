/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

/**
 *
 * @author mariocordova
 */


import codigo3ayuco.Codigo3;
import codigo3ayuco.Cuadrupla;
import java.util.ArrayList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author mariocordova
 */
public class TMCuadruplas implements TableModel
{
    public ArrayList<TablaCuadrupla> lc;

    public ArrayList<TablaCuadrupla> getLc() {
        return lc;
    }

    public TMCuadruplas(Codigo3 c3) {
        lc = new ArrayList<TablaCuadrupla>();
        for (int i = 0; i < c3.lenght(); i++) {
            lc.add(new TablaCuadrupla(i, c3.getCuadrupla(i).toString(c3), c3.getCuadrupla(i)));
        }

    }
    
    @Override
    public int getRowCount() {
        return lc.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex)
        {
            case 0:
                return "Nro";
            case 1:
                return "c3";
            case 2:
                return "OPCODE";
            case 3:
                return "Dir1";
            case 4:
                return "Dir2";
            case 5:
                return "Dir3";
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        if(columnIndex == 1 || columnIndex==2)
            return String.class;
        else
            return Integer.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex)
        {
            case 0:
                return lc.get(rowIndex).nro;
            case 1:
                return lc.get(rowIndex).c3;
            case 2:
                return getNombreOpCode(lc.get(rowIndex).c.getOpCode());
            case 3:
                return lc.get(rowIndex).c.getDir(1);
            case 4:
                return lc.get(rowIndex).c.getDir(2);
            case 5:
                return lc.get(rowIndex).c.getDir(3);
        }
        return "";        
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        /*
        switch(columnIndex)
        {
            case 0:
                lc.get(rowIndex).nro = Integer.parseInt(aValue.toString());
            case 1:
            case 2:
                return getNombreOpCode(lc.get(rowIndex).c.getOpCode());
            case 3:
                return lc.get(rowIndex).c.getDir(1);
            case 4:
                return lc.get(rowIndex).c.getDir(2);
            case 5:
                return lc.get(rowIndex).c.getDir(3);
        }
        */
    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static String getNombreOpCode(int opcode)
    {
        switch(opcode)
        {
            case Cuadrupla.NOP:
                return "NOP";
            case Cuadrupla.OPNL:
                return "OPNL";
            case Cuadrupla.OPRET:
                return "OPRET";
            case Cuadrupla.OPCALL:
                return "OPCALL";   
            case Cuadrupla.OPGOTO:
                return "OPGOTO";

            case Cuadrupla.OPINC:
                return "OPINC";    
            case Cuadrupla.OPDEC:
                return "OPDEC";
            case Cuadrupla.OPWRITE:
                return "OPWRITE";
            case Cuadrupla.OPREAD:
                return "OPREAD";
            case Cuadrupla.OPWRITES:
                return "OPWRITES";

            case Cuadrupla.ETIQUETA:
                return "ETIQUETA";
            case Cuadrupla.OPNOT:
                return "OPNOT";
            case Cuadrupla.OPAND:
                return "OPAND";
            case Cuadrupla.OPOR:
                return "OPOR";
            case Cuadrupla.OPSUMA:
                return "OPSUMA";

            case Cuadrupla.OPMENOS:
                return "OPMENOS";
            case Cuadrupla.OPPOR:
                return "OPPOR";
            case Cuadrupla.OPDIV:
                return "OPDIV";
            case Cuadrupla.OPMOD:
                return "OPMOD";
            case Cuadrupla.OPMEN:
                return "OPMEN";
            
            case Cuadrupla.OPMAY:
                return "OPMAY";
            case Cuadrupla.OPIGUAL:
                return "OPIGUAL";
            case Cuadrupla.OPDIS:
                return "OPDIS";
            case Cuadrupla.OPMEI:
                return "OPMEI";
            case Cuadrupla.OPMAI:
                return "OPMAI";
           
            case Cuadrupla.OPIF0:
                return "OPIF0";
            case Cuadrupla.OPIF1:
                return "OPIF1";
            case Cuadrupla.OPASIGNID:
                return "OPASIGNID";
            case Cuadrupla.OPASIGNNUM:
                return "OPASIGNNUM";
            default:
                return "unknown";
        }
    }

}

