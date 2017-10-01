/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo3ayuco;


import Vista.Vista;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Estudiante
 */
public class Interprete {
    private Codigo3 c3;
    private TSS tss;
    private TSVAR tsVar;
     
    private int IP;
    public static Stack<Integer> pilaTemp; //pila de temporales
    public static Stack<Integer> pilaEBP;  //pila de EBPs(para saber desde donde van los temporales de la subrutina)

    public void setIP(int IP) {
        this.IP = IP;
    }
   
    
    
    public Interprete(){
        
        c3 = null;
        pilaTemp = new Stack<Integer>();
        pilaEBP = new Stack<Integer>();        
        //c3.getCuadrupla(IP);
    }
    
    public void open(String filename)
    {
        c3 = new Codigo3();
        boolean exito = c3.Open(filename);
        
        if(exito){
            tss = c3.getTSS();
            tsVar = c3.getTSVar();
            try {
                //IP = c3.primerIP();
                fijarParaMAIN();
            } catch (Exception e) {
                 JOptionPane.showMessageDialog(null,e.getMessage());
            }       
        }
        else
            c3 = null;
    }
  
    public boolean HayCodigoCargado()
    {
        return (c3 != null);
    }
    
    public void run(){
        if(!HayCodigoCargado()){
            return;
        }
    }
    
    public void interpretarCuadrupla(Cuadrupla c){
        int p,dir,salto,vBool,operando1,operando2,resultado;
        String s;
                
        switch (c.getOpCode())
        {
            case Cuadrupla.ETIQUETA:
                IP++;
                break;
            case Cuadrupla.NOP:
                IP++;
                break;
            case Cuadrupla.OPAND:
                //obtengo el valor de los operandos
                
                IP++;
                break;
            case Cuadrupla.OPASIGNID:
                p=c.getDir(2);
                int value;
                
                if(p>0)
                    value = getTemporal(p);
                else
                    value = getVarValor(-p);
                
                p=c.getDir(1);
                if(p>0)
                    setTemporal(p,value);
                else
                    setVarValor(-p,value);
                
                IP++;
                break;
                
            case Cuadrupla.OPASIGNNUM:
                p=c.getDir(2);
                dir=c.getDir(1);
                if(dir>0)
                    setTemporal(dir, p);
                else
                    setVarValor(-dir, p);
                
                IP++;
                break;
            case Cuadrupla.OPCALL:
                pilaEBP.push(pilaTemp.size());
                pilaTemp.push(IP+1);
                
                dir=-c.getDir(1);
                int cantTemp=tsVar.getCantTmp(dir);
                for(int i=1; i<= cantTemp;i++){
                    pilaTemp.push(0);
                }
                IP = tsVar.getValorI(dir);
                break;
                
            case Cuadrupla.OPDEC:
                
                IP++;    
                
                break;
                
            case Cuadrupla.OPDIS:
                                               
                IP++;
                break;
            case Cuadrupla.OPDIV:
                
                IP++;
                break;
            case Cuadrupla.OPGOTO:
                salto = c.getDir(1);
                IP = ipEtiqueta(salto, c3.getCuadruplas());
                
                break;
            case Cuadrupla.OPIF0:
                
                p = c.getDir(1);
                salto=c.getDir(2); // guardo el nro de etiqueta al que quiero ir
                
                if(p>0)
                    vBool = getTemporal(p);
                else
                    vBool = getVarValor(p);
                //luego modifico el IP
                if(vBool == 0)
                    IP = ipEtiqueta(salto, c3.getCuadruplas());
                else
                    IP++;
                
                
                break;    
            case Cuadrupla.OPIF1:
                p = c.getDir(1);
                salto=c.getDir(2); // guardo el nro de etiqueta al que quiero ir
                
                if(p>0)
                    vBool = getTemporal(p);
                else
                    vBool = getVarValor(p);
                
                if(vBool != 0)
                    IP = ipEtiqueta(salto, c3.getCuadruplas());
                else
                    IP++;

                break;
            case Cuadrupla.OPIGUAL:
                
                IP++;
                break;    
            case Cuadrupla.OPINC:
                //obtengo el valor de los operandos
                p=c.getDir(1);
                operando1 = (p>0)? getTemporal(p):tsVar.getValorI(-p);
               
                resultado = ++operando1;
                
                p=c.getDir(1);
                if(p>0)
                    setTemporal(p, resultado);
                else
                    setVarValor(-p,resultado);
                
                IP++;
                break;    
            case Cuadrupla.OPMAI:
                
                IP++;
                
                break;    
            case Cuadrupla.OPMAY:
                
                IP++;
                break; 
            case Cuadrupla.OPMEI:
                //obtengo el valor de los operandos
                p=c.getDir(2);
                operando1 = (p>0)? getTemporal(p):tsVar.getValorI(-p);
                p=c.getDir(3);
                operando2 = (p>0)? getTemporal(p):tsVar.getValorI(-p);
                //calculo el resultado
                resultado = (operando1 <= operando2)? 1: 0;
                
                p=c.getDir(1);
                if(p>0)
                    setTemporal(p, resultado);
                else
                    setVarValor(-p,resultado);
                int x =0;
                //if(operando1>3)
                    //x=10;
                
                IP++;
                break;
            case Cuadrupla.OPMEN:
                
                //obtengo el valor de los operandos
               
                
                IP++;
                break;
            case Cuadrupla.OPMENOS:
                //obtengo el valor de los operandos

                IP++;
                break;
            case Cuadrupla.OPMOD:
                //obtengo el valor de los operandos
                
                IP++;
                break;
            case Cuadrupla.OPNL:
                System.out.println();
                Vista.salida.setText(Vista.salida.getText()+"\n");
                IP++;
                break;
            case Cuadrupla.OPNOT:
                
                IP++;
                break;
            case Cuadrupla.OPOR:
                //obtengo el valor de los operandos
               
                
                IP++;
                break;
            case Cuadrupla.OPPOR:
                //obtengo el valor de los operandos
                
                IP++;
                break;
            case Cuadrupla.OPREAD:
                //int dato = Integer.parseInt(JOptionPane.showInputDialog("ingresa el dato", ""));
                //tsVar.setValorI(-c.getDir(1),dato);
                int dato = Integer.parseInt(JOptionPane.showInputDialog("Ingrese: " + tsVar.getNombreID(-c.getDir(1))));
                tsVar.setValorI(-c.getDir(1),dato);
                IP++;
                
                break;
            case Cuadrupla.OPRET:
                int sizePila = pilaTemp.size();
                int posDR = pilaEBP.pop();//posicion de la pila donde se encuentra la direccion de retorno
                
                while(sizePila > posDR +1){ // ciclo que saca los temporales de la pila 
                    pilaTemp.pop();
                    sizePila--;
                }   
                IP = pilaTemp.pop();
                
                break;
            case Cuadrupla.OPSUMA:
                //obtengo el valor de los operandos
                p=c.getDir(2);
                operando1 = (p>0)? getTemporal(p):tsVar.getValorI(-p);
                p=c.getDir(3);
                operando2 = (p>0)? getTemporal(p):tsVar.getValorI(-p);
                //calculo el resultado
                resultado = operando1+operando2;
                
                p=c.getDir(1);
                if(p>0)
                    setTemporal(p, resultado);
                else
                    setVarValor(-p,resultado);
                
                IP++;
                break;
            case Cuadrupla.OPWRITE:
                
                p = c.getDir(1);
                if(p > 0){
                    dir = pilaEBP.peek()+ p;
                    int temp=pilaTemp.get(dir);
                    System.out.print(temp);
                    Vista.salida.setText(Vista.salida.getText()+temp);
                }    
                else{
                    dir = -c.getDir(1);
                    s=String.valueOf(tsVar.getValorI(dir));
                    System.out.print(s);
                    Vista.salida.setText(Vista.salida.getText()+s);                    
                }
                   
               
                
                IP++;
                
                break;
            case Cuadrupla.OPWRITES: 
                /*int a=0;
                if(c.getDir(1)<0)
                    a=10;
                */
                p = -c.getDir(1);
                Vista.salida.setText(Vista.salida.getText()+tss.getStr(p));
                System.out.print(tss.getStr(p));
                IP++;
                
                int a=0;
                if( -c.getDir(1)!=0)
                    a++;
                break;            
        }
        
        
        
    }
    
    
    //      METODOS AUXILIARES DEL INTERPRETE
    
    private int getVarValor(int index)
    {
        return tsVar.getValorI(index);
    }
    private void setVarValor(Cuadrupla c,int value)
    {
        int index = -c.getDir(1);
        tsVar.setValorI(index, value);
    }
    private void setVarValor(int index, int value)
    {
        tsVar.setValorI(index, value);
    }
    
    
    
    private int getTemporal(int nro)
    {
        int dir = pilaEBP.peek() + nro;
        return pilaTemp.get(dir);
    }
    private void setTemporal(int nro, int value)
    {
        int dir = pilaEBP.peek() + nro;
        pilaTemp.set(dir, value);
    }
    
    public int ipEtiqueta(int etiqueta,Cuadrupla[] vc){ // busca el IP al que debe saltar con el goto
        
        int ipEti=-1;
        int longitud=vc.length;
        
        int i=0;
        while(ipEti == -1 ){
            if(vc[i].getDir(1)==etiqueta && vc[i].getOpCode()==Cuadrupla.ETIQUETA)
                ipEti = i;
            i++;
        }
        return ipEti;
    }
    
    public int getIP()
    {
        return IP;
    }
    public int ultimoIP()//throws MiExcepcion
    {

        //try {
            return this.c3.ultimoIP();
        /*
        } catch (Exception e) {
            throw new MiExcepcion("no se encontro el $MAIN");
        }
        */
        
    }
    public int primerIP()//throws MiExcepcion
    {
        //try {
            return this.c3.primerIP();
        /*   
        } catch (Exception e) {
            throw new MiExcepcion("no se encontro el $MAIN");
        }
        */
        
    }
    public Cuadrupla getCuadrupla(int index)
    {
        return c3.getCuadrupla(index);
    }
    public Codigo3 getC3()
    {
        return this.c3;
    }
    public boolean pilasVacias()
    {
        return pilaEBP.empty() && pilaTemp.empty();
    }
    public void fijarParaMAIN()
    {
        int dir=c3.posMain();
        pilaEBP.push(pilaTemp.size());
        pilaTemp.push(tsVar.getValorF(dir)+1);
                
        int cantTemp=tsVar.getCantTmp(dir);
        for(int i=1; i<= cantTemp;i++){
            pilaTemp.push(0);    
        }
        IP = tsVar.getValorI(dir);
    }
    
    
    
}



