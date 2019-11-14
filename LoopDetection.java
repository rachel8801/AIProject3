package ImageExtraction;

public class LoopDetection {
	char[][] object;
	int width;
	int length;
	int loops = 0;
	public LoopDetection(char[][] arr) {
		length = arr.length;
		width = arr[0].length;

		if(length > width){
			object = new char[length][length];
			width = length;
		}else{
			object = new char[width][width];
			length = width;
		}

		for(int i = 0 ; i < arr.length; i ++){
			for(int j = 0; j < arr[0].length; j++){
				object[i][j] = arr[i][j];
			}
		}
	}

	public boolean loop_count() {
		int zero = 0;
		int one = 0;
		int two = 0;
		int other = 0;

		for(int j = 0; j<4 ; j++){
			object = eliminate(object);
			object = invert(object);
		}

		int i = 0;

		for(int j = 0 ; j < 4 ; j++){
			i = loop(object);
			object = invert(object);
			switch(i){
			case 0:
				zero++;
				break;
			case 1:
				one++;
				break;
			case 2:
				two++;
				break;
			default:
				other++;
				break;

			}
		}
		loops = zero;
		int actual_loops = 0;
		if(one >= loops){
			loops = one;
			actual_loops = 1;
		}

		if(two >= loops){
			loops = two;
			actual_loops = 2;
		}
		if(one+ two + other > loops){
			actual_loops = 1;
		}

		return actual_loops > 0 ? true : false;
	}


	private int loop(char[][] obj) {
		/*
		for(char[] c : obj){
			System.out.println(c);
		}
		*/
		boolean found = false;
		for(char[] c : obj){
			String s = new String(c);
			if(s.contains("1")){
				found = true;
				break;
			}
		}
		if(!found){
			return 0;
		}


		int l = 0;
		for(int i = 1; i < length; i++) {
			String s = new String(obj[i]);
			s = s.trim();
			if(s.contains(" ")){
				boolean top = true;
				String s_prev = "";
				if(i != 0) {
					String curr_str = new String(obj[i]);
					int num_left_space = curr_str.indexOf('1');
					curr_str = curr_str.trim();
					s_prev = new String(obj[i-1]).substring(num_left_space);
					int counter = 0;
					if(s_prev.trim().length()!=0){
						while(counter < curr_str.length()) {
							if(curr_str.charAt(counter) == ' ') {
								if(s_prev.charAt(counter)== ' ') {
									top = false;
									break;
								}
							}
							counter++;
						}
					}else{
						top = false;
					}
				}
				if(top) {
					boolean coherrent = true;
					if(i < obj.length -1) {
						while(s.contains(" ") && s.trim().contains("1")) {
							if(i > length -1) {
								break;
							}
							s = new String(obj[i]);
							s = s.trim();

							if(s.contains(" ")){
								//get previous row
								s_prev = new String(obj[i-1]);
								//get current row
								String curr_s = new String(obj[i]);
								curr_s = curr_s.substring(s_prev.indexOf('1'));
								s_prev = s_prev.trim();
								int counter = 0;

								//checking coherrency
								while(counter < s_prev.length() - 1){
									if(s_prev.charAt(counter) == '1' && s_prev.charAt(counter + 1) == ' '){
										boolean block = false;
										int reverse = counter;
										while(reverse >= 0){

											if(curr_s.charAt(reverse) == '1'){
												block = true;
												break;
											}
											reverse --;
										}
										if(!block){
											coherrent = false;
											break;
										}
									}
									if(s_prev.charAt(counter) == ' ' && s_prev.charAt(counter + 1) == '1'){
										boolean block = false;
										int forward = counter;

										while(forward < s_prev.length()){
											if(curr_s.charAt(forward) == '1'){
												block = true;
												break;
											}
											forward++;
										}
										if(!block){
											coherrent = false;
											break;
										}
									}
									counter ++;
								}

								if(coherrent){
									if(i< length - 1) {
										i++;
										s_prev = new String(obj[i-1]);
										s = new String(obj[i]);

										s = s.substring(s_prev.indexOf('1'));
										s_prev = s_prev.trim();

										if(s.contains("1")){
											boolean bottom = true;
											int counter1 = 0;
											boolean start = false;
											while(counter1 < s_prev.length()) {
												if(s_prev.charAt(counter1) == ' ') {
													start = true;
													if(s.charAt(counter1)== ' ') {
														bottom = false;
														break;
													}
												}else {
													if(start == true) {
														String test = s_prev.substring(counter1);
														if(!test.contains(" ")){
															break;
														}
													}
												}
												counter1++;
											}
											if(bottom) {
												//System.out.println("row "+i+" : "+new String(obj[i]));
												l++;
											}
										}
									}
								}else{
									i++;
								}
							}else{
								i++;
							}
							s = new String(obj[i]).trim();
						}
					}
				}
			}
		}
		//System.out.println(l);
		return l ;
	}


	public char[][] eliminate(char[][] obj){

		char[][] replacement = new char[length][width];
		int i = 0;
		for(i = 0; i< length; i++){
			String s = new String(obj[i]);
			s = s.trim();
			//handles the top
			if(s.contains(" ") && s.contains("1") && i!=0) {
				if(i != 0) {
					replacement[i-1] = obj[i-1];
					replacement[i] = obj[i];
					i++;
					while(i<length -1){
						replacement[i] = obj[i];
						i++;
					}
				}
				return replacement;
			}
		}
		return replacement;
	}

	public char[][] invert(char[][] a) {

		int N = length;
		// Traverse each cycle
	    for (int i = 0; i < N / 2; i++)
	    {
	        for (int j = i; j < N - i - 1; j++)
	        {
	            // Swap elements of each cycle
	            // in clockwise direction
	            char temp = a[i][j];
	            a[i][j] = a[N - 1 - j][i];
	            a[N - 1 - j][i] = a[N - 1 - i][N - 1 - j];
	            a[N - 1 - i][N - 1 - j] = a[j][N - 1 - i];
	            a[j][N - 1 - i] = temp;
	        }
	    }

		return a;
	}

}
