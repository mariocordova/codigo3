/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo3ayuco;

/**
 *
 * @author mariocordova
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Tupla { //Esta class representa la tupla de la TSVAR.
    private String NombreID;
    private int ValorI, ValorF, CantTmp;

    public Tupla(){
        Make("", 0, 0, 0);
    }
    
    public Tupla(String NombreID, int ValorI, int ValorF, int CantTmp) {
        Make(NombreID, ValorI, ValorF, CantTmp);
    }

    public void setNombreID(String NombreID) {
        this.NombreID = NombreID;
    }
    
    public void setValorF(int ValorF) {
        this.ValorF = ValorF;
    }
    
    public void setValorI(int ValorI) {
        this.ValorI = ValorI;
    }


    public void setCantTmp(int CantTmp) {
        this.CantTmp = CantTmp;
    }
    

    public String getNombreID() {
        return NombreID;
    }
  
    public int getValorI() {
        return ValorI;
    }
    
    public int getValorF() {
        return ValorF;
    }
    
    
    public int getCantTmp() {
        return CantTmp;
    }

    public final void Make(String NombreID, int ValorI, int ValorF, int CantTmp){  //Actualiza TODOS los campos.
        this.NombreID = NombreID;
        this.ValorI   = ValorI;
        this.ValorF   = ValorF;
        this.CantTmp  = CantTmp;
    }
    
    
    boolean Save(DataOutputStream F){    //Guarda la tupla al flujo F. Si hay error, return false.
        try{
            F.writeUTF(NombreID);
            F.writeInt(ValorI);
            F.writeInt(ValorF);
            F.writeInt(CantTmp);
            return true;
        } catch(Exception e){}
        
        return false;
    }

    
    boolean Open(DataInputStream F){ //Lee del flujo F, una tupla.
        try{
            NombreID = F.readUTF();
            ValorI   = F.readInt();
            ValorF   = F.readInt();
            CantTmp  = F.readInt();
            return true;
        }catch (Exception e){}
        
        return false;
    }
    
}

