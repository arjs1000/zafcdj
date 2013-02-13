/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package djapp;

/**
 *
 * @author arjuns
 */

import net.beadsproject.beads.core.*;
import net.beadsproject.beads.data.*;
import net.beadsproject.beads.ugens.*;

import javax.swing.*;
import java.awt.event.*;

import java.awt.*;


public class GUIClockExample extends JFrame {
    
    
	public static void main(String args[]){
        new GUIClockExample();
		
		
		
	}
	
	GUIClockExample() {
		final AudioContext ac = new AudioContext();
        final Clock clock = new Clock(ac, 1000);	

		ac.start();
		
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
		final JSlider noteSlider = new JSlider(-12,12);  // 2 octave range
        
   		final JSlider volumeSlider = new JSlider(0,100);  // 2 octave range
        
		
        
        clock.addMessageListener(
            new Bead() {  //using an anonymous class created every time the clock ticks
                @Override
                public void messageReceived(Bead message) {
                
                    Sample selectedSample;
                    selectedSample = SampleManager.sample("sounds/kick.aif");
                    SamplePlayer samplePlayer = new SamplePlayer(ac,selectedSample);
                    samplePlayer.setKillOnEnd(true);  
                    float gainLevel  = (float)0.5;

                    
                    float note = noteSlider.getValue();
                    
                    float playbackRate = PitchRatioCalculator.semitoneRatio(98, note);
                    samplePlayer.getPitchEnvelope().setValue(playbackRate);

                    float volume = volumeSlider.getValue()/100.0f;
                    
                    Envelope gain = new Envelope(ac, gainLevel);                    
                    
                    gain.addInput(samplePlayer);
                    gain.setKillListener(samplePlayer);
                
                    ac.out.addInput(gain);					
            }
        });   // end of Clock Message Listener
        
        
        
        
        
        ac.out.addDependent(clock);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
        
        this.getContentPane().add(new JLabel("note"));
        this.getContentPane().add(noteSlider);
        this.getContentPane().add(new JLabel("volume"));                              
         this.getContentPane().add(volumeSlider);
        
        this.pack();
		this.setVisible(true);
		
		
	}
}
