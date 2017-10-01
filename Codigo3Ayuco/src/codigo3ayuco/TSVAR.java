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

public class TSVAR {    //Esta class manipula la Tabla de Simbolos de los ID's.
    
        /* CONSTANTES QUE DEFINEN LOS TIPOS DE LAS VARIABLES */
    public static final int TIPOINT     = -2;
    public static final int TIPOBOOLEAN = -3;
    
        /* CAMPOS DE LA CLASS */
    private Tupla V[];      //Almacenará las entradas (tuplas) de la tabla (mejor si se usa una Lista).
    private int n;          //Dimensión de V[].
    
        /* METODOS DE LA CLASS */
    public static final int MAXIDS = 50;
    public TSVAR(){     //Construye una tabla vacía        
        V = new Tupla[MAXIDS+1];
        n = -1;
    }
    
    
    public void Init(){ //Inicializa la TSVAR.  Es decir, TSVAR=(Vacía).
        n=-1;
    }
    
    public int lenght(){    //Devuelve la cantidad de elementos de la tabla.
        return n+1;
    }
    
    public int Existe(String NombreID){ //Devuelve la posición (índice de V[]) donde se encuentra el ID con NombreID.  Si no existe devuelve -1.
        NombreID = Validar(NombreID);
        for (int i=0; i<=n; i++){
            if (V[i].getNombreID().equals(NombreID))
                return i;
        }
        return -1;
    }
    
    
    public int addProc(String NombreID){ //Inserta un nuevo Proc a la TSVAR con C3 vacío (ValorI=ValorF=-1). Devuelve la posición donde está/fue alojado.
        return addTupla(NombreID, -1, -1, 0);
    }
    
    
    public int addVar(String NombreID, int Tipo){ //Inserta una nueva Var con su Tipo  a la TSVAR. Devuelve la posición donde está/fue alojada.
        if (TipoCorrecto(Tipo))
            return addTupla(NombreID, 0, Tipo, 0);
        
        return -1;      //Devolver -1 para que el llamante sepa que la Variable no se insertó a la tabla.        
    }
    
    
    public boolean isProc(int Index){  //Devuelve true sii la tupla de la posición Index es un Proc
        return (PosValida(Index) && V[Index].getValorF() >= -1);
    }

    
    public boolean isVar(int Index){  //Devuelve true sii la tupla de la posición Index es una Var.
        return (PosValida(Index) && V[Index].getValorF() < -1);
    }
    
    
    
//******** SETTER's & GETTER's de la tupla especificada en la pTS Index ********
    public void setValorI(int Index, int ValorI){   //Actualiza el ValorI de la Tupla Index
        if (PosValida(Index))
            V[Index].setValorI(ValorI);        
    }
    
    public void setValorF(int Index, int ValorF){   //Actualiza el ValorF de la Tupla Index (sii es un Proc).
        if (isProc(Index))
            V[Index].setValorF(ValorF);        
    }
    
    public void setTipo(int Index, int Tipo){   //Actualiza el ValorF de la Tupla Index (sii es una Var).
        if (isVar(Index) && TipoCorrecto(Tipo))
            V[Index].setValorF(Tipo);        
    }
    
    public void setCantTmp(int Index, int CantTmp){   //Actualiza el CantTmp de la Tupla Index (sii es una Proc).
        if (isProc(Index))
            V[Index].setCantTmp(CantTmp);
    }
    
    
    public Tupla getTupla(int Index){ //Devuelve la Tupla de la posición Index
        if (PosValida(Index))
            return V[Index];
        
        return null;
    }
  
    
    public String getNombreID(int Index){ //Devuelve el nombre del ID alojado en la posición Index
        if (PosValida(Index))
            return V[Index].getNombreID();
        
        return "";
    }
    
    public int getValorI(int Index){ //Devuelve el ValorI de la tupla Index
        if (PosValida(Index))
            return V[Index].getValorI();
        
        return 0;
    }

    public int getValorF(int Index){ //Devuelve el ValorF de la tupla Index
        if (PosValida(Index))
            return V[Index].getValorF();
        
        return 0;
    }
    
    public int getCantTmp(int Index){ //Devuelve el campo CantTmp de la tupla Index
        if (PosValida(Index))
            return V[Index].getCantTmp();
        
        return 0;
    }
//****** END Setter's & Getter's de la tupla especificada en la pTS Index ******    
    
    
    public boolean PosValida(int Index){
        return (0 <= Index && Index <= n);
    }
    
    
    boolean Save(DataOutputStream F){    //Guarda la TSVAR al Flujo F. Si hubo error al guardar, devuelve false.        
        try{
                //Guardar la Cant. de Tuplas.
            F.writeInt( lenght() );

                //Guardar las Tuplas.
            boolean b = true;
            int i=0;
            while (b && i<=n){
                b = V[i].Save(F);   //Guarda la tupla.
                i++;
            }
            
            return b;                
        }catch(Exception e){}
        
        return false;
    }

    
    boolean Open(DataInputStream F){    //Lee la TSVAR del Flujo F.
        try{
                //Leer la Cant. de Tuplas.
            n= F.readInt()-1;
            
                //Leer las tuplas.
            boolean b = true;
            int i=0;
            while (b && i<=n){
                if (V[i]==null)
                    V[i] = new Tupla();
                
                b = V[i].Open(F);   //Lee la tupla.
                i++;
            }
            
            return b;                
        }catch(Exception e){}
        
        return false;
    }
    
 //*****
    private static String TITLE[]={"NombreID", "ValorI", "ValorF", "CantTmp"};
    
    @Override
    public String toString(){ //Para mostrar la TSVAR usando Print
        if (lenght()==0)
            return "(TSVAR Vacía)";
        
        final char   LF ='\n';
        final String PADDLEFT = "   ";
        
        String Result;
        int FieldNombre  = FieldLength(0);
        int FieldValorI  = FieldLength(1);
        int FieldValorF  = FieldLength(2);
        int FieldCantTmp = FieldLength(3);
        int Total = FieldNombre +FieldValorI + FieldValorF+FieldCantTmp;
        
        String BordeSup = PADDLEFT+" "+Utils.RunLength('_', Total);
        String Title    = PADDLEFT+"|"+
                          Utils.FieldCenter(TITLE[0], FieldNombre)+
                          Utils.FieldCenter(TITLE[1], FieldValorI)+
                          Utils.FieldCenter(TITLE[2], FieldValorF)+
                          Utils.FieldCenter(TITLE[3], FieldCantTmp)+"|";
        String BordeInf = PADDLEFT+"+"+Utils.RunLength('-', Total)+"+";
        
        Result = BordeSup + LF + Title + LF + BordeInf + LF;
            
        for (int i=0; i<=n; i++){
            String Posicion = Utils.FieldRight(""+i, PADDLEFT.length())+"|";
            String Nombre   = Utils.FieldLeft(" "+getElem(i, 0), FieldNombre);
            String ValorI   = Utils.FieldCenter(getElem(i, 1), FieldValorI);
            String ValorF   = Utils.FieldCenter(getElem(i, 2), FieldValorF);
            String CantTmp  = Utils.FieldCenter(getElem(i, 3), FieldCantTmp)+"|";
            Result += Posicion + Nombre + ValorI + ValorF + CantTmp + LF +"\n";
        } //End For 
        
        return Result + BordeInf + LF;
    }
    
    private int FieldLength(int Col){
        int May = TITLE[Col].length();
        for (int i=0; i<=n; i++){
            int Lon = getElem(i, Col).length();
            if (Lon > May)
                May = Lon;
        }
        return May+2;
    }
    
    private String getElem(int Fila, int Col){
        switch (Col){
            case 0  :   return V[Fila].getNombreID();
            case 1  :   return ""+V[Fila].getValorI();
            case 2  :   if (isVar(Fila))
                            return (V[Fila].getValorF()==TIPOINT?"TIPOINT":"TIPOBOOLEAN");
                        return ""+V[Fila].getValorF();
        }
        
        if (isVar(Fila))
           return "-";
        
        return ""+V[Fila].getCantTmp();
    }
//*****   
    
    private int addTupla(String NombreID, int ValorI, int ValorF, int CantTmp){ //Inserta una nueva tupla, validando NombreID.
        NombreID = Validar(NombreID);
         if (NombreID.isEmpty())   //Si el NombreID es vacio...
            return -1;              //...devolver -1, indicando que la tupla no se insertó.
         
        int Pos = Existe(NombreID);
        if (Pos != -1){  //El ID ya existe en la TSVAR.
            V[Pos].Make(NombreID, ValorI, ValorF, CantTmp);
            return Pos;             
        }    
        
        if (n >= MAXIDS)    
            return -1;      //No hay mas espacio en V[]. 
         
        n++;
        if (V[n]== null)
            V[n] = new Tupla(NombreID, ValorI, ValorF, CantTmp); 
        else
            V[n].Make(NombreID, ValorI, ValorF, CantTmp);
        
        return n;   //Devolver la posición donde fue insertada la nueva tupla.
    }
    
    
    private boolean TipoCorrecto(int Tipo){ //Devuelve true sii el int especificado, corresponde a TIPOBOOLEAN o TIPOINT.
        return (TIPOBOOLEAN <= Tipo && Tipo <= TIPOINT);
    }  
    
    
    private String Validar(String NombreID){ //Realiza un semi validación del NombreID. (Si el usuario de esta class fuese el compilador, esta función es innecesaria).
        NombreID = NombreID.trim();    //Eliminar espacios iniciales y finales
        if (NombreID.charAt(0)=='$')
            NombreID = NombreID.toUpperCase();        
        
        return NombreID.replace(' ', '_');
    }
    
}

