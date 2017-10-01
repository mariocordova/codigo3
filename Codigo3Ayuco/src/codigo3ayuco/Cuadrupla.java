/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo3ayuco;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Cuadrupla {
    
    /* CONSTANTES PARA EL OpCode (usan el prefijo "OP" de "OPeration").  ¡¡NO MODIFIQUE SUS VALORES!! */
    public static final int NOP       = 0;      //No-Operation  
    public static final int OPNL      = 1;      //Salto de línea.
    public static final int OPRET     = 2;      //return.
    
    public static final int OPCALL    = 3;      //e.g. CALL Proc
    public static final int OPGOTO    = 4;      //e.g. GOTO Etiqueta
    
    public static final int OPINC     = 5;      //e.g. INC Var
    public static final int OPDEC     = 6;      //e.g. DEC Var
    
    public static final int OPWRITE   = 7;      //e.g. WRITE(VAR)
    public static final int OPREAD    = 8;      //e.g. READ(Var)
    public static final int OPWRITES  = 9;      //e.g. WRITES(StringCtte)
    
    public static final int ETIQUETA  = 10;     //e.g.  E1

    public static final int OPNOT     = 11;     // !  (not)
    public static final int OPAND     = 12;     // &&   e.g. var = var and var
    public static final int OPOR      = 13;     // ||

    public static final int OPSUMA    = 14;     // +    e.g. var = var + var
    public static final int OPMENOS   = 15;     // -
    public static final int OPPOR     = 16;     // *
    public static final int OPDIV     = 17;     // DIV  (división entera)
    public static final int OPMOD     = 18;     // MOD
    
    public static final int OPMEN     = 19;     // <    e.g. var = (var < var)
    public static final int OPMAY     = 20;     // >
    public static final int OPIGUAL   = 21;     // ==
    public static final int OPDIS     = 22;     // !=  (distinto)
    public static final int OPMEI     = 23;     // <=
    public static final int OPMAI     = 24;     // >=
    
    public static final int OPIF0     = 25;     //e.g. IF (Var=0) => GOTO Etiqueta
    public static final int OPIF1     = 26;     //e.g. IF (Var=1) => GOTO Etiqueta 
    
    public static final int OPASIGNID = 27;     //e.g. Var=Var
    public static final int OPASIGNNUM= 28;     //e.g. Var=Número
    
    
    
        /* CAMPOS DE LA CLASS */
    private int OpCode;          //Código de Operación (usa las constantes definidas arriba).
    private int Dir[];           //Direcciones de memoria.
    
        /* METODOS DE LA CLASS */
    public Cuadrupla(){
       Dir = new int[3];
       Make(NOP, 0, 0, 0);
    }

    public Cuadrupla(int TipoOp, int Dir1, int Dir2, int Dir3){
        Dir = new int[3]; 
        Make(TipoOp, Dir1, Dir2, Dir3);    
    }
    

    public void setOpCode(int TipoOp) { //Setter del Código de Operación
        this.OpCode = Validar(TipoOp);
    }
    
    public void setDir(int Index, int dir){  //Setter de la Dir de Memoria Index (1<=Index<=3)
        if (1 <= Index && Index <= 3)        //e.g  C.setDir(1, 2), actualizará la 1era. Dir. de Mem con 2.
            Dir[Index-1] = dir;
    }

    
    public int getOpCode() { //Getter del Código de Operación
        return OpCode;
    }
    
    public int getDir(int Index){  //Getter de la Dir de Memoria Index (1<=Index<=3)
        if (1 <= Index && Index <= 3)
            return Dir[Index-1];
        
        return 0;
    }

    
    public final void Make(int OpCode, int Dir1, int Dir2, int Dir3){  //Actualiza TODOS los campos de la Cuadrupla.
       this.OpCode = OpCode;
       this.Dir[0] = Dir1;
       this.Dir[1] = Dir2;
       this.Dir[2] = Dir3;
    }
    
    
    boolean Save(DataOutputStream F){
        try{
            F.writeInt(OpCode);
            F.writeInt(getDir(1));
            F.writeInt(getDir(2));
            F.writeInt(getDir(3));
            return true;
        } catch(Exception e){}
        
        return false;
    }

    
    boolean Open(DataInputStream F){
        try{
            OpCode = F.readInt();
            setDir(1, F.readInt());
            setDir(2, F.readInt());
            setDir(3, F.readInt());
            return true;
        }catch (Exception e){}
        
        return false;
    }
    
    private static final String DESCONOCIDO="??";
    public String toString(Codigo3 c3){
        TSVAR TsVar = c3.getTSVar();
        TSS   Tss   = c3.getTSS();
        
        String Simbolo = SimboloOP();
        int Dir1       = getDir(1);        
        String Arg;        
        
        if (OPCALL <= OpCode && OpCode<=OPDEC){      //Estas operaciones tienen un argumento, pero no usan paréntesis.
            if (OpCode == OPGOTO)
                Arg = Etiqueta(Dir1);
            else                        
                if (OpCode == OPCALL)
                    Arg = getNombreProc(TsVar, Dir1);
                else
                    Arg = getNombreVar(TsVar, Dir1);

            return Simbolo +" "+Arg;
        }
        
        if (OPWRITE <= OpCode && OpCode<=OPWRITES){  //Estas operaciones tienen un argumento y usan paréntesis.            
            if (OpCode == OPWRITES)
                Arg = getStrCtte(Tss, Dir1);
            else
                Arg = getNombreVar(TsVar, Dir1);
            
            return Simbolo +"("+Arg+")";
        }    
        
        if (OPAND <= OpCode && OpCode <= OPMOD)
            return getNombreVar(TsVar, Dir1)+" = "+getNombreVar(TsVar, getDir(2))+" "+Simbolo+" "+getNombreVar(TsVar, getDir(3));

        if (OPMEN <= OpCode && OpCode <= OPMAI)
            return getNombreVar(TsVar, Dir1)+" = ("+getNombreVar(TsVar, getDir(2))+" "+Simbolo+" "+getNombreVar(TsVar, getDir(3))+")";
        
        int Dir2 = getDir(2);
        switch (OpCode){
            case ETIQUETA   : return Etiqueta(Dir1)+":";        
            case OPNOT      : return getNombreVar(TsVar, Dir1)+" = NOT "+getNombreVar(TsVar, Dir2);
            case OPIF0      : return "IF ("+getNombreVar(TsVar, Dir1)+"=0) => GOTO "+Etiqueta(Dir2);
            case OPIF1      : return "IF ("+getNombreVar(TsVar, Dir1)+"=1) => GOTO "+Etiqueta(Dir2);
            case OPASIGNID  : return getNombreVar(TsVar, Dir1)+" = "+getNombreVar(TsVar, Dir2);
            case OPASIGNNUM : return getNombreVar(TsVar, Dir1)+" = "+Dir2;    
        }
        
        return Simbolo;
    }
    
    
    
    //*****
    private int Validar(int TipoOp){ //Valida el Tipo de Operación
        if (NOP <= TipoOp && TipoOp <= OPASIGNNUM)
            return TipoOp;
        
        return NOP;
    }
        
    
    private String SimboloOP(){ //Devuelve el simbolo o string que caracteriza al Tipo de Operación de la Cuadrupla.
        final String Simbolo[]={"NOP", "NL", "RET", "CALL", "GOTO", "INC", "DEC", "WRITE", "READ", "WriteS", "E", 
                                "NOT", "AND", "OR", "+", "-", "*", "DIV", "MOD", "<", ">", "==", "!=", "<=", ">=", "IF", "IF"};        
        
        if (NOP <= OpCode && OpCode <= OPIF1)
            return Simbolo[OpCode];
        
        return DESCONOCIDO;     
    }
    
        
    private String Etiqueta(int Dir){
        if (Dir<=0)
            return "E "+DESCONOCIDO;
        
        return "E"+Dir;
    }
    
    
    private String getNombreVar(TSVAR TsVar, int Dir){
        if (Dir > 0)
            return "t"+Dir;
        
        Dir = -Dir;
        
        if (TsVar == null)
            return "Var"+Dir;
                    
        if (TsVar.isVar(Dir))
            return TsVar.getNombreID(Dir);
        
        return DESCONOCIDO;
    }

    
    
    private String getNombreProc(TSVAR TsVar, int Dir){
        if (Dir <= 0){
            Dir = -Dir;
            if (TsVar == null)
                return "Proc"+Dir;
        
            if (TsVar.isProc(Dir))
                return TsVar.getNombreID(Dir);
        }
        return DESCONOCIDO;        
    }

    
    private String getStrCtte(TSS Tss, int Dir){
        if (Dir <= 0){
            Dir = -Dir;
            if (Tss == null)
                return "Str"+Dir;
        
            if (Tss.PosValida(Dir))
                return '"'+Tss.getStr(Dir)+'"';
        }
        return DESCONOCIDO;                
    }
    

    
}

