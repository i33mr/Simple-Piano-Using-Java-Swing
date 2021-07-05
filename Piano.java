import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.sound.midi.*;

public class Piano {
	Synthesizer synth;
	MidiChannel[] mChannels;
	Piano(){
		JFrame frame = new JFrame("Pea-Air-Know");
		JButton[] w = new JButton[14]; // 2 octaves
		JButton[] b = new JButton[13];
		JLayeredPane panel = new JLayeredPane();
		frame.add(panel);

		for (int i = 0; i < 14; i++) {
			w[i] = new JButton();
			w[i].setBackground(Color.WHITE);
			w[i].setLocation(i * 70, 0);
			w[i].setSize(70, 300);
			// setting the name of each white button to the right note, its used as the noteNumber parameter in noteOn()
			if(i < 3)
				w[i].setName(Integer.toString(60 + 2*i));
			else if(i >= 3 && i < 7)
				w[i].setName(Integer.toString(59 + 2*i));
			else if(i >= 7 && i < 10)
				w[i].setName(Integer.toString(58 + 2*i));
			else 
				w[i].setName(Integer.toString(57 + 2*i));
			w[i].addChangeListener(new PianoListeners()); // setting the changeListener
			w[i].setFocusable(false); // to solve the problem of buttons getting the focus after clicking by mouse, without it, you can't use keyboard after using the mouse. 
			panel.add(w[i], 0, -1); // back (0)
		}

		for (int i = 0; i < 13; i++) {
			if (i==2 || i == 6 || i == 9) // skip the unwanted black keys
				continue;
			b[i] = new JButton();
			b[i].setBackground(Color.BLACK);
			b[i].setForeground(Color.WHITE);
			b[i].setLocation(45 + i * 70, 0); 
			b[i].setSize(50, 150);
			// setting the name of each black button to the right note, its used as the noteNumber parameter in noteOn()
			if(i < 2)
				b[i].setName(Integer.toString(61 + 2*i));
			else if(i >= 2 && i < 6)
				b[i].setName(Integer.toString(60 + 2*i));
			else if(i >= 6 && i < 9)
				b[i].setName(Integer.toString(59 + 2*i));
			else
				b[i].setName(Integer.toString(58 + 2*i));
			b[i].addChangeListener(new PianoListeners());// setting the changeListener
			b[i].setFocusable(false);// to solve the problem of buttons getting the focus after clicking by mouse, without it, you can't use keyboard after using the mouse. 
			panel.add(b[i], 1, -1); // front (1)
		}
		// Black Buttons' labels
		b[0].setText("A");
		b[1].setText("S");
		b[3].setText("F");
		b[4].setText("G");
		b[5].setText("H");
		b[7].setText("K");
		b[8].setText("L");
		b[10].setText("\'");
		b[11].setText("↩"); // doesn't work with command prompt due to the encoding, consider java -Dfile.encoding=UTF-8 Piano
		b[12].setText("4");

		// White Buttons' labels
		w[0].setText("LSHFT");
		w[1].setText("Z");
		w[2].setText("X");
		w[3].setText("C");
		w[4].setText("V");
		w[5].setText("B");
		w[6].setText("N");
		w[7].setText("M");
		w[8].setText(",");
		w[9].setText(".");
		w[10].setText("/");
		w[11].setText("RSHFT");
		w[12].setText("1");
		w[13].setText("2");
		
		frame.addKeyListener(new KeyListener(){ // setting the keyListener for the whole frame.
			@Override
			public void keyTyped (KeyEvent e){

			}
			@Override
			public void keyPressed (KeyEvent e){
				switch(e.getKeyCode()){ // click the right button according to the keyCode, the clicking will trigger the ChangeListener (PianoListener)
					case 16: 
						if(e.getKeyLocation() == 2)
							w[0].doClick();
						else 
							w[11].doClick();
					break;
					case 'Z': w[1].doClick();
					break;
					case 'X':w[2].doClick();
					break;
					case 'C':w[3].doClick();
					break;
					case 'V':w[4].doClick();
					break;
					case 'B':w[5].doClick();
					break;
					case 'N':w[6].doClick();
					break;
					case 'M':w[7].doClick();
					break;
					case ',':w[8].doClick();
					break;
					case '.':w[9].doClick();
					break;
					case '/':w[10].doClick();
					break;
					case 97:w[12].doClick();
					break;
					case 98:w[13].doClick();
					break;
					//black
					case 'A':b[0].doClick();
					break;
					case 'S':b[1].doClick();
					break;
					case 'F':b[3].doClick();
					break;
					case 'G':b[4].doClick();
					break;
					case 'H':b[5].doClick();
					break;
					case 'K':b[7].doClick();
					break;
					case 'L':b[8].doClick();
					break;
					case 222:b[10].doClick();
					break;
					case 10 :b[11].doClick();
					break;
					case 100:b[12].doClick();
					break;
				}
			}
			@Override
			public void keyReleased (KeyEvent e){

			}
		});
		try{ 
			synth=MidiSystem.getSynthesizer();
			synth.open();
			mChannels = synth.getChannels();
		} 
		catch (MidiUnavailableException e){ 
			JOptionPane.showMessageDialog(null,"Unable to open MIDI.");
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 320);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setFocusable(true); 
	}
	public static void main(String[] args) {
		new Piano();	
	}

	private class PianoListeners implements ChangeListener{
			@Override
			public void stateChanged(ChangeEvent e){
				AbstractButton aButton = (AbstractButton) e.getSource();
				if(aButton.getModel().isPressed()){ // check that the changeState is pressing.
					System.out.println("Note Number:" + aButton.getName());
					mChannels[0].noteOn(Integer.parseInt(aButton.getName()),127); // every button has its name set to the note number, so w0 = 60 and w13 = 83
				}

			}
	}
}
