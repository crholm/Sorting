
public class Flashsort implements SortingAlgorithm {

	@Override
	public int[] sort(int[] subject, int lowerLimit, int upperLimit) {
		
		int result[] = new int[subject.length];
		
		int len = subject.length;
		int index;
		
		double a_min = lowerLimit;
		double a_range = upperLimit - lowerLimit;
		
		
		for(int i = 0; i < len; i++){
			index = 1+(int)(  ((len-1)*((double)subject[i]-a_min)) / (a_range)  );
			
			add(subject[i], result, index, len);
		}
		
		
		
		return new InsertionSort().sort(result, lowerLimit, upperLimit);
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
		return "Flash Sort";
	}

}
