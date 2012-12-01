
public class FlashSort implements SortingAlgorithm {

	private int scale = 2;
	
	@Override
	public int[] sort(int[] subject, int lowerLimit, int upperLimit) {
		
		int result[] = new int[scale*subject.length];
		
		int len = subject.length;
		int index;
		
		double a_min = lowerLimit;
		double a_range = upperLimit - lowerLimit;
		
		
		for(int i = 0; i < len; i++){
			index = 1+(int)(  scale*((len-1)*((double)subject[i]-a_min)) / (a_range)  );
			
			add(subject[i], result, index, len);
		}
		
		
		mergin(subject, result);
		insertionSort(subject);
				
		return subject;
	}
	public void mergin(int[] target, int[] values){
		int pointer = 0;
		for(int i = 0; i< values.length; i++){
			if(values[i] != 0){
				target[pointer] = values[i];
				pointer++;
			}
		}
	}
	
	public int[] insertionSort(int[] target) {

		int len = target.length;
		for (int i = 1; i < len; i++){
			  int j = i;
			  int B = target[i];
			  while ((j > 0) && (target[j-1] > B)){
				  target[j] = target[j-1];
				  j--;
			  }
			  target[j] = B;
		  }
		  
		
		return target;
	}
	
	
	private void add(int toBeAdded, int[] result, int index, int len){
		if(result[index] == 0){
			result[index] = toBeAdded;
			return;
		}else if(result[index] <= toBeAdded){
			int i = 1;
			while(index+i < len){
				if(result[index+i] == 0){
					result[index+i] = toBeAdded;
					return;
				}
				i++;
			}
			i=1;
			while(index-i >= 0){
				if(result[index-i] == 0){
					result[index-i] = toBeAdded;
					return;
				}
				i++;
			}		
		}else{
			int i = 1;
			while(index-i >= 0){
				if(result[index-i] == 0){
					result[index-i] = toBeAdded;
					return;
				}
				i++;
			}
			i=1;
			while(index+i < len){
				if(result[index+i] == 0){
					result[index+i] = toBeAdded;
					return;
				}
				i++;
			}
		}
	}
	

	@Override
	public String getName() {
		return "FlashSort";
	}

}
