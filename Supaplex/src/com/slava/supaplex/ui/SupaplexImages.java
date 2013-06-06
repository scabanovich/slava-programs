package com.slava.supaplex.ui;

import java.awt.*;
import java.awt.image.*;
import java.net.URL;

import javax.swing.*;

public class SupaplexImages {
	Image EMPTY = getImage("images/empty.gif");
	Image ZONK = getImage("images/zonk.gif");
	Image BASE = getImage("images/base.gif");
	Image MURPHY = getImage("images/murphy.gif");
	Image INFOTRON = getImage("images/crystal.gif");
	Image PORT_UP = getImage("images/port-up.gif");
	Image PORT_DOWN = getImage("images/port-down.gif");
	Image PORT_RIGHT = getImage("images/port-right.gif");
	Image PORT_LEFT = getImage("images/port-left.gif");
	Image PORT_LEFT_RIGHT = getImage("images/port-left-right.gif");
	Image PORT_UP_DOWN = getImage("images/port-up-down.gif");
	Image PORT_ALL_WAY = getImage("images/port-all-way.gif");
	Image EXIT = getImage("images/exit.gif");
	Image SNIK_SNAK = getImage("images/snik-snak.gif");
	Image FALLING_DISC = getImage("images/falling-disk.gif");
	Image EATABLE_DISC = getImage("images/eatable-disk.gif");
	Image MOVABLE_DISC = getImage("images/movable-disk.gif");
	Image TERMINAL = getImage("images/terminal.gif");
	Image BUG = getImage("images/bug.gif");
	Image ELECTRON = getImage("images/electron.gif");

	Image CHIP = getImage("images/chip.gif");
	Image CHIP_1A = getImage("images/chip-1A.gif");
	Image CHIP_1B = getImage("images/chip-1B.gif");
	Image CHIP_26 = getImage("images/chip-26.gif");
	Image CHIP_27 = getImage("images/chip-27.gif");

	Image HARDWARE_06 = getImage("images/hardware.gif");
	Image HARDWARE_1C = getImage("images/hardware-1C.gif");
	Image HARDWARE_1F = getImage("images/hardware-1F.gif");
	Image HARDWARE_20 = getImage("images/hardware-20.gif");
	Image HARDWARE_21 = getImage("images/hardware-21.gif");
	Image HARDWARE_22 = getImage("images/hardware-22.gif");
	Image HARDWARE_25 = getImage("images/hardware-25.gif");
	
	Image[] images = new Image[]{
		null,                 // 00 
		ZONK,                 // 01
		BASE,                 // 02
		MURPHY,               // 03
		INFOTRON,             // 04
		CHIP,                 // 05
		HARDWARE_06,          // 06
		EXIT,                 // 07
		FALLING_DISC,         // 08
		PORT_RIGHT,           // 09
		PORT_DOWN,            // 0A
		PORT_LEFT,            // 0B
		PORT_UP,              // 0C
		
		PORT_RIGHT,           // 0D
		PORT_DOWN,            // 0E
		PORT_LEFT,            // 0F
		PORT_UP,              // 10
		
		SNIK_SNAK,            // 11
		MOVABLE_DISC,         // 12
		TERMINAL,             // 13
		EATABLE_DISC,         // 14
		PORT_UP_DOWN,         // 15
		PORT_LEFT_RIGHT,      // 16
		PORT_ALL_WAY,         // 17
		ELECTRON,             // 18
		BUG,                  // 19
		CHIP_1A,              // 1A
		CHIP_1B,              // 1B
		HARDWARE_1C,          // 1C
		null,                 // 1D
		null,                 // 1E
		HARDWARE_1F,          // 1F
		HARDWARE_20,          // 20
		HARDWARE_21,          // 21
		HARDWARE_22,          // 22
		null,                 // 23
		null,                 // 24
		HARDWARE_25,          // 25
		CHIP_26,              // 26
		CHIP_27,              // 27
		
	};
	
	Image getImage(byte b) {
		int i = (int)b;
		if(i < 0) i += 256;
		if(i >= images.length) return null;
		return images[i];
	}

	Image getImage(String name) {
		URL url = getClass().getResource(name);
		Image i = Toolkit.getDefaultToolkit().createImage(url);
		return makeTransparentBackground(i);
	}
	
	Color[] colors = new Color[]{
		null,            // 00 
		null,            // 01
		Color.GREEN,     // 02
		null,            // 03
		null,            // 04
		null,            // 05
		Color.GRAY,      // 06
		Color.YELLOW,    // 07
		Color.WHITE,     // 08
		null,            // 09
		null,            // 0A
		null,            // 0B
		null,            // 0C
		null,            // 0D
		null,            // 0E
		null,            // 0F
		null,            // 10
		null,            // 11
		Color.WHITE,     // 12
		null,            // 13
		Color.WHITE,     // 14
		null,            // 15
		null,            // 16
		null,            // 17
		null,            // 18
		null,            // 19
		null,            // 1A
		null,            // 1B
		Color.GRAY,      // 1C
		Color.GRAY,      // 1D
		Color.GRAY,      // 1E
		Color.GRAY,      // 1F
		Color.GRAY,      // 20
		Color.GRAY,      // 21
		Color.GRAY,      // 22
		Color.GRAY,      // 23
		Color.GRAY,      // 24
		Color.GRAY,      // 25
		null,            // 26
		null,            // 27
		Color.PINK,      // 28
		Color.PINK,      // 29
	};
	
	public Color getColor(int b) {
		int i = (int)b;
		if(i < 0) i += 256;
		if(i >= colors.length) return Color.BLACK;
		return colors[i] == null ? Color.BLACK : colors[i];
	}
	
	private Image makeTransparentBackground(Image i) {
		int w = new ImageIcon(i).getIconWidth();
		int h = new ImageIcon(i).getIconHeight();
		PixelGrabber g = new PixelGrabber(i, 0, 0, w, h, true);
		try {
			g.grabPixels();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			int[] pixels = (int[])g.getPixels();
			BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
			ColorModel m = b.getColorModel();
			for (int k = 0; k < pixels.length; k++) {
				int pixel = pixels[k];
				int x = k % w;
				int y = k / w;
				if(pixel == pixels[0]) {
					b.setRGB(x, y, 16777215);
				} else {
					b.setRGB(x, y, pixel);
				}
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}


}
