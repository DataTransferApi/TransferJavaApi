/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xbee.java.test;

import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;

/**
 *
 * @author Luis
 */
public class test {

    /**
     * @param args the command line arguments
     */
    private static final String PORT = "COM5";
    // TODO Replace with the baud rate of your sender module.
    private static final int BAUD_RATE = 9600;

    private static final String DATA_TO_SEND = "Prueba transmisión de datos XBEE!";

    public static void main(String[] args) {
        // TODO code application logic here
        XBeeDevice myDevice = new XBeeDevice(PORT, BAUD_RATE);
        byte[] dataToSend = DATA_TO_SEND.getBytes();

        try {
            myDevice.open();

            System.out.format("Sending broadcast data: '%s'", new String(dataToSend));

            myDevice.sendBroadcastData(dataToSend);

            System.out.println(" >> Success");

        } catch (XBeeException e) {
            System.out.println(" >> Error");
            e.printStackTrace();
            System.exit(1);
        } finally {
            myDevice.close();
        }
    }

}
