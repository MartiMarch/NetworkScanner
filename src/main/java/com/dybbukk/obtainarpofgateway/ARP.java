package com.dybbukk.obtainarpofgateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class ARP {
    private String ip_1 = "192.168.1.0", ip_2 = "192.168.1.255";
    private static final char PUNTO = '.';
    private ArrayList<String> tablaARP = new ArrayList();
    
    public ARP(){}
    
    public ARP (String ip_1, String ip_2)
    {
        if(validarIP(ip_1) && validarIP(ip_2))
        {
            this.ip_1 = ip_1;
            this.ip_2 = ip_2;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Introduce las IP's con el formato correcto.");
        }
    }

    public void setIp_1(String ip_1) {
        if(validarIP(ip_1))
        {
            this.ip_1 = ip_1;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Introduce unaIP con el formato correcto.");
        }
    }

    public void setIp_2(String ip_2) {
        if(validarIP(ip_2))
        {
            this.ip_2 = ip_2;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Introduce unaIP con el formato correcto.");
        }
    }

    public String getIp_1() {
        return ip_1;
    }

    public String getIp_2() {
        return ip_2;
    }
    
    public boolean validarIP(String ip)
    {
        boolean validacion = false;
        Pattern patron = Pattern.compile("[0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}");
        Matcher comparador = patron.matcher(ip);
        if(comparador.find())
        {
            validacion = true;
        }
        ip += ".";
        String sub_ip;
        int i_anterior = 0;
        for(int i = 0; i < ip.length() && validacion; ++i)
        {
            sub_ip = "";
            while( ip.charAt(i) != PUNTO && i < ip.length())
            {
                ++i;
            }
            sub_ip = ip.substring(i_anterior, i);
            i_anterior = i + 1;
            try{
                int numero = Integer.parseInt(sub_ip);
                if(!(numero >= 0 && numero <= 255))
                {
                    validacion = false;
                }
            }
            catch (NumberFormatException ex){
                validacion = false;
            }
        }
        return validacion;
    }
    
    public int comparacion(String ip1, String ip2)
    {
        int resultado = 0;
        // 0 son iguales
        // +1 Incrementar
        // -1 Decrementar
        ArrayList<Integer> partes1 = obtenerPartes(ip1);
        ArrayList<Integer> partes2 = obtenerPartes(ip2);
        if(partes1.get(0) < partes2.get(0))
        {
            resultado = 1;
        }
        else if (partes1.get(0) > partes2.get(0))
        {
            resultado = -1;
        }
        else
        {
            if(partes1.get(1) < partes2.get(1))
            {
                resultado = 1;
            }
            else if(partes1.get(1) > partes2.get(1))
            {
                resultado = -1;
            }
            else
            {
                if(partes1.get(2) < partes2.get(2))
                {
                    resultado = 1;
                }
                else if(partes1.get(2) < partes2.get(2))
                {
                    resultado = -1;
                }
                else
                {
                    if(partes1.get(3) < partes2.get(3))
                    {
                        resultado = 1;
                    }
                    else if(partes1.get(3) < partes2.get(3))
                    {
                        resultado = -1;
                    }
                    else
                    {
                        resultado = 0;
                    }
                }
            }
        }
        return resultado;
    }
    
    public void escanearRed()
    {
        String os = System.getProperty("os.name");
        os = os.toLowerCase();
        if(os.contains("windows"))
        {
            String ip = this.ip_1;
            String salida = "";
            System.out.println("Escaneando la red, puede tardar unos minutos.");
            while(!this.ip_2.equals(ip))
            {
                if(comparacion(ip_2, ip) < 0)
                {
                    ip = incrementarIP(ip);
                }
                else if(comparacion(ip_2, ip) > 0)
                {
                    ip = decrementarIP(ip);
                }
                //salida = ejecucionCMD("ping " + ip + " -n 1 -w 1 -l 0.5");
                salida = ejecucionCMD("ping " + ip);
                existenciaIP(salida, ip);
            }
        }
        else
        {
            
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Imprimiendo la tabla ARP de la puerta de enlace: ");
        for(int i = 0; i < tablaARP.size(); ++i)
        {
            System.out.println("  " + tablaARP.get(i));
        }
        System.out.println("------------------------------------------------------------------------");
    }
    
    public String ejecucionCMD(String comando)
    {
        String salidaConsola = "";
        try {
            ProcessBuilder instruccion = new ProcessBuilder("cmd.exe", "/c", comando);
            instruccion.redirectErrorStream(true);
            Process proceso = instruccion.start();
            BufferedReader salida = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            boolean ejecucion = true;
            while (ejecucion) 
            {    
                if (salida.readLine() == null)
                { 
                    ejecucion = false; 
                }
                else
                {
                    salidaConsola += salida.readLine() + "\n";
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ARP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salidaConsola;
    }
    
    public String incrementarIP(String ip)
    {
        ip += ".";
        String nuevaIP = "";
        int parte1 = 0, parte2 = 0, parte3 = 0, parte4 = 0;
        ArrayList<Integer> partes = obtenerPartes(ip);
        parte1 = partes.get(0);
        parte2 = partes.get(1);
        parte3 = partes.get(2);
        parte4 = partes.get(3);
        if(parte1 <= 254)
        {
            ++parte1;
        }
        else if(parte1 == 255 && parte2 <= 254)
        {
            parte1 = 0;
            ++parte2;
        }
        else if(parte1 == 255 && parte2 == 255 && parte3 <= 254)
        {
            parte1 = 0;
            parte2 = 0;
            ++parte3;
        }
        else if (parte1 == 255 && parte2 == 255 && parte3 == 255)
        {
            parte1 = 0;
            parte2 = 0;
            parte3 = 0;
            ++parte4;
        }
        nuevaIP = String.valueOf(parte4) + "." + String.valueOf(parte3) + "." + String.valueOf(parte2) + "." + String.valueOf(parte1);
        return nuevaIP;
    }
    
    public String decrementarIP(String ip)
    {
        ip += ".";
        String nuevaIP = "";
        int parte1 = 0, parte2 = 0, parte3 = 0, parte4 = 0;
        ArrayList<Integer> partes = obtenerPartes(ip);
        parte1 = partes.get(0);
        parte2 = partes.get(1);
        parte3 = partes.get(2);
        parte4 = partes.get(3);
        if(parte1 > 0)
        {
            --parte1;
        }
        else
        {
            parte1 = 255;
            --parte2;
            if(parte2 == -1)
            {
                parte2 = 255;
                --parte3;
                if(parte3 == -1)
                {
                    parte3 = 255;
                    --parte4;
                }
            }
        }
        nuevaIP = String.valueOf(parte4) + "." + String.valueOf(parte3) + "." + String.valueOf(parte2) + "." + String.valueOf(parte1);
        return nuevaIP;
    }
    
    public ArrayList<Integer> obtenerPartes(String ip)
    {
        ArrayList<Integer> partes = new ArrayList();
        int parte1 = 0, parte2 = 0, parte3 = 0, parte4 = 0;
        int contador = 0;
        int sub_i = 0;
        ip += ".";
        for(int i = 0; i < ip.length(); ++i)
        {
            if(ip.charAt(i) == '.')
            {
                switch(contador)
                {
                    case 0:
                        parte4 = Integer.parseInt(ip.substring(sub_i, i));
                        sub_i = i + 1;
                        break; 
                        
                    case 1:
                        parte3 = Integer.parseInt(ip.substring(sub_i, i));
                        sub_i = i + 1;
                        break;
                        
                    case 2:
                        parte2 = Integer.parseInt(ip.substring(sub_i, i));
                        sub_i = i + 1;
                        break;
                        
                    case 3:
                        parte1 = Integer.parseInt(ip.substring(sub_i, i));
                        sub_i = i + 1;
                        break;
                        
                    default:
                        break;
                }
                ++contador;
            }
        }
        partes.add(parte1);
        partes.add(parte2);
        partes.add(parte3);
        partes.add(parte4);
        return partes;
    }
    
    public void existenciaIP(String salida, String ip)
    {
        if(!salida.contains("(100%"))
        {
            this.tablaARP.add(ip);
        }
    }
    
    public static void main(String[] args) {
        ARP arp = new ARP("192.168.1.0", "192.168.1.100");
        arp.escanearRed();
    }
}