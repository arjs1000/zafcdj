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


public class GUISamplePlayer extends JFrame {

        String file = "sounds/loop1.wav";
	public static void main(String args[]){
		new GUISamplePlayer();
		
		
		
	}
	
	GUISamplePlayer() {
		final AudioContext audioContext = new AudioContext();
		
		audioContext.start();
		
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
		final JSlider noteSlider = new JSlider(-12,12);  // 2 octave range

		
        JButton play = new JButton("Play");
        
        // Combo box options
        String[] playStrings = { "Loop1", "Loop2", "Loop3", "Kick"};
        JComboBox pList = new JComboBox(playStrings);
        pList.setSelectedIndex(0);
        pList.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String name = (String)cb.getSelectedItem();
                if(name.equals("Loop1"))
                {
                     file = "sounds/loop1.wav";
                }
                else if(name.equals("Loop2"))
                {
                     file = "sounds/loop2.wav";
                }
                else if(name.equals("Loop3"))
                {
                     file = "sounds/loop3.wav";
                }
                else if(name.equals("Kick"))
                {
                     file = "sounds/kick.aif";
                }
                
            }
        });
        play.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
                Sample sound = SampleManager.sample(file);	
                SamplePlayer samplePlayer = new SamplePlayer(audioContext, sound);
                
                float note = noteSlider.getValue();
                
                float playbackRate = PitchRatioCalculator.semitoneRatio(98, note);
                samplePlayer.getPitchEnvelope().setValue(playbackRate);
                
                samplePlayer.setLoopType(SamplePlayer.LoopType.NO_LOOP_FORWARDS);
                samplePlayer.setKillOnEnd(true);
                audioContext.out.addInput(samplePlayer);    

            }
        }); 
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(play);
        this.getContentPane().add(pList);
        this.getContentPane().add(noteSlider);
        
        this.pack();
		this.setVisible(true);
		
		
	}
}
