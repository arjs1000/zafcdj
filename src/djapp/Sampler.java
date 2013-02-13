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


public class Sampler {


	
	public static void main(String args[]){
	

			//create the audio context
		AudioContext audioContext = new AudioContext();
						
		
		// we load sound samples like this
		
		Sample drumLoopSample;
			/* Sample from 
			 http://www.freesound.org/samplesViewSingle.php?id=111978
			 
			 */
        drumLoopSample = SampleManager.sample("sounds/loop1.wav");
		
		/* the sample player plays back our sounds.
		We pass in the audio context and an initial sound sample
		*/
		SamplePlayer samplePlayer = new SamplePlayer(audioContext, drumLoopSample);


		//Change the pitch of a sound using the Pitch Envelope (takes a float)
		// 1.0f is normal speed
		// 0.5f is half speed
		// 2.0f is double
		// etc
		
		samplePlayer.getPitchEnvelope().setValue(2f);


		/*and we can set the type of loop.  We have these types to choose from
			LOOP_ALTERNATING
			LOOP_BACKWARDS
			LOOP_FORWARDS
			NO_LOOP_BACKWARDS
			NO_LOOP_FORWARDS 
		*/
		
		samplePlayer.setLoopType(SamplePlayer.LoopType.NO_LOOP_FORWARDS);
		
		//connect the sample player to the output	
	    audioContext.out.addInput(samplePlayer);    

		audioContext.start(); //start computing the audio
		
		
	}
}