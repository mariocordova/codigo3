/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo3ayuco;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Codigo3 {
    private Cuadrupla V[];
    private int n;
    
    private TSVAR TsVar;    //Tabla de símbolos de Variables y Procs.
    private TSS   Tss;      //Tabla de símbolos de StringCtte's
    
    private int CantTmp;    //Contador de temporales.
    private int CantLbl;    //Contador de Etiquetas.

    public static final int MAXCUADRUPLAS=500;
    
    public Codigo3(){
        Make(null, null);
    }
    
    public Codigo3(TSVAR TsVar, TSS Tss) { //En la Arq. de un Compilador, se observa la relación del C3 con la TS.
        Make(TsVar, Tss);
    }

    private void Make(TSVAR TsVar, TSS Tss) {
        this.TsVar = TsVar;
        this.Tss   = Tss;
         
        V = new Cuadrupla[MAXCUADRUPLAS+1];
        n = -1;
        CantLbl = 0;
    }
    
    public TSVAR getTSVar() {  //Getter de la TSVAR utilizada por el C3.
        return TsVar;
    }

    public TSS getTSS() {   //Getter de la TSS utilizada por el C3.
        return Tss;
    }

    public void setTSVar(TSVAR TsVar) {  //Setter de la TSVAR utilizada por el C3.
        this.TsVar = TsVar;
    }

    public void setTSS(TSS Tss) {  //Setter de la TSS utilizada por el C3.
        this.Tss = Tss;
    }
    
           
    public int lenght(){ //Devuelve la cantidad de Cuadruplas insertadas en el C3.
        return n+1;
    }
    
    
    public int add(int TipoOp){ //Inserta la Cuadrupla (TipoOp, 0, 0, 0) devolviendo la posición donde fue insertada.
        return add(TipoOp, 0, 0, 0); 
    }

    
    public int add(int TipoOp, int Dir1){ //Inserta la Cuadrupla (TipoOp, Dir1, 0, 0) devolviendo la posición donde fue insertada.
        return add(TipoOp, Dir1, 0, 0); 
    }

    
    public int add(int TipoOp, int Dir1, int Dir2){ //Inserta la Cuadrupla (TipoOp, Dir1, Dir2, 0) devolviendo la posición donde fue insertada.
        return add(TipoOp, Dir1, Dir2, 0); 
    }
    

    public int add(int TipoOp, int Dir1, int Dir2, int Dir3){ //Inserta la Cuadrupla (TipoOp, Dir1, Dir2, Dir3) devolviendo la posición donde fue insertada.
        if (n < MAXCUADRUPLAS){
            n++;
            if (V[n]==null)
                V[n] = new Cuadrupla(TipoOp, Dir1, Dir2, Dir3);
            else
                V[n].Make(TipoOp, Dir1, Dir2, Dir3);
            
            return n;    
        }        
        return -1;  //La Cuadrupla no fue insertada.
    }

    
    public int add(Cuadrupla C){ //Inserta la Cuadrupla C al Codigo3, devolviendo la posición donde fue insertada.
        if (n < MAXCUADRUPLAS && C!=null){
            n++;
            V[n]=C;
            return n;
        }
        return -1;  //La cuadrupla no fue insertada.
    }
    
    
    public Cuadrupla getCuadrupla(int Index){   //Devuelve la Cuadrupla de la posición Index (0<= Index <=length()-1)
        if (IndexCorrecto(Index))
            return V[Index];
        
        return null;
    }

    
    public boolean IndexCorrecto(int Index){
        return (0 <= Index && Index <= n);
    }
    
    
    public boolean Save(String Filename){  //Guarda el C3, con sus tablas TSVAR y TSS y su CantTemp al archivo Filename. Si hay error, devuelve false.
        DataOutputStream Out = AbrirEscribir(Filename);
        if (Out == null)
            return false;
        
        final String Msj = "Codigo3.Save: Error al escribir en el archivo "+'"'+Filename+'"';
        try{
            Out.writeInt( lenght() );       //Guardar Cant. de Cuadruplas.
            
                //Guardar las Cuadruplas.
            boolean b = true;
            int i=0;
            while (b && i<=n){
                b = V[i].Save(Out);   //Guardar la Cuadrupla V[i].
                i++;
            }
            
                //Guardar TSVAR
            if (TsVar==null)
                Out.writeInt(0);    //Indicarle al Open que no hay tuplas.
            else            
                b= b && TsVar.Save(Out);
            
                //Guardar TSS
            if (Tss == null)
                Out.writeInt(0);
            else                            
                b=b && Tss.Save(Out);
                        
            Out.close();
            if (!b){  //Hubo error al escribir algunos de los elementos.     
                System.err.println(Msj);
                return false;
            }
            
            return true;
        }catch(Exception e){ 
            System.err.println(Msj);
        }
        
        return false;
    }
    
    
    public boolean Open(String Filename){        
        DataInputStream in = AbrirLeer(Filename);
        if (in==null)
            return false;

        final String Msj = "Codigo3.Open: El archivo "+'"'+Filename+'"'+ " no tiene el formato de un .c3";
        try{
            n = in.readInt() - 1;       //Leer Cant. de Cuadruplas.
            
                //Leer las Cuadruplas.
            boolean b = true;
            int i=0;
            while (b && i<=n){
                if (V[i]==null)
                    V[i]= new Cuadrupla();
                
                b = V[i].Open(in);   //Leer la Cuadrupla V[i].
                i++;
            }
            
                //Leer la TSVAR
            if (TsVar == null)
                TsVar = new TSVAR();
            
            b= b && TsVar.Open(in);
            
                //Leer la TSS
            if (Tss == null)
                Tss = new TSS();
            
            b = b && Tss.Open(in);
            
            in.close();
            
            if (!b){  //Hubo error al leer algunos de los elementos.     
                System.err.println(Msj);
                return false;
            }
            
            return true;
        }catch(Exception e){
            System.err.println(Msj);
        }

        return false;
    }
    
    
//***** MANEJO DE LOS TEMPORALES y ETIQUETAS (Usado por el compiler).
    public int getNextLabel(){  //Devuelve el número de la prox. etiqueta.
        return ++CantLbl;
    }

    public void InitTmp(){  //Inicializa el contador de temporales.
        CantTmp = 0;
    }
    
    public int getNextTmp(){    //Devuelve el número del prox. temporal.
        return ++CantTmp;
    }
    
    public int getCantTmp() {  //Getter de la cantidad de temporales utilizados.
        return CantTmp;
    }
    
    public void setCantTemp(int CantTemp) {  //Setter de la cantidad de temporales utilizados.
        this.CantTmp = CantTemp;
    }
//***** END Manejo de Temporales y Etiquetas.
    
    @Override
    public String toString(){  //Para usar con Print.
        if (lenght()==0)
            return "(Código3 Vacio)";
        
        final char LF ='\n';
        String S = "";
        for (int i=0; i<=n; i++)
            S += Utils.FieldRight(""+i, 3)+" "+V[i].toString(this)+LF;
        
        if (Tss == null || TsVar == null){
            S += "-------"+LF;
            if (TsVar == null)
                S+="  *Los ID's se muestran sin nombre, porque la TSVAR no fue especificada (o sea vale null)."+LF;

            if (Tss == null)
                S+="  *Las StringCtte's se muestran sin contenido, porque la TSS no fue especificada (o sea vale null)."+LF;

        }
        return S;
    }  

    
    
    private DataOutputStream AbrirEscribir(String Filename){ //Abre, para escribir, el archivo FileName, retornado el flujo abierto.
        try{
            FileOutputStream F   = new FileOutputStream(Filename);    //Crear archivo (sobreescribiendo si existiese).
            DataOutputStream Out = new DataOutputStream(F);     
            return Out;
        }catch(IOException e){
            System.err.println("Codigo3.Save: Error al guardar "+e.getMessage());
        }

        return null;
    }
    
    private DataInputStream AbrirLeer(String Filename){ //Abre, para leer, el archivo FileName, retornado el flujo abierto.
        try{
            FileInputStream F  = new FileInputStream(Filename);     
            DataInputStream in = new DataInputStream(F);
            return in;
        }catch(IOException e){
            System.err.println("Codigo3.Open: Error al abrir "+e.getMessage());
        }
        
        return null;
    }    
    
    public Cuadrupla[] getCuadruplas()
    {
        return this.V;
    }
    public int ultimoIP() //throws MiExcepcion
    {
        
        int i = 0;
        while(i<TsVar.lenght())
        {
            if("$MAIN".equals(TsVar.getNombreID(i)))
                return TsVar.getValorF(i);
            i++;
        }
        //throw new MiExcepcion("no se encontro el $MAIN");
        return -1;
    }
    public int primerIP()//throws MiExcepcion
    {
        
        int i = 0;
        while(i<TsVar.lenght())
        {
            String name = TsVar.getNombreID(i);
            if("$MAIN".equals(name))
                return TsVar.getValorI(i);
            i++;
        }
        return -1;
        //throw new MiExcepcion("no se encontro el $MAIN");
        
    }
    public int posMain()
    {
        
        int i = 0;
        while(i<TsVar.lenght())
        {
            String name = TsVar.getNombreID(i);
            if("$MAIN".equals(name))
                return i;
            i++;
        }
        return -1;
        
    }

    
}


  
