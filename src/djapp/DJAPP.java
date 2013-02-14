/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package djapp;

/**
 *
 * @author arjs1000
 * 
 * WAGWANNNNN
 */
import net.beadsproject.beads.core.*;
import net.beadsproject.beads.data.*;
import net.beadsproject.beads.ugens.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*;

import java.awt.*;

public class DJAPP extends JFrame{

        String filepath = "NA";
        String tname;
        int playCount = 0;
        JButton play;
        JButton pause;
        JButton stop;
        JButton browse;
        final JLabel pitch = new JLabel("Pitch");
        JFileChooser fc;
        JLabel track;
        SamplePlayer player1;
	
        public static void main(String args[]){
		new DJAPP();
        }
        
        public DJAPP()
        {
                final AudioContext audioContext = new AudioContext();
		
                
		audioContext.start();
		
                this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
		final JSlider noteSlider = new JSlider(-12,12);  // 2 octave range

		
            play = new JButton("Play");
            pause = new JButton("Pause");
            stop = new JButton("Stop");
            browse = new JButton("Browse");
            fc = new JFileChooser();
            track = new JLabel("No track selected");
                // Combo box options
                //String[] playStrings = { "Loop1", "Loop2", "Loop3", "Kick"};
                //JComboBox pList = new JComboBox(playStrings);
                //pList.setSelectedIndex(0);
                //pList.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent e) {
//                JComboBox cb = (JComboBox)e.getSource();
//                String name = (String)cb.getSelectedItem();
//                if(name.equals("Loop1"))
//                {
//                     file = "sounds/loop1.wav";
//                }
//                else if(name.equals("Loop2"))
//                {
//                     file = "sounds/loop2.wav";
//                }
//                else if(name.equals("Loop3"))
//                {
//                     file = "sounds/loop3.wav";
//                }
//                else if(name.equals("Kick"))
//                {
//                     file = "sounds/kick.aif";
//                }
//                
//            }
//        });
        
                play.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                
                if(playCount==0)
                {
                    if(!(filepath.equals("NA")))    
                    {
                 
                        SampleManager sm = new SampleManager();
                        Sample sound = sm.sample(filepath);
                        player1 = new SamplePlayer(audioContext, sound);
                    
                        float note = noteSlider.getValue();
                
                        float playbackRate = PitchRatioCalculator.semitoneRatio(98, note);
                        player1.getPitchEnvelope().setValue(playbackRate);
                
                        player1.setLoopType(SamplePlayer.LoopType.NO_LOOP_FORWARDS);
                        player1.setKillOnEnd(true);
                        audioContext.out.addInput(player1);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Please select a song!");
                    }
                }
                else
                {
                    player1.pause(false);
                }

            }
        }); 
                
           pause.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    player1.pause(true);
                }
           });
           
           stop.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent e) {
                   player1.pause(true);
                   playCount=0;
               }
           });
                
            browse.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                
                    //Handle open button action.
          
                    int returnVal = fc.showOpenDialog(DJAPP.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) 
                        {
                            File file = fc.getSelectedFile();
                            filepath = file.getAbsolutePath();
                            tname = file.getName();
                            track.setText(tname);
                        }
                        else
                        {
                            filepath = "NA";
                        }
                }
            }); 
        
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.getContentPane().add(play);
            this.getContentPane().add(pause);
            this.getContentPane().add(stop);
            this.getContentPane().add(track);
            this.getContentPane().add(browse);
            this.getContentPane().add(pitch);
            this.getContentPane().add(noteSlider);
        
            this.pack();
		this.setVisible(true);
		
		
       }
		
}
