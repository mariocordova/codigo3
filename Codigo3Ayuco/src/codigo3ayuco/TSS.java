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
import java.util.ArrayList;

public class TSS { //Esta class manipula la TS de las String cttes
    private ArrayList <String> L;
    
    public TSS(){
        L = new ArrayList<String>();
    }
   
    public void Init(){ //Inicializa la TSS.  Es decir, TSS=(Vacía).
        L.clear();
    }
    
    public int length(){
        return L.size();
    }
    
    
    public int Existe(String Str){ //Devuelve la posición (índice de L) donde se encuentra la StringCtte Str.  Si no existe devuelve -1.
        for (int i=0; i<=length()-1; i++){
            if (L.get(i).equals(Str))
                return i;
        }
        return -1;
    }

    public int add(String Str){ //Inserta una nueva StringCtte. Devuelve la posición donde está/fue alojado.  Si no se pudo insertar return -1.
        int pos = Existe(Str);
        if (pos !=-1)
            return pos;     //La Str ya existe en la TSS.
        
        L.add(Str);            
        return L.size()-1;  //La Str fue insertada al final.
    }
    
    public String getStr(int Index){ //Devuelve la StingCtte de la posición Index
        if (!PosValida(Index)){ //Diverge.
            System.err.println("TSS.getStr:Posición inválida.");
            return "";
        }    
        return L.get(Index);
    }
    
    
    public boolean PosValida(int Index){
        return (0 <= Index && Index <= length()-1);
    }

    public boolean Save(DataOutputStream F){    //Guarda la TSS al Flujo F. Si hay error, return false.
        try{            
                //Guardar la Cant. de StringCtte's.
            int n=length();
            F.writeInt( n );
            
                //Guardar los StringCtte's.
            for (int i=0; i<n; i++)
                F.writeUTF(L.get(i));
            
            return true;                
        }catch(Exception e){}
        
        return false;
    }

    
    public boolean Open(DataInputStream F){    //Lee la TSS del Flujo F.        
        ArrayList <String> Aux = new ArrayList<String>();
        
        try{            
                //Leer la Cant. de StringCtte's.            
            int n = F.readInt();
            
                //Leer los StringCtte's y depositarlas en la lista Aux.
            for (int i=1; i<=n; i++)
                Aux.add( F.readUTF() );
            
                //Copiar lista Aux a L y liberar Aux
            L.clear();      L=null;            
            L = Clonar(Aux);
            Aux.clear();    Aux = null;
            
            return true;                
        }
        catch(Exception e){
        }
        
        return false;
    }

    
//*****    
    private static String TITLE = "TSS";
    
    @Override
    public String toString(){ //Para mostrar la TSS con Print
        if (length()==0)
            return "("+TITLE+" Vacía)";
       
        final char   LF ='\n';
        final String PADDLEFT = "   ";
        
        String Result;
        int n = LongitudFila();
        
        String BordeSup  = PADDLEFT+' '+Utils.RunLength('_', n);
        String Titulo    = PADDLEFT+'|'+Utils.FieldCenter(TITLE, n)+'|';
        String BordeInf  = PADDLEFT+'+'+Utils.RunLength('-', n)+'+';
        
        Result = BordeSup + LF + Titulo + LF + BordeInf + LF;
        
        int FieldPos = PADDLEFT.length();
        for (int i=0; i<=length()-1; i++){
                String Posicion = Utils.FieldRight(""+i, FieldPos);
                String Fila     = '|' +  Utils.FieldLeft('"'+L.get(i)+'"', n) + '|';
                Result += Posicion + Fila + LF;
        }
        
        return Result+BordeInf+LF; 
    }
    
    
    private int LongitudFila(){ //Corrutina de print().
        int May=TITLE.length();
        for (int i=0; i<=length()-1; i++){
            int LonStr = L.get(i).length();
            if (LonStr > May)
                May = LonStr;
        }
        
        return May+2;   //+2 comillas
    }    
//*****
    
    private ArrayList <String> Clonar(ArrayList <String> L){
        ArrayList <String> Aux = new ArrayList<String>();
        for (int i=0; i<=L.size()-1; i++)
            Aux.add( L.get(i) );
        return Aux;
    }
    
    public String imprimirTss()
    {
        String s ="";
        for (int i = 0; i < L.size(); i++) {
            s+= i+"   "+L.get(i)+"\n";
            
        }
        return s;
    }
}
