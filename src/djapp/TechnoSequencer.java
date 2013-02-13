/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package djapp;

/**
 *
 * @author arjuns
 */
/*
	A little sequencer demonstration, showing how to mix Beads and Swing 
	components to make banging techno tunez.
	
	Robin Fencott 2011
	
*/




import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import net.beadsproject.beads.core.*;
import net.beadsproject.beads.events.*;
import net.beadsproject.beads.ugens.*;
import net.beadsproject.beads.data.*;
import java.util.*;
import java.io.File;

class TechnoSequencer extends JFrame {
	
	ArrayList<SequencerRow> sequencerList = new ArrayList<SequencerRow>();
	
	
	public static void main(String args[]){
		new TechnoSequencer();	
	}


	TechnoSequencer(){
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));		
	
		
		//beads objects.
		// see http://www.beadsproject.net/doc/
		final AudioContext ac = new AudioContext(); 
		final Clock clock = new Clock(ac, 1500);	
		
		
		//create the slider which controls the speed of the sequencer
		final JSlider tempo = new JSlider(30, 1900);
		tempo.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				clock.getIntervalEnvelope().setValue(2000 - tempo.getValue());
				//set the interval of the clock to the slider value
			}
		});
		Box tempoBox = Box.createVerticalBox();
		tempoBox.add(new JLabel("Speed"));
		tempoBox.add(tempo);
		
	
		// create the button which allows us to add new rows to the sequencer
		JButton addRowButton = new JButton("Add Row");
		addRowButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				addSequencerRow();
			}
		});
		
		// add these controls to the Frame using a Box to organise them	
		Box controlBar = Box.createHorizontalBox();
		controlBar.add(addRowButton);
		controlBar.add(tempoBox);		
		add(controlBar);
		

		
		// create a callback for the Clock, so we can then
		// use the Clock to trigger events 
		clock.addMessageListener(
			new Bead() {  //using an anonymous class created every time the clock ticks
				public void messageReceived(Bead message) {

					int beat = clock.getInt() % 16;   // turn the clock's count into a number between 0 and 15
					
					
					// on each beat, run through the List of SequencerRows and notify them
					// that the beat has changed
					for(SequencerRow row : sequencerList){
						row.setBeat(beat, ac);	// passing in the beat number and the audio context
					}
				}
			});   // end of Clock Message Listener
			
			
		ac.out.addDependent(clock);  //add the clock as dependant so it gets computed by the audio context
		ac.start();  //start the audio context

		
		addSequencerRow();		//add one sequencer row to start off with
		pack(); // pack the Frame
		setVisible(true); //show it
		
	}
	
	private void addSequencerRow(){
		// helper method to add new Sequencer Rows to the frame 
		
		SequencerRow row = new SequencerRow();  // create the Row
		add(row);  //add it to the frame
		add(Box.createRigidArea(new Dimension(1, 10)));
		sequencerList.add(row);  //add it to the list (so we can call it from the Clock)
		pack();  // pack the frame
	}




	
	
	
	private class SequencerRow extends JPanel{
		// a class which defines a row in the sequencer
		
	
		private JCheckBox[] sequenceBoxes = new JCheckBox[16];  // an array of check boxes
		private int currentBeat = 0;
		
		private JComboBox soundFilesCombo; //combo box to select which sound file to play
		private JSlider volumeSlider;  // slider for the volume
		
		
		private	String[] soundFiles = {"kick.aif", "snare.aif", "hat.wav", "crash.wav", "BassNote_C.aif", "BassNote_D#.aif", "BassNote_F.aif", "BassNote_A#.aif"};  // the sound files (found in the 'sounds' folder)
		
		
		
		SequencerRow(){
			setLayout(new FlowLayout());
			// create a box to store all the checkboxes
			Box sequenceBox = Box.createHorizontalBox();	 // a horizontal box to organise the checkboxes
		
			for(int i=0;i<16;++i){   // create the 16 steps of the sequencer
			
				sequenceBoxes[i] = new JCheckBox();

				sequenceBoxes[i].setOpaque(true);   // set opaque so we can change the background colour	
				sequenceBoxes[i].setBackground(Color.LIGHT_GRAY);
				sequenceBox.add(sequenceBoxes[i]);  // add them to the Box
				
					// this creates little spaces every 4 boxes along the row using a RigidArea.
					// this isn't essential, but makes programming beats a little easier
				if(i==3 || i==7 || i==11) {
                                sequenceBox.add(Box.createRigidArea(new Dimension(10,1)));
                            }
			}

			add(sequenceBox);
				
				// create and add the sound file selector
			soundFilesCombo = new JComboBox(soundFiles);
			Box sampleBox = Box.createVerticalBox();
			sampleBox.add(new JLabel("Sound"));
			sampleBox.add(soundFilesCombo);
			add(sampleBox);	
			
			// create and add the volume slider
			volumeSlider = new JSlider(0, 150);
			Box volumeBox = Box.createVerticalBox();
			volumeBox.add(new JLabel("Volume"));
			volumeBox.add(volumeSlider);
			add(volumeBox);	
		}
		
		
		public void setBeat(int beat, AudioContext ac){
			/* This method is called from the Clock message listener at the start of each beat.
			*/
			
			sequenceBoxes[currentBeat].setBackground(Color.LIGHT_GRAY);  // set the old step to boring grey colour
			currentBeat = beat;
			sequenceBoxes[currentBeat].setBackground(new Color(125, 38, 205)); // make the active step purple!
			
			if(sequenceBoxes[currentBeat].isSelected()){  // if the box is selected then play a sound 
			
			
			
			// get the currently selected sound file, and add the folder path to it,
			// note use of the File.separator to ensure platform independence.
				String soundFile = "sounds/"+ soundFiles[soundFilesCombo.getSelectedIndex()];  
				
				
				//load the sound
					// in Beads the first time a sound is loaded it gets stored into memory, so you may
					// hear a click or a pause the first time, but after that it should be fine.
				Sample selectedSample;
                                selectedSample = SampleManager.sample(soundFile);
				
				// create a sample player
				SamplePlayer samplePlayer = new SamplePlayer(ac,selectedSample);
				
				// set to kill on end, so when the sample finished
				// the sample player is removed from the audio context (to free up CPU resources)
				samplePlayer.setKillOnEnd(true);  
	
				//scale the volume with a Gain, and use the value from the volume slider
				float gainLevel  = (float)volumeSlider.getValue()/100;
				Gain gain = new Gain(ac, 1, new Envelope(ac, gainLevel ));
				
				// add the sample player to the gain's input
				gain.addInput(samplePlayer);
				
                
                                    gain.setKillListener(samplePlayer); 
                
				// add the gain to the audio context's output
				ac.out.addInput(gain);
			}
		}
	}
	
}	
