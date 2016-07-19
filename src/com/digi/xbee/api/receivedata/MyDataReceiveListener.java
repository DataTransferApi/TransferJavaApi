/**
 * Copyright (c) 2014-2015 Digi International Inc., All rights not expressly
 * granted are reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Digi International Inc. 11001 Bren Road East, Minnetonka, MN 55343
 * =======================================================================
 */
package com.digi.xbee.api.receivedata;

import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBeeMessage;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Class to manage the XBee received data that was sent by other modules in the
 * same network.
 *
 * <p>
 * Acts as a data listener by implementing the {@link IDataReceiveListener}
 * interface, and is notified when new data for the module is received.</p>
 *
 * @see IDataReceiveListener
 *
 */
public class MyDataReceiveListener implements IDataReceiveListener {
    /*
     * (non-Javadoc)
     * @see com.digi.xbee.api.listeners.IDataReceiveListener#dataReceived(com.digi.xbee.api.models.XBeeMessage)
     */

    private ArrayList<byte[]> img;
    private Integer countTramas;
    private Integer countData;
    private XBeeDevice myDevice;

    public MyDataReceiveListener(XBeeDevice myDevice) {
        this.countTramas = 0;
        this.countData = 0;
        this.img = new ArrayList<>();
        this.myDevice = myDevice;
    }

    @Override
    public void dataReceived(XBeeMessage xbeeMessage) {
        if (xbeeMessage != null) {
            System.out.println(">> numTra:" + countTramas);
            if (countTramas == 0) {
                String num = new String(xbeeMessage.getData());
                countData = Integer.parseInt(num);
                System.out.println(">>" + countData);
                countTramas++;
            } else if (countTramas != 0) {
                if (xbeeMessage.getData().length != 4) {
                    img.add(xbeeMessage.getData());
                } else {
                    myDevice.close();
                    System.out.println(">> Creando Imagen..!! ");
                    processImage();
                }
                countTramas++;
            }

        }
    }

    public void processImage() {
        Integer size = img.size() * 34;
        byte[] map = new byte[size];
        int i = 0;
        for (byte[] part : img) {
            for (int j = 0; j < part.length; j++) {
                map[i] = part[j];
                i++;
            }
        }
        System.out.println(">> " + map.length);
        createImage(map);
    }

    public void createImage(byte[] map) {
        try {
            String dirName = "X:\\app";
            byte[] alpha = extractBytes("X:\\00001.jpeg");
            if (alpha.length > map.length) {
                map = alpha;
                BufferedImage imag = ImageIO.read(new ByteArrayInputStream(map));
                ImageIO.write(imag, "jpg", new File(dirName, "snap.jpg"));
                System.out.println(">> finish..!!");
            }
        } catch (IOException ex) {
            System.out.println(">> " + ex.getMessage());
        }
    }

    public byte[] extractBytes(String ImageName) throws IOException {
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        return (data.getData());
    }
}
