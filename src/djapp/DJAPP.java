/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package djapp;

/**
 *
 * @author arjs1000
 * ZOHEB SEE THIS
 * WAGWANNNNN
 */
import net.beadsproject.beads.core.*;
import net.beadsproject.beads.data.*;
import net.beadsproject.beads.ugens.*;

import javax.swing.*;
import java.io.*;
import java.awt.event.*;

import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DJAPP extends JFrame{

        String filepath = "NA";
        String tname;
        int playCount = 0;
        JButton play;
        JButton pause;
        JButton stop;
        JButton browse;
        double trackpos = 0.0;
        final JLabel title = new JLabel("ZAFC DJ Application - BETA v1.01");
        final JLabel pitch = new JLabel("Pitch");
        final JLabel tempo = new JLabel("Tempo");
        final JLabel volume = new JLabel("Volume");
        JFileChooser fc;
        JLabel track;
        SamplePlayer player1;
	
        public static void main(String args[]){
		new DJAPP();
        }
        
        public DJAPP()
        {
                this.setTitle("ZAFC DJ Application - BETA v1.01");
                final AudioContext audioContext = new AudioContext();
		final Clock clock = new Clock(audioContext, 200);
                this.getContentPane().setLayout(new CardLayout());
                
                //Where instance variables are declared:
            JPanel cards;
            String CARD1 = "Now Playing";
            String CARD2 = "Mixing";  

//Where the components controlled by the CardLayout are initialized:
//Create the "cards".
            JPanel card1 = new JPanel();

            JPanel card2 = new JPanel();

//Create the panel that contains the "cards".
            

        
            final JSlider noteSlider = new JSlider(-12,12);  // 2 octave range
            final JSlider volumeSlider = new JSlider(0,100); // 0 - 100 volume used for gain
            final JSlider tempoSlider = new JSlider(30, 1900);
            
            
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
            
                clock.addMessageListener(
                    new Bead() {  //using an anonymous class created every time the clock ticks
                    @Override
                        public void messageReceived(Bead message) {
                            if(clock.isBeat())
                            {
                                float note = noteSlider.getValue();
                                float vol = volumeSlider.getValue();
                                float tempo = 2000 - tempoSlider.getValue();
                                float playbackRate = PitchRatioCalculator.semitoneRatio(98, note);
                                player1.getPitchEnvelope().setValue(playbackRate);
                               
                                audioContext.out.setGain(vol/100);
                                clock.getIntervalEnvelope().setValue(tempo);
                                
                            }
                        
                        
                       }
                    });
        
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
                        float vol = volumeSlider.getValue();
                        
                        float playbackRate = PitchRatioCalculator.semitoneRatio(98, note);
                        player1.getPitchEnvelope().setValue(playbackRate);
                
                        player1.setLoopType(SamplePlayer.LoopType.NO_LOOP_FORWARDS);
                        player1.setKillOnEnd(true);
                        audioContext.out.addInput(player1);
                            float gainLevel  = (float)(vol/100);
				Gain gain = new Gain(audioContext, 1, new Envelope(audioContext, gainLevel ));
				
				// add the sample player to the gain's input
				gain.addInput(player1);
				
                
                                    gain.setKillListener(player1); 
                
				// add the gain to the audio context's output
                        audioContext.out.addInput(gain);
                        audioContext.out.addDependent(clock);
                        
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Please select a song!");
                    }
                }
                else
                {
                    player1.setPosition(trackpos);
                    player1.pause(false);
                }

            }
        }); 
                
           pause.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    playCount +=1;
                    player1.pause(true);
                    trackpos = player1.getPosition();
                    
                }
           });
           
           stop.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent e) {
                   player1.pause(true);
                   playCount=0;
                   track.setText("No track selected");
                   filepath = "NA";
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
            
            
            // GAIN
            
        
            audioContext.start();
            
            JTabbedPane tabbedPane = new JTabbedPane();
            

            
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //this.getContentPane().add(title);
            card1.add(play);
            card1.add(pause);
            card1.add(stop);
            
            card1.add(track);
            card1.add(browse);
            
          
            card2.add(pitch);
            card2.add(noteSlider);
            card2.add(tempo);
            card2.add(tempoSlider);
            card2.add(volume);
            card2.add(volumeSlider);
            
            ImageIcon icon = null;
            tabbedPane.addTab("Now Playing", icon, card1,
                  "Track controls");
            tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

            
            tabbedPane.addTab("Mixer", icon, card2,
                  "DJ Mixing controls");
            tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
            
            
            
            this.getContentPane().add(tabbedPane);
            
            
            
            this.pack();
		this.setVisible(true);
		
		
       }
		
}
