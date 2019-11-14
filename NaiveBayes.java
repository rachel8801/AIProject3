package algorithm;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import ImageExtraction.ImageExtraction;
import ImageExtraction.LoopDetection;
import objects.Cell;
import objects.TestObject;

public class NaiveBayes {
	private double[][][] likelihood;
	private double K;

	private double[] frequency, priors;
	private int dim, data_size;
	private Type type;
	private double[] loops = new double[10];

	public enum Type{
		FACE, DIGIT
	}

	public NaiveBayes(double[] quantity, double[] priors, ArrayList<TestObject> digits, Type type, int repeat) throws IOException{
		dim = 0;
		data_size = 0;
		this.type = type;
		switch(type){
			case DIGIT:
				dim = 28;
				data_size = 10;
				break;
			case FACE:
				dim = 70;
				data_size = 2;

		}


		//instantiate
		likelihood = new double[data_size][dim][dim];

		this.frequency = quantity;
		this.priors = priors;

		//get loop data
				if(type == Type.DIGIT){
					for(int i = 0; i < digits.size(); i ++){
						TestObject to = digits.get(i);
						//check if a loop exists
						LoopDetection ld = new LoopDetection(to.object2D());
						boolean loop = ld.loop_count();
						//if a loop exists, add 1 to the digit class counter
						if(loop){
							loops[to.data]++;
						}
					}

					//get the probabilty of each class having a loop
					for(int i = 0; i < 10; i++){
						loops[i] /= frequency[i];
					}
				}

		//run number of time to increase accuracy
		for(int a = 0; a < repeat; a++){

			//get total likelihoods
			for(int i = 0 ; i < digits.size(); i++){

				//get the object in the training data
				TestObject d = digits.get(i);

				//make the object into a 2D cell array
				Cell[][] cells = d.getCellArray();

				//traverse through cell array for each cell
				for(int j = 0 ; j < cells.length; j++){
					for(int b = 0 ; b < cells.length; b++){
						if(cells[j][b].getData() != 0){

							//d.data is the type of object, increase count in cell[j][k] for d.data (digit class)
							likelihood[d.data][j][b]++;
						}
					}
				}
			}

		}

		//get the validation data
		ImageExtraction extract = new ImageExtraction();
		ArrayList<TestObject> validation;
		if(type == NaiveBayes.Type.DIGIT){
			validation = extract.imageExtract(ImageExtraction.Type.DIGIT_VALID);
		}else{
			validation = extract.imageExtract(ImageExtraction.Type.FACE_VALID);
		}

		//test the validation data without laplace smoothing
		double k = 1.0;
		double count = 0;
		double accuracy = 0;

		//finding the best k
		for(double k_potential = 1.0; k_potential < 50.1 ; k_potential += 0.5){
			for(TestObject t : validation){
				boolean test = this.test(t, k_potential);
				if(test == true){
					count++;
				}
			}
			double curr_accuracy = count/validation.size();
			if(curr_accuracy > accuracy){
				accuracy = curr_accuracy;
				k= k_potential;
			}
			count = 0;
		}
		System.out.println("Chosen k = " + k);
		System.out.println("validation accuracy : " + accuracy);
		K = k;

	}

	public double getK(){
		return K;
	}

	public void printLikelihood(int data){
		DecimalFormat df = new DecimalFormat("#.##");
		if(data <= data_size){
			for(int i = 0; i < dim ; i++){
				for(int j = 0; j < dim ; j++){
					String s = df.format(likelihood[data][i][j]) + "";
					if(s.equals("0")){
						s = "0.00";
					}
					System.out.print(s + " ");
				}
				System.out.println();
			}
		}
	}


	public boolean test(TestObject d, double k){
		Cell[][] img = d.getCellArray();
		double[] probability = new double[10];

		//transfer priors to new array
		for(int i = 0 ; i < data_size ; i++){
			probability[i] = priors[i];
		}

		for(double p : probability){
			p = Math.log(p);
		}

		/**
		 * Update probability of each digit class for each cell[i][j]
		 */

		for(int digit = 0; digit < data_size; digit++ ){
			for(int i = 0; i < img.length; i++){
				for(int j = 0; j < img.length; j++){
					//if cell[i][j] has pixel, update probability
					if(img[i][j].getData()!=0){
						//laplace smoothing done during testing
						probability[digit] += Math.log((likelihood[digit][i][j] + k)/(frequency[digit]+k*2));

					}
				}
			}
		}

		if(type == Type.DIGIT){
			LoopDetection ld = new LoopDetection(d.object2D());
			//if a loop exists add the probability that a loop exists to each digit class.
			//if a loop does not exist add 1 - probability that a loop exists to teach digit class.
			if(ld.loop_count()){
				for(int i = 0; i <10 ; i++){
					probability[i] += Math.log(loops[i]);
				}
			}else{
				for(int i = 0; i <10 ; i++){
					probability[i] += Math.log(1- loops[i]);
				}
			}
		}

		//choose the best probability
		int best_p = 0;
		for(int i = 1; i < data_size; i++){
			if(probability[best_p]<probability[i]){
				best_p = i;
			}
		}

		//assign the chosen value to the object and returns a boolean
		d.assign_value(best_p);
		return d.checkData();
	}
}
